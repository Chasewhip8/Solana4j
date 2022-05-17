package dev.whips.solana4j;

public class SolanaAPIBuilder {
    private SolanaCluster cluster;
    private int readTimeout;

    public SolanaAPIBuilder(){
        this.cluster = SolanaCluster.TESTNET;
        this.readTimeout = 15 * 1000; // 15 Seconds
    }

    public SolanaAPIBuilder setCluster(SolanaCluster cluster) {
        this.cluster = cluster;
        return this;
    }

    public SolanaAPIBuilder setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public SolanaAPI build(){
        return new SolanaAPI(
                cluster, readTimeout
        );
    }
}
