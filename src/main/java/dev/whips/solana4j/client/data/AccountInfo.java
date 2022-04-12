package dev.whips.solana4j.client.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.exceptions.ContractException;
import dev.whips.solana4j.utils.DataReader;
import io.github.novacrypto.base58.Base58;

import java.util.List;

public class AccountInfo {
    private List<String> data;
    private boolean executable;
    private UnsignedLong lamports;
    private String owner;
    private UnsignedLong rentEpoch;

    public AccountInfo(List<String> data, boolean executable, UnsignedLong lamports, String owner, UnsignedLong rentEpoch) {
        this.data = data;
        this.executable = executable;
        this.lamports = lamports;
        this.owner = owner;
        this.rentEpoch = rentEpoch;
    }

    public AccountInfo() {

    }

    public List<String> getData() {
        return data;
    }

    public boolean isExecutable() {
        return executable;
    }

    public UnsignedLong getLamports() {
        return lamports;
    }

    public String getOwner() {
        return owner;
    }

    public UnsignedLong getRentEpoch() {
        return rentEpoch;
    }

    @JsonIgnore
    public DataReader getDataReader() throws ContractException {
        if (data.size() != 2){
            throw new ContractException("Contract data was missing or non existent");
        }

        byte[] rawData;
        RPCEncoding encoding = RPCEncoding.getEncoding(data.get(1));

        if (encoding == null){
            throw new ContractException("Contract data has invalid encoding");
        }

        switch (encoding){
            case BASE64 -> rawData = BaseEncoding.base64().decode(data.get(0));
            case BASE58 -> rawData = Base58.base58Decode(data.get(0));
            default -> throw new ContractException("Contract data has invalid encoding");
        }

        return new DataReader(rawData);
    }
}
