package dev.whips.solana4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SolanaAPITest {
    private static SolanaAPI api;

    @BeforeAll
    public static void initApi(){
        api = new SolanaAPIBuilder()
                .setCluster(SolanaCluster.MAINNET_BETA)
                .build();
    }

    @Test
    public void getAccountBalance_notNull(){

    }
}
