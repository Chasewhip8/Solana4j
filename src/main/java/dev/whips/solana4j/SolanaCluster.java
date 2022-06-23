package dev.whips.solana4j;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class SolanaCluster {
    public static final SolanaCluster MAINNET_BETA = new SolanaCluster("https://api.mainnet-beta.solana.com", "wss://api.mainnet-beta.solana.com");
    public static final SolanaCluster TESTNET = new SolanaCluster("https://api.testnet.solana.com", "wss://api.testnet.solana.com");
    public static final SolanaCluster DEVNET = new SolanaCluster("https://api.devnet.solana.com", "wss://api.devnet.solana.com");
    public static final SolanaCluster GENESYS_GO_MAINNET = new SolanaCluster("https://ssc-dao.genesysgo.net/", "wss://ssc-dao.genesysgo.net/");
    public static final SolanaCluster LOCALNET = new SolanaCluster("http://127.0.0.1:8899/", "wss://http://127.0.0.1:8899/");

    private final URL endpoint;
    private final URI webSocketEndpoint;

    public SolanaCluster(String url, String ws){
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
