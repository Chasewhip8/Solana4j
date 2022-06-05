package dev.whips.solana4j.utils.serialize;

public interface ByteDeserializer<T> {
    int requiredSize();

    T decodeFromBytes(byte[] bytes, int offset);
}
