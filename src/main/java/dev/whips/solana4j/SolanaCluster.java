package dev.whips.solana4j;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public enum SolanaCluster {
    MAINNET_BETA("https://api.mainnet-beta.solana.com", ""),
    TESTNET("https://api.testnet.solana.com", ""),
    DEVNET("https://api.devnet.solana.com", ""),
    GENESYS_GO_MAINNET("https://ssc-dao.genesysgo.net/", "wss://ssc-dao.genesysgo.net/");

    URL endpoint;
    URI webSocketEndpoint;

    SolanaCluster(String url, String ws){
        try {
            this.endpoint = new URL(url);
            this.webSocketEndpoint = new URI(ws);
        } catch (MalformedURLException | URISyntaxException e){
            throw new IllegalArgumentException("Invalid URL for cluster endpoint");
        }
    }

    public URL getEndpoint() {
        return endpoint;
    }

    public URI getWebSocketEndpoint(){
        return webSocketEndpoint;
    }

    @Override
    public String toString() {
        return endpoint.toString();
    }
}
