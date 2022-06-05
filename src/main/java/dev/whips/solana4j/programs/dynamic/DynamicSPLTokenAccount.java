package dev.whips.solana4j.programs.dynamic;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.SolanaAPI;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.programs.DynamicProgram;
import dev.whips.solana4j.utils.DataReader;
import dev.whips.solana4j.utils.data.COption;
import dev.whips.solana4j.utils.serialize.ByteDeserializers;

import java.util.Arrays;

public class DynamicSPLTokenAccount extends DynamicProgram {
    private PubKey mint;
    private PubKey owner;
    private UnsignedLong amount;
    private COption<PubKey> delegate;
    private byte[] accountState; /// TODO make enum representation of this
    private COption<UnsignedLong> isNative;
    private UnsignedLong delegatedAmount;
    private COption<PubKey> closeAuthority;

    public DynamicSPLTokenAccount(SolanaAPI solanaAPI, PubKey pubKey){
        super(solanaAPI, pubKey);
    }

    @Override
    public int getExpectedDataLength() {
        return 165;
    }

    @Override
    public void updateProgramData(DataReader dataReader) {
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

    @Override
    public String toString() {
        return "DynamicSPLTokenAccount{" +
                "\nmint=" + mint +
                "\n, owner=" + owner +
                "\n, amount=" + amount +
                "\n, delegate=" + delegate +
                "\n, accountState=" + Arrays.toString(accountState) +
                "\n, isNative=" + isNative +
                "\n, delegatedAmount=" + delegatedAmount +
                "\n, closeAuthority=" + closeAuthority +
                '}';
    }
}
