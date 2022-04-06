package dev.whips.solana4j.programs;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.data.PubKey;
import dev.whips.solana4j.utils.DataReader;

import java.math.BigInteger;

public class SerumOpenOrdersV2 {
    private final byte[] blob; // size 5

    private final UnsignedLong accountFlags; // u64

    private final PubKey market;
    private final PubKey owner;

    // These are in spl-token (i.e. not lot) units - u64
    private final UnsignedLong baseTokenFree;
    private final UnsignedLong baseTokenTotal;
    private final UnsignedLong quoteTokenFree;
    private final UnsignedLong quoteTokenTotal;

    // u128
    private final BigInteger freeSlotBits;
    private final BigInteger isBidBits;

    private final BigInteger[] orders; // 128 of u128
    private final UnsignedLong[] clientIds; // 128 of u64

    private final UnsignedLong referrerRebatesAccrued;

    private final byte[] blob2; // size 7

    public SerumOpenOrdersV2(DataReader dataReader) {
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
}
