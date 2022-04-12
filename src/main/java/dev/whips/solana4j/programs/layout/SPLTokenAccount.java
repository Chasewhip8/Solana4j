package dev.whips.solana4j.programs.layout;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.utils.DataReader;

public class SPLTokenAccount {
    private final PubKey mint;
    private final PubKey owner;
    private final UnsignedLong amount;
    private final PubKey delegate;
    private final UnsignedLong accountState; /// TODO make enum representation of this
    private final UnsignedLong isNative;
    private final UnsignedLong delegatedAmount;
    private final PubKey closeAuthority;

    public SPLTokenAccount(DataReader dataReader){
        this.mint = dataReader.readPubKey();
        this.owner = dataReader.readPubKey();
        this.amount = dataReader.readU64();
        this.delegate = dataReader.readPubKey();
        this.accountState = dataReader.readU64();
        this.isNative = dataReader.readU64();
        this.delegatedAmount = dataReader.readU64();
        this.closeAuthority = dataReader.readPubKey();
    }

    public PubKey getMint() {
        return mint;
    }

    public PubKey getOwner() {
        return owner;
    }

    public UnsignedLong getAmount() {
        return amount;
    }

    public PubKey getDelegate() {
        return delegate;
    }

    public UnsignedLong getAccountState() {
        return accountState;
    }

    public UnsignedLong getIsNative() {
        return isNative;
    }

    public UnsignedLong getDelegatedAmount() {
        return delegatedAmount;
    }

    public PubKey getCloseAuthority() {
        return closeAuthority;
    }
}
