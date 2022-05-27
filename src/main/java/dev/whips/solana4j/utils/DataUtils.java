package dev.whips.solana4j.utils;

public class DataUtils {
    public static double toDecimal(long value, int decimals){
        return value * Math.pow(10, -decimals);
    }

    public static double toDecimal(String stringValue, int decimals){
        return Long.parseLong(stringValue) * Math.pow(10, -decimals);
    }

    public static byte[] getCompactLength(int length){
        if (length > 0x3fff){
            return new byte[] {
                    (byte) ((length & 0x7F) | 0x80), // Lowest byte is the first 7 bits of length with the highest bit set
                    (byte) (((length >> 7) & 0x7F) | 0x80), // Next byte is just the first 7 bits with the highest bit set
                    (byte) ((length >> 14) & 0x3) // Last bit is just the 15th and 16th bit packed into a byte
            };
        } else if (length > 0x7f){
            return new byte[] {
                    (byte) ((length & 0x7F) | 0x80), // Lowest byte is the first 7 bits of length with the highest bit set
                    (byte) ((length >> 7) & 0x7F) // Next byte is just the first 7 bits
            };
        } else {
            return new byte[] {
                    (byte) (length & 0x7F) // Lowest byte is just the first 7 bits of the length
            };
        }
    }
}
