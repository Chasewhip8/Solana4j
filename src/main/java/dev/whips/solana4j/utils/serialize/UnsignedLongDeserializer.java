package dev.whips.solana4j.utils.serialize;

import com.google.common.primitives.UnsignedLong;

public class UnsignedLongDeserializer implements ByteDeserializer<UnsignedLong> {
    private final int bits;

    public UnsignedLongDeserializer(int bits) {
        this.bits = bits;
    }

    private static long readRawLong(byte[] bytes, int length, int currentOffset) {
        long val = 0;
        for (int i = (length - 1); i >= 0; i--) {
            val <<= 8;
            val |= (bytes [currentOffset + i] & 0x00FF);
        }
        return val;
    }

    @Override
    public int requiredSize() {
        return bits / 8;
    }

    @Override
    public UnsignedLong decodeFromBytes(byte[] bytes, int offset) {
        return UnsignedLong.fromLongBits(readRawLong(bytes, requiredSize(), offset));
    }
}
