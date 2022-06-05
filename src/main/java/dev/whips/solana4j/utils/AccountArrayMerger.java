package dev.whips.solana4j.utils;

import dev.whips.solana4j.transaction.Account;
import dev.whips.solana4j.utils.serialize.ByteSerializable;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.TreeSet;

public class AccountArrayMerger extends TreeSet<Account> implements ByteSerializable, ArrayCompactable {
    public AccountArrayMerger() {
        super(new AccountComparator());
    }

    @Override
    public int getSerializedLength() {
        int total = 0;
        for (ByteSerializable byteSerializable : this){
            total += byteSerializable.getSerializedLength();
        }
        return total;
    }

    @Override
    public byte[] serialize(){
        ByteBuffer buffer = ByteBuffer.allocate(getSerializedLength());
        buffer.put(getCompactLength());
        for (ByteSerializable byteSerializable : this){
            buffer.put(byteSerializable.serialize());
        }
        return buffer.array();
    }

    @Override
    public byte[] getCompactLength() {
        return DataUtils.getCompactLength(this.size());
    }

    private static class AccountComparator implements Comparator<Account> {
        @Override
        public int compare(Account a, Account b) {
            int compare = a.compareTo(b);
            if (compare == 0){
                a.mergeAccounts(b);
            }
            return compare;
        }
    }
}

