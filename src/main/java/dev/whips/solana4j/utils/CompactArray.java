package dev.whips.solana4j.utils;

import dev.whips.solana4j.utils.serialize.ByteSerializable;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class CompactArray<T extends ByteSerializable> extends ArrayList<T> implements ByteSerializable, ArrayCompactable {
    public CompactArray(){
        super();
    }

    public CompactArray(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public byte[] getCompactLength(){
        return DataUtils.getCompactLength(size());
    }

    @Override
    public int getSerializedLength() {
        int total = 0;
        for (ByteSerializable byteSerializable : this){
            total += byteSerializable.getSerializedLength();
        }
        return total;
    }

    @Override
    public byte[] serialize(){
        ByteBuffer buffer = ByteBuffer.allocate(getSerializedLength());
        buffer.put(getCompactLength());
        for (ByteSerializable byteSerializable : this){
            buffer.put(byteSerializable.serialize());
        }
        return buffer.array();
    }
}
