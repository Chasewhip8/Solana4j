package dev.whips.solana4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.*;
import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.ProgramAccount;
import dev.whips.solana4j.client.data.config.GeneralConfig;
import dev.whips.solana4j.client.data.config.ProgramAccountConfig;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.filters.DataSliceFilter;
import dev.whips.solana4j.client.data.filters.Filter;
import dev.whips.solana4j.client.data.results.ContextResult;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.client.data.PubKey;

import java.util.List;

public class SolanaAPI {
    private final SolanaCluster cluster;
    private final RPCClient rpcClient;

    public SolanaAPI(SolanaCluster cluster, int readTimeout) {
        this.cluster = cluster;
        this.rpcClient = new RPCClient(cluster, readTimeout);
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
}
