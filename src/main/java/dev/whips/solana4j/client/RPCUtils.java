package dev.whips.solana4j.client;

import java.util.Random;

public class RPCUtils {
    private final static Random random;

    static {
        random = new Random();
    }

    public static int generateUniqueId(){
        return Math.abs(random.nextInt());
    }
}
