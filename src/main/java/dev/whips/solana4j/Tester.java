package dev.whips.solana4j;

import dev.whips.solana4j.exceptions.ContractException;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.programs.layout.RaydiumAMMV4;

public class Tester {
    public static void main(String[] args) throws RPCException, ContractException {
        SolanaAPI solanaAPI = new SolanaAPIBuilder()
                .setCluster(SolanaCluster.MAINNET_BETA)
                .build();

        PubKey wallet = new PubKey("24y6Hi2nUCjAP7Lzxm1kqMjA2UfUMMosKkETxJeqMcWT");
        PubKey JFI_USDC_AMM_Market = new PubKey("8Skw2e6PeEvyoMGXsKAk4TLM86Qh29zRQYXzXEzxRm8Y");

        RaydiumAMMV4 raydiumAMMV4 = new RaydiumAMMV4(solanaAPI, JFI_USDC_AMM_Market);
        System.out.println(raydiumAMMV4.requestCurrentPrice());
    }
}
