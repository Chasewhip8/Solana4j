package dev.whips.solana4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.*;
import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.config.GeneralConfig;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.results.ContextResult;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.client.data.PubKey;

public class SolanaAPI {
    private final SolanaCluster cluster;
    private final RPCClient rpcClient;

    public SolanaAPI(SolanaCluster cluster) {
        this.cluster = cluster;
        this.rpcClient = new RPCClient(cluster);
    }

    private void checkError(RPCResponse<?> response) throws RPCException {
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

    public void getBlock(){

    }
}
