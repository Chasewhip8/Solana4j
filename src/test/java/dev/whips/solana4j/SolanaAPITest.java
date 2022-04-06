package dev.whips.solana4j;

import dev.whips.solana4j.data.PubKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SolanaAPITest {
    private SolanaAPI api;

    @BeforeAll
    public void initApi(){
        api = new SolanaAPIBuilder()
                .setCluster(SolanaCluster.MAINNET_BETA)
                .build();
    }

    @Test
    public void getAccountBalance_notNull(){

    }
}
