package dev.whips.solana4j.programs;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.data.PubKey;
import dev.whips.solana4j.utils.DataReader;

public class SPLTokenAccount {
    private final PubKey mint;
    private final PubKey owner;
    private final UnsignedLong amount;

    public SPLTokenAccount(DataReader dataReader){
        this.mint = dataReader.readPubKey();
        this.owner = dataReader.readPubKey();
        this.amount = dataReader.readU64();
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
}
