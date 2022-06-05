package dev.whips.solana4j.utils.data;

import dev.whips.solana4j.utils.serialize.ByteDeserializer;

public class COption<T> {
    private final T data;

    public COption(ByteDeserializer<T> deserializer, byte[] data, int currentOffset){
        final int length = data.length - currentOffset;
        final int optionLength = length - 32;

        if (optionLength < deserializer.requiredSize()){
            this.data = null;
            return;
        }

        long flag = 0;
        for (int i = 31; i >= 0; i--) {
            flag <<= 8;
            flag |= (data[currentOffset + i] & 0x00FF);
        }

        this.data = flag == 1 ? deserializer.decodeFromBytes(data, 32) : null;
    }

    public T get() {
        return data;
    }

    public boolean isPresent(){
        return data != null;
    }

    @Override
    public String toString() {
        return "COption{" +
                "data=" + data +
                '}';
    }
}
