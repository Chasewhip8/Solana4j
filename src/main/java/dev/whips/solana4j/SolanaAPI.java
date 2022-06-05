package dev.whips.solana4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.RPCClient;
import dev.whips.solana4j.client.RPCMethod;
import dev.whips.solana4j.client.RPCResponse;
import dev.whips.solana4j.client.data.*;
import dev.whips.solana4j.client.data.config.GeneralConfig;
import dev.whips.solana4j.client.data.config.ProgramAccountConfig;
import dev.whips.solana4j.client.data.enums.RPCCommitment;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.filters.DataSliceFilter;
import dev.whips.solana4j.client.data.filters.Filter;
import dev.whips.solana4j.client.data.results.ContextResult;
import dev.whips.solana4j.client.websocket.NotificationListener;
import dev.whips.solana4j.client.websocket.RPCNotification;
import dev.whips.solana4j.client.websocket.SolanaWebSocketClient;
import dev.whips.solana4j.client.websocket.Subscription;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.programs.UpdatePropagatorManager;

import java.util.List;

public class SolanaAPI {
    private final RPCClient rpcClient;
    private final SolanaWebSocketClient webSocketClient;
    private final UpdatePropagatorManager updatePropagatorManager;

    public SolanaAPI(SolanaCluster cluster, int readTimeout) {
        this.rpcClient = new RPCClient(cluster, readTimeout);
        this.webSocketClient = new SolanaWebSocketClient(cluster);
        this.updatePropagatorManager = new UpdatePropagatorManager();
    }

    private void checkError(RPCResponse<?> response) throws RPCException {
        if (response == null){
            throw new RPCException("Response was null!");
        }
        if (response.getError() != null){
            throw new RPCException(response.getError());
        }
    }

    public UpdatePropagatorManager getUpdatePropagatorManager() {
        return updatePropagatorManager;
    }

    public AccountInfo getAccountInfo(PubKey pubKey, RPCEncoding encoding) throws RPCException {
        RPCResponse<ContextResult<AccountInfo>> response = rpcClient.call(
                RPCMethod.GET_ACCOUNT_INFO, new TypeReference<>(){},
                pubKey, new GeneralConfig(encoding)
        );
        checkError(response);

        return response.getResult().getValue();
    }

    public UnsignedLong getBalance(PubKey pubKey) throws RPCException{
        RPCResponse<ContextResult<UnsignedLong>> response = rpcClient.call(
                RPCMethod.GET_BALANCE, new TypeReference<>(){},
                pubKey
        );
        checkError(response);

        return response.getResult().getValue();
    }

    public List<ProgramAccount> getProgramAccounts(PubKey pubKey, RPCEncoding encoding, DataSliceFilter dataSliceFilter, Filter... filters) throws RPCException {
        RPCResponse<List<ProgramAccount>> response = rpcClient.call(
                RPCMethod.GET_PROGRAM_ACCOUNTS, new TypeReference<>(){},
                pubKey, new ProgramAccountConfig(encoding, dataSliceFilter, List.of(filters))
        );
        checkError(response);

        return response.getResult();
    }

    public ContextResult<TokenBalance> getTokenSupply(PubKey pubKey) throws RPCException{
        RPCResponse<ContextResult<TokenBalance>> response = rpcClient.call(
                RPCMethod.GET_TOKEN_SUPPLY, new TypeReference<>(){},
                pubKey
        );
        checkError(response);

        return response.getResult();
    }

    public ContextResult<TokenBalance> getTokenAccountBalance(PubKey pubKey) throws RPCException{
        RPCResponse<ContextResult<TokenBalance>> response = rpcClient.call(
                RPCMethod.GET_TOKEN_ACCOUNT_BALANCE, new TypeReference<>(){},
                pubKey
        );
        checkError(response);

        return response.getResult();
    }

    public Subscription<ContextResult<AccountInfo>> subscribeAccount(PubKey pubKey, RPCEncoding encoding, NotificationListener<RPCNotification<ContextResult<AccountInfo>>> listener) throws RPCException {
        return webSocketClient.subscribe(
                RPCMethod.WEB_SOCKET_ACCOUNT_SUBSCRIBE, new TypeReference<>(){},
                listener, pubKey, new GeneralConfig(encoding)
        );
    }

    public void unSubscribeAccount(Subscription<?> subscription) throws RPCException {
        webSocketClient.unSubscribe(RPCMethod.WEB_SOCKET_ACCOUNT_UNSUBSCRIBE, subscription);
    }

    public Subscription<SlotInfo> subscribeSlot(NotificationListener<RPCNotification<SlotInfo>> listener) throws RPCException {
        return webSocketClient.subscribe(
                RPCMethod.WEB_SOCKET_SLOT_SUBSCRIBE, new TypeReference<>(){},
                listener
        );
    }

    public void unSubscribeSlot(Subscription<?> subscription) throws RPCException {
        webSocketClient.unSubscribe(RPCMethod.WEB_SOCKET_SLOT_UNSUBSCRIBE, subscription);
    }

    public Subscription<ContextResult<ProgramAccount>> subscribeProgram(PubKey pubKey, RPCEncoding encoding, NotificationListener<RPCNotification<ContextResult<ProgramAccount>>> listener) throws RPCException {
        return webSocketClient.subscribe(
                RPCMethod.WEB_SOCKET_PROGRAM_SUBSCRIBE, new TypeReference<>(){},
                listener, pubKey, new GeneralConfig(encoding)
        );
    }

    public void unSubscribeProgram(Subscription<?> subscription) throws RPCException {
        webSocketClient.unSubscribe(RPCMethod.WEB_SOCKET_PROGRAM_UNSUBSCRIBE, subscription);
    }
}
