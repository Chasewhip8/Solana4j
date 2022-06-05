package dev.whips.solana4j.client.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.whips.solana4j.SolanaCluster;
import dev.whips.solana4j.client.RPCMethod;
import dev.whips.solana4j.client.RPCRequest;
import dev.whips.solana4j.client.RPCResponse;
import dev.whips.solana4j.client.RPCUtils;
import dev.whips.solana4j.client.providers.JacksonMappingsProvider;
import dev.whips.solana4j.exceptions.RPCException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SolanaWebSocketClient extends WebSocketClient {
    private final HashMap<Integer, RPCSubscription<?>> pendingSubscriptions;
    private final HashMap<Long, RPCSubscription<?>> subscriptions;
    private final ObjectMapper objectMapper;

    private int expectOtherResponse = 0;
    private boolean connected;

    public SolanaWebSocketClient(SolanaCluster cluster) {
        super(cluster.getWebSocketEndpoint());
        this.subscriptions = new HashMap<>();
        this.pendingSubscriptions = new HashMap<>();
        this.objectMapper = JacksonMappingsProvider.createObjectMapper(
                DeserializationFeature.USE_LONG_FOR_INTS);
    }

    @SuppressWarnings("unchecked")
    public <T> Subscription<T> subscribe(RPCMethod rpcMethod, TypeReference<RPCNotification<T>> typeReference,
                                         NotificationListener<RPCNotification<T>> listener, Object... params) throws RPCException {
        if (!connected || isClosed()){
            try {
                connectBlocking();
                this.connected = true;
            } catch (InterruptedException e){
                e.printStackTrace();
                this.connected = false;
            }
        }

        final int paramHash = calculateParamHash(params);
        for (RPCSubscription<?> subscription : subscriptions.values()){
            if (rpcMethod.equals(subscription.getMethod()) && subscription.hashCode() == paramHash){
                RPCSubscription<T> rawSubscription = (RPCSubscription<T>) subscription;
                rawSubscription.addListener(listener);
                return new Subscription<T>(rawSubscription, listener);
            }
        }

        final int subscriptionId = RPCUtils.generateUniqueId();

        List<Object> paramList = List.of(params);
        RPCSubscription<T> subscription = new RPCSubscription<T>(rpcMethod, typeReference, paramList, paramHash);
        subscription.addListener(listener);
        addPendingSubscription(subscriptionId, subscription);

        try {
            RPCRequest request = new RPCRequest("2.0", subscriptionId, rpcMethod.toString(), paramList);
            final byte[] data = objectMapper.writeValueAsBytes(request);
            setExpectOtherResponse();
            send(data);
        } catch (IOException e) {
            removePendingSubscription(subscriptionId);
            throw new RPCException(e.getMessage());
        }

        return new Subscription<>(subscription, listener);
    }

    public void unSubscribe(RPCMethod method, Subscription<?> wrappedSubscription) throws RPCException{
        RPCSubscription<?> subscription = wrappedSubscription.getSubscription();
        NotificationListener<?> listener = wrappedSubscription.getListener();

        if (!subscription.getListeners().contains(listener)){
            return;
        }

        if (subscription.getListeners().size() > 1){
            subscription.getListeners().remove(listener);
        } else {
            removeSubscription(subscription.getSubscription());

            try {
                RPCRequest request = new RPCRequest("2.0", RPCUtils.generateUniqueId(),
                        method.toString(),
                        Collections.singletonList(subscription.getSubscription()));
                final byte[] data = objectMapper.writeValueAsBytes(request);

                setExpectOtherResponse(); // Expecting a response message from this
                send(data);
            } catch (IOException e) {
                throw new RPCException(e.getMessage());
            }
        }
    }

    private int calculateParamHash(Object... params){
        return Arrays.hashCode(params);
    }

    private void addPendingSubscription(int identifier, RPCSubscription<?> subscription){
        pendingSubscriptions.put(identifier, subscription);
    }

    private RPCSubscription<?> getPendingSubscription(int identifier){
        return pendingSubscriptions.get(identifier);
    }

    private void removePendingSubscription(int identifier){
        pendingSubscriptions.remove(identifier);
    }

    private void addSubscription(long identifier, RPCSubscription<?> subscription) {
        subscriptions.put(identifier, subscription);
    }

    private RPCSubscription<?> getSubscription(long identifier){
        return subscriptions.get(identifier);
    }

    private void removeSubscription(long identifier){
        subscriptions.remove(identifier);
    }

    private void setExpectOtherResponse(){
        this.expectOtherResponse++;
    }

    private void unSetExpectOtherResponse(){
        this.expectOtherResponse--;
    }

    private boolean isExpectingOtherResponse(){
        return expectOtherResponse != 0;
    }

    @Override
    @SuppressWarnings("unchecked, rawtypes")
    public void onMessage(String message) {
        if (isExpectingOtherResponse()) {
            try {
                RPCResponse<Object> idResponse = objectMapper.readValue(message.getBytes(), new TypeReference<>(){});

                if (idResponse != null && idResponse.getError() == null) {
                    int requestId = idResponse.getId();
                    Object object = idResponse.getResult();

                    if (object instanceof Long id){
                        RPCSubscription<?> subscription = getPendingSubscription(requestId);
                        if (subscription != null) {
                            subscription.setSubscription(id);
                            removePendingSubscription(requestId);
                            addSubscription(id, subscription);
                        }
                    }

                    unSetExpectOtherResponse();
                    return;
                }
            } catch (IOException e) {
                // Skip
            }
        }

        try {
            RPCNotification<?> notification = objectMapper.readValue(message.getBytes(), RPCNotification.class);

            if (notification == null){
                return;
            }

            long id = notification.getParams().getSubscription();
            RPCSubscription<?> subscription = getSubscription(id);
            RPCNotification realNotification = objectMapper.readValue(message.getBytes(), subscription.getTypeReference());

            subscription.getListeners().forEach(listener -> listener.call(realNotification));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (tryReconnection()){
            reInitializeSubscriptions();
        } else {
            if (tryReconnection()){
                reInitializeSubscriptions();
            } else {
                // TODO switch to fallback cluster

            }
        }

        // TODO reconnect and resend all subscriptions
    }

    private boolean tryReconnection(){
        try {
            return reconnectBlocking();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }

    private void reInitializeSubscriptions(){

    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
