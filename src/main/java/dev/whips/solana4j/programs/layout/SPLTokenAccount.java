package dev.whips.solana4j.programs.layout;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.programs.BaseProgram;
import dev.whips.solana4j.utils.DataReader;
import dev.whips.solana4j.utils.data.COption;
import dev.whips.solana4j.utils.serialize.ByteDeserializers;

public class SPLTokenAccount extends BaseProgram {
    private final PubKey mint;
    private final PubKey owner;
    private final UnsignedLong amount;
    private final COption<PubKey> delegate;
    private final byte[] accountState; /// TODO make enum representation of this
    private final COption<UnsignedLong> isNative;
    private final UnsignedLong delegatedAmount;
    private final COption<PubKey> closeAuthority;

    public SPLTokenAccount(DataReader dataReader){
        this.mint = dataReader.readPubKey();
        this.owner = dataReader.readPubKey();
        this.amount = dataReader.readU64();
        this.delegate = dataReader.readByteDeserializable(ByteDeserializers.C_OPTION_PUBKEY);
        this.accountState = dataReader.readBlob(1);
        this.isNative = dataReader.readByteDeserializable(ByteDeserializers.C_OPTION_U64);
        this.delegatedAmount = dataReader.readU64();
        this.closeAuthority = dataReader.readByteDeserializable(ByteDeserializers.C_OPTION_PUBKEY);
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

    public COption<PubKey> getDelegate() {
        return delegate;
    }

    public byte[] getAccountState() {
        return accountState;
    }

    public COption<UnsignedLong> getIsNative() {
        return isNative;
    }

    public UnsignedLong getDelegatedAmount() {
        return delegatedAmount;
    }

    public COption<PubKey> getCloseAuthority() {
        return closeAuthority;
    }
}
