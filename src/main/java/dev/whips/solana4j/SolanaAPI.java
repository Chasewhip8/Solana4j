package dev.whips.solana4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.*;
import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.ProgramAccount;
import dev.whips.solana4j.client.data.TokenSupply;
import dev.whips.solana4j.client.data.config.GeneralConfig;
import dev.whips.solana4j.client.data.config.ProgramAccountConfig;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.filters.DataSliceFilter;
import dev.whips.solana4j.client.data.filters.Filter;
import dev.whips.solana4j.client.data.results.ContextResult;
import dev.whips.solana4j.client.websocket.*;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.client.data.PubKey;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SolanaAPI {
    private final RPCClient rpcClient;
    private final SolanaWebSocketClient webSocketClient;

    public SolanaAPI(SolanaCluster cluster, int readTimeout) {
        this.rpcClient = new RPCClient(cluster, readTimeout);
        this.webSocketClient = new SolanaWebSocketClient(cluster);
    }

    private void checkError(RPCResponse<?> response) throws RPCException {
        if (response == null){
            throw new RPCException("Response was null!");
        }
        if (response.getError() != null){
            throw new RPCException(response.getError());
        }
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

    public ContextResult<TokenSupply> getTokenSupply(PubKey pubKey) throws RPCException{
        RPCResponse<ContextResult<TokenSupply>> response = rpcClient.call(
                RPCMethod.GET_TOKEN_SUPPLY, new TypeReference<>(){},
                pubKey
        );
        checkError(response);

        return response.getResult();
    }

    public Subsciption<AccountInfo> subscribeAccount(PubKey pubKey, RPCEncoding encoding, NotificationListener<RPCNotification<AccountInfo>> listener) throws RPCException {
        return webSocketClient.subscribe(
                RPCMethod.WEB_SOCKET_ACCOUNT_SUBSCRIBE, new TypeReference<>(){},
                listener, pubKey, new GeneralConfig(encoding)
        );
    }

    public void unSubscribeAccount(Subsciption<?> subscription) throws RPCException {
        webSocketClient.unSubscribe(RPCMethod.WEB_SOCKET_ACCOUNT_UNSUBSCRIBE, subscription);
    }
}
