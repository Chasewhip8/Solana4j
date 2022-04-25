package dev.whips.solana4j.client.data.filters;

public class DataSizeFilter implements Filter{
    private int dataSize;

    public DataSizeFilter(int dataSize) {
        this.dataSize = dataSize;
    }

    public DataSizeFilter() {

    }

    public int getDataSize() {
        return dataSize;
    }
}
