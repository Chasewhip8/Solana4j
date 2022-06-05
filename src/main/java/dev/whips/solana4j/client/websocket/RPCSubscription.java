package dev.whips.solana4j.client.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.whips.solana4j.client.RPCMethod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RPCSubscription<T> {
    private final RPCMethod method;
    private final Set<NotificationListener<RPCNotification<T>>> listeners;
    private final TypeReference<RPCNotification<T>> typeReference;
    private final List<Object> params;
    private final int paramHash;

    private long subscription;
    private boolean open;

    public RPCSubscription(RPCMethod method, TypeReference<RPCNotification<T>> typeReference, List<Object> params, int paramHash) {
        this.method = method;
        this.listeners = new HashSet<>();
        this.typeReference = typeReference;
        this.params = params;
        this.paramHash = paramHash;
    }

    public void addListener(NotificationListener<RPCNotification<T>> listener){
        listeners.add(listener);
    }

    public RPCMethod getMethod() {
        return method;
    }

    public long getSubscription() {
        return subscription;
    }

    public void setSubscription(long subscription) {
        this.subscription = subscription;
        this.open = true;
    }

    public boolean isOpen() {
        return open;
    }

    public Set<NotificationListener<RPCNotification<T>>> getListeners() {
        return listeners;
    }

    public TypeReference<RPCNotification<T>> getTypeReference() {
        return typeReference;
    }

    @Override
    public int hashCode() {
        return paramHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RPCSubscription<?> sb){
            return hashCode() == sb.hashCode();
        }
        return false;
    }
}
