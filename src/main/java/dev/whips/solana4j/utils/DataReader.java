package dev.whips.solana4j.utils;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.data.PubKey;

import java.math.BigInteger;
import java.util.Arrays;

public class DataReader {
    private static final int UINT_32_LENGTH = 4;
    private static final int UINT_64_LENGTH = 8;
    private static final int UINT_128_LENGTH = 16;
    private static final int PUB_KEY_LENGTH = 32;

    private final byte[] bytes;
    private int currentOffset;

    public DataReader(byte[] bytes) {
        this.bytes = bytes;
        this.currentOffset = 0;
    }

    public UnsignedLong readU32(){
        return UnsignedLong.fromLongBits(readRawLong(UINT_32_LENGTH));
    }

    public UnsignedLong readU64(){
        return UnsignedLong.fromLongBits(readRawLong(UINT_64_LENGTH));
    }

    public BigInteger readU128(){
        byte[] data = readRawBytes(UINT_128_LENGTH);
        byte[] rev = new byte[data.length + 1];
        for (int i = 0, j = data.length; j > 0; i++, j--)
            rev[j] = data[i];
        return new BigInteger(rev);
    }

    public PubKey readPubKey(){
        return new PubKey(readRawBytes(PUB_KEY_LENGTH));
    }

    public byte[] readBlob(int length){
        checkHasEnoughBytes(length);

        return readRawBytes(length);
    }

    public UnsignedLong[] readSequentialU64(int count){
        checkHasEnoughBytes(count * UINT_64_LENGTH);
        UnsignedLong[] data = new UnsignedLong[count];
        for (int x = 0; x < count; x++){
            data[x] = readU64();
        }
        return data;
    }

    public UnsignedLong[] readSequentialU32(int count){
        checkHasEnoughBytes(count * UINT_32_LENGTH);
        UnsignedLong[] data = new UnsignedLong[count];
        for (int x = 0; x < count; x++){
            data[x] = readU32();
        }
        return data;
    }

    public BigInteger[] readSequentialU128(int count){
        checkHasEnoughBytes(count * UINT_128_LENGTH);
        BigInteger[] data = new BigInteger[count];
        for (int x = 0; x < count; x++){
            data[x] = readU128();
        }
        return data;
    }

    public byte[] readRawBytes(int length){
        checkHasEnoughBytes(length);

        byte[] data = Arrays.copyOfRange(bytes, currentOffset, currentOffset + length);
        currentOffset += length;
        return data;
    }

    public long readRawLong(int length) {
        checkHasEnoughBytes(length);

        long val = 0;
        for (int i = (length - 1); i >= 0; i--) {
            val <<= 8;
            val |= (bytes [currentOffset + i] & 0x00FF);
        }
        currentOffset += length;
        return val;
    }

    public void skipBytes(int length){
        checkHasEnoughBytes(length);
        currentOffset += length;
    }

    public void checkHasEnoughBytes(int length){
        if (currentOffset + length > bytes.length){
            throw new DataReadException(bytes.length - currentOffset, length);
        }
    }

    public byte[] getRawBytes() {
        return bytes;
    }

    public int getRemainingBytes(){
        return bytes.length - currentOffset;
    }
}
