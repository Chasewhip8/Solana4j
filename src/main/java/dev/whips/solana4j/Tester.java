package dev.whips.solana4j;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.data.ProgramAccount;
import dev.whips.solana4j.client.data.config.ProgramAccountConfig;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.filters.DataSizeFilter;
import dev.whips.solana4j.client.data.filters.DataSliceFilter;
import dev.whips.solana4j.client.data.filters.MemcmpFilter;
import dev.whips.solana4j.exceptions.ContractException;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.programs.layout.RaydiumAMMV4;
import dev.whips.solana4j.utils.DataReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tester {
    public static void main(String[] args) throws Exception {
        SolanaAPI solanaAPI = new SolanaAPIBuilder()
                .setCluster(SolanaCluster.GENESYS_GO_MAINNET)
                .build();

        PubKey wallet = new PubKey("24y6Hi2nUCjAP7Lzxm1kqMjA2UfUMMosKkETxJeqMcWT");
        PubKey JFI_USDC_AMM_Market = new PubKey("8Skw2e6PeEvyoMGXsKAk4TLM86Qh29zRQYXzXEzxRm8Y");

        RaydiumAMMV4 raydiumAMMV4 = new RaydiumAMMV4(solanaAPI, JFI_USDC_AMM_Market);
        System.out.println(raydiumAMMV4.requestCurrentPrice());
        PubKey soceanJFIContract = new PubKey("ErdVK113JZZnBHnqFxrmsZ3c3RxVwCQ6xwgd1LMuLuFe");
        PubKey jungleJFIContract = new PubKey("GePFQaZKHcWE5vpxHfviQtH5jgxokSs51Y5Q4zgBiMDs");
        PubKey jungleJFILPContract = new PubKey("J2hGHwbkpj2SVo6Bs4X2Houy7n6oauydhbh9D6HpKBU4");
        PubKey jungleJFICALLContract = new PubKey("etCPUaMghWobVwxug5Vgxb6r3A7DEBdLKUywrnDrJeZ");

//        System.out.println(getUniqueHolders(solanaAPI,
//                soceanJFIContract, jungleJFIContract, jungleJFILPContract, jungleJFICALLContract
//        ));

    }

    private static int getUniqueHolders(SolanaAPI solanaAPI, PubKey... pubKeys) throws RPCException, ContractException {
        Set<PubKey> holders = new HashSet<>();
        for (PubKey key : pubKeys) {
            final PubKey tokenAccount = new PubKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA");
            List<ProgramAccount> accounts = solanaAPI.getProgramAccounts(tokenAccount, RPCEncoding.BASE64,
                    new DataSliceFilter(32, 32),
                    new DataSizeFilter(165),
                    new MemcmpFilter(0, key.getRawKey())
            );

            for (ProgramAccount programAccount : accounts){
                DataReader dataReader = programAccount.getAccount().getDataReader();
                PubKey owner = dataReader.readPubKey();
                holders.add(owner);
            }
        }
        return holders.size();
    }
}
