package dev.whips.solana4j;

public class SolanaAPIBuilder {
    private SolanaCluster cluster;
    private int readTimeout;
    private int maxReconnectionRetries;
    private int reconnectionDelay;

    public SolanaAPIBuilder(){
        this.cluster = SolanaCluster.TESTNET;
        this.readTimeout = 15 * 1000; // 15 Seconds
        this.maxReconnectionRetries = 5;
        this.reconnectionDelay = 5000;
    }

    public SolanaAPIBuilder setCluster(SolanaCluster cluster) {
        this.cluster = cluster;
        return this;
    }

    public SolanaAPIBuilder setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public SolanaAPIBuilder setMaxReconnectionRetries(int maxReconnectionRetries){
        this.maxReconnectionRetries = maxReconnectionRetries;
        return this;
    }

    public SolanaAPIBuilder setReconnectionDelay(int reconnectionDelay){
        this.reconnectionDelay = reconnectionDelay;
        return this;
    }

    public SolanaAPI build(){
        return new SolanaAPI(
                cluster, readTimeout, maxReconnectionRetries, reconnectionDelay
        );
    }
}
