package dev.whips.solana4j.transaction;

import com.iwebpp.crypto.TweetNaclFast;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.utils.ByteSerializable;
import org.jetbrains.annotations.NotNull;

public class Account implements ByteSerializable, Comparable<Account> {
    private static final int ACCOUNT_LENGTH = 32;

    private final PubKey address;
    private boolean writeable;
    private TweetNaclFast.Signature.KeyPair keyPair;

    public Account(PubKey address, boolean writeable, TweetNaclFast.Signature.KeyPair keyPair) {
        this.address = address;
        this.writeable = writeable;
        this.keyPair = keyPair;
    }

    public PubKey getAddress() {
        return address;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public TweetNaclFast.Signature.KeyPair getKeyPair() {
        return keyPair;
    }

    public boolean requiresSignature(){
        return keyPair != null;
    }

    public void mergeAccounts(Account otherAccount){
        if (!this.equals(otherAccount)){
            return;
        }

        if (otherAccount.isWriteable()) {
            this.setWriteable(true);
        }
        if (otherAccount.getKeyPair() != null){
            this.setKeyPair(otherAccount.getKeyPair());
        }
    }

    private int compareWriteable(Account o){
        if (this.isWriteable()){
            if (o.isWriteable()){
                return 0;
            } else {
                return 1;
            }
        } else {
            if (o.isWriteable()){
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public int compareTo(@NotNull Account o) {
        if (this.requiresSignature() && !o.requiresSignature()){
            return 1;
        } else if (o.requiresSignature()){
            return -1;
        }
        return compareWriteable(o);
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    public void setKeyPair(TweetNaclFast.Signature.KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @Override
    public byte[] serialize() {
        return address.getRawKey();
    }

    @Override
    public int getSerializedLength() {
        return ACCOUNT_LENGTH;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account account){
            return this.getAddress().equals(account.getAddress());
        }
        return false;
    }
}
