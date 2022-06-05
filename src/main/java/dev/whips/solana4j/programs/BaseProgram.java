package dev.whips.solana4j.programs;

import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.utils.DataReader;

public abstract class BaseProgram {
    private final PubKey pubKey;

    public BaseProgram() {
        this.pubKey = null;
    }

    public BaseProgram(PubKey pubKey) {
        this.pubKey = pubKey;
    }

    protected static void checkProgramSize(DataReader dataReader, int size) throws RPCException {
        if (dataReader.getRemainingBytes() < size){
            throw new RPCException("Invalid Contract Structure Size, required "
                    + size + ", only found " + dataReader.getRemainingBytes());
        }
    }

    public PubKey getPubKey() {
        return pubKey;
    }

    public boolean isSamePubKey(PubKey pubKey){
        if (this.pubKey == null){
            return false;
        }
        return this.pubKey.equals(pubKey);
    }
}
