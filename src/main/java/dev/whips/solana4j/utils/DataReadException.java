package dev.whips.solana4j.utils;

public class DataReadException extends RuntimeException{
    public DataReadException(int remaining, int needed) {
        super("Not enough bytes remaining to read data value, " + needed + " needed, " + remaining + " remaining");
    }
}
