package dev.whips.solana4j;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

public class SolanaCluster {
    public static final SolanaCluster MAINNET_BETA = new SolanaCluster("mainnet-beta", "https://free.rpcpool.com/", "wss://free.rpcpool.com/");
    public static final SolanaCluster TESTNET = new SolanaCluster("testnet", "https://api.testnet.solana.com", "wss://api.testnet.solana.com");
    public static final SolanaCluster DEVNET = new SolanaCluster("devnet", "https://api.devnet.solana.com", "wss://api.devnet.solana.com");
    public static final SolanaCluster LOCALNET = new SolanaCluster("localnet", "http://127.0.0.1:8899/", "wss://http://127.0.0.1:8899/");

    private final String name;
    private final URL endpoint;
    private final URI webSocketEndpoint;

    public SolanaCluster(String name, String url, String ws){
        this.name = name;
        try {
            this.endpoint = new URL(url);
            this.webSocketEndpoint = new URI(ws);
        } catch (MalformedURLException | URISyntaxException e){
            throw new IllegalArgumentException("Invalid URL for cluster endpoint");
        }
    }

    public String getName() {
        return name;
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
