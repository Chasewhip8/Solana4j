package dev.whips.solana4j;

import java.net.MalformedURLException;
import java.net.URL;

public enum SolanaCluster {
    MAINNET_BETA("https://api.mainnet-beta.solana.com"),
    TESTNET("https://api.testnet.solana.com"),
    DEVNET("https://api.devnet.solana.com"),
    GENESYS_GO_MAINNET("https://ssc-dao.genesysgo.net/");

    URL endpoint;

    SolanaCluster(String url){
        try {
            this.endpoint = new URL(url);
        } catch (MalformedURLException e){
            throw new IllegalArgumentException("Invalid URL for cluster endpoint");
        }
    }

    public URL getEndpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return endpoint.toString();
    }
}
