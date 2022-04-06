package dev.whips.solana4j;

public class SolanaAPIBuilder {
    private SolanaCluster cluster;

    public SolanaAPIBuilder(){
        this.cluster = SolanaCluster.TESTNET;
    }

    public SolanaAPIBuilder setCluster(SolanaCluster cluster) {
        this.cluster = cluster;
        return this;
    }

    public SolanaAPI build(){
        return new SolanaAPI(
                cluster
        );
    }
}
