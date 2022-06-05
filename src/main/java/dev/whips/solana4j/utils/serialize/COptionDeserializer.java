package dev.whips.solana4j.utils.serialize;

import dev.whips.solana4j.utils.data.COption;

public class COptionDeserializer<R> implements ByteDeserializer<COption<R>> {
    private final ByteDeserializer<R> deserializer;

    public COptionDeserializer(ByteDeserializer<R> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public int requiredSize() {
        return 4 + deserializer.requiredSize();
    }

    @Override
    public COption<R> decodeFromBytes(byte[] bytes, int offset) {
        return new COption<>(deserializer, bytes, offset);
    }
}
