package dev.whips.solana4j.utils;

public class NumberUtils {
    public static double toDecimal(long value, int decimals){
        return value * Math.pow(10, -decimals);
    }
}
