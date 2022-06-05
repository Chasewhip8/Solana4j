package dev.whips.solana4j.programs.dynamic;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.SolanaAPI;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.programs.DynamicProgram;
import dev.whips.solana4j.utils.DataReader;

import java.math.BigInteger;
import java.util.Arrays;

public class DynamicSerumOpenOrders extends DynamicProgram {
    private byte[] blob; // size 5

    private UnsignedLong accountFlags; // u64

    private PubKey market;
    private PubKey owner;

    // These are in spl-token (i.e. not lot) units - u64
    private UnsignedLong baseTokenFree;
    private UnsignedLong baseTokenTotal;
    private UnsignedLong quoteTokenFree;
    private UnsignedLong quoteTokenTotal;

    // u128
    private BigInteger freeSlotBits;
    private BigInteger isBidBits;

    private BigInteger[] orders; // 128 of u128
    private UnsignedLong[] clientIds; // 128 of u64

    private UnsignedLong referrerRebatesAccrued;

    private byte[] blob2; // size 7

    public DynamicSerumOpenOrders(SolanaAPI api, PubKey pubKey) {
        super(api, pubKey);
    }

    @Override
    public int getExpectedDataLength() {
        return 0;
    }

    @Override
    public void updateProgramData(DataReader dataReader) {
        this.blob = dataReader.readBlob(5);
        this.accountFlags = dataReader.readU64();
        this.market = dataReader.readPubKey();
        this.owner = dataReader.readPubKey();
        this.baseTokenFree = dataReader.readU64();
        this.baseTokenTotal = dataReader.readU64();
        this.quoteTokenFree = dataReader.readU64();
        this.quoteTokenTotal = dataReader.readU64();
        this.freeSlotBits = dataReader.readU128();
        this.isBidBits = dataReader.readU128();
        this.orders = dataReader.readSequentialU128(128);
        this.clientIds = dataReader.readSequentialU64(128);
        this.referrerRebatesAccrued = dataReader.readU64();
        this.blob2 = dataReader.readBlob(7);
    }

    public byte[] getBlob() {
        return blob;
    }

    public UnsignedLong getAccountFlags() {
        return accountFlags;
    }

    public PubKey getMarket() {
        return market;
    }

    public PubKey getOwner() {
        return owner;
    }

    public UnsignedLong getBaseTokenFree() {
        return baseTokenFree;
    }

    public UnsignedLong getBaseTokenTotal() {
        return baseTokenTotal;
    }

    public UnsignedLong getQuoteTokenFree() {
        return quoteTokenFree;
    }

    public UnsignedLong getQuoteTokenTotal() {
        return quoteTokenTotal;
    }

    public BigInteger getFreeSlotBits() {
        return freeSlotBits;
    }

    public BigInteger getIsBidBits() {
        return isBidBits;
    }

    public BigInteger[] getOrders() {
        return orders;
    }

    public UnsignedLong[] getClientIds() {
        return clientIds;
    }

    public UnsignedLong getReferrerRebatesAccrued() {
        return referrerRebatesAccrued;
    }

    public byte[] getBlob2() {
        return blob2;
    }

    @Override
    public String toString() {
        return "DynamicSerumOpenOrders{" +
                "\nblob=" + Arrays.toString(blob) +
                "\n, accountFlags=" + accountFlags +
                "\n, market=" + market +
                "\n, owner=" + owner +
                "\n, baseTokenFree=" + baseTokenFree +
                "\n, baseTokenTotal=" + baseTokenTotal +
                "\n, quoteTokenFree=" + quoteTokenFree +
                "\n, quoteTokenTotal=" + quoteTokenTotal +
                "\n, freeSlotBits=" + freeSlotBits +
                "\n, isBidBits=" + isBidBits +
                "\n, orders=" + Arrays.toString(orders) +
                "\n, clientIds=" + Arrays.toString(clientIds) +
                "\n, referrerRebatesAccrued=" + referrerRebatesAccrued +
                "\n, blob2=" + Arrays.toString(blob2) +
                '}';
    }
}
