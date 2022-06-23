package dev.whips.solana4j;

import java.util.List;

public class ClusterRegistry {
    public static List<SolanaCluster> CLUSTERS = List.of(
            SolanaCluster.MAINNET_BETA,
            SolanaCluster.TESTNET,
            SolanaCluster.DEVNET,
            SolanaCluster.LOCALNET
    );

    public static SolanaCluster valueOf(String string){
        final String matchName = string.toString();
        for (SolanaCluster cluster : CLUSTERS){
            if (cluster.getName().equals(matchName)){
                return cluster;
            }
        }
        return null;
    }

    public static void registerCluster(SolanaCluster solanaCluster){
        final String name = solanaCluster.getName();
        for (SolanaCluster cluster : CLUSTERS){
            if (cluster.getName().equals(name)){
                throw new RuntimeException("Cannot add multiple clusters with the same identifier!");
            }
        }

        CLUSTERS.add(solanaCluster);
    }

    public static void unRegisterCluster(SolanaCluster solanaCluster){
        CLUSTERS.remove(solanaCluster);
    }
}
