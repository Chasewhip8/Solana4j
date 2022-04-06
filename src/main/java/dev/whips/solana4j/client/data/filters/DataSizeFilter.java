package dev.whips.solana4j.client.data.filters;

import com.google.common.primitives.UnsignedLong;

public class DataSizeFilter implements Filter{
    private UnsignedLong dataSize;

    public DataSizeFilter(UnsignedLong dataSize) {
        this.dataSize = dataSize;
    }

    public DataSizeFilter() {

    }

    public UnsignedLong getDataSize() {
        return dataSize;
    }
}
