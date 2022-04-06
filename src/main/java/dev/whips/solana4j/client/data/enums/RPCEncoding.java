package dev.whips.solana4j.client.data.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RPCEncoding {
    BASE58("base58"),
    BASE64("base64"),
    BASE64_COMPRESSED("base64+zstd"),
    JSON("jsonParsed");

    String encoding;

    RPCEncoding(String encoding){
        this.encoding = encoding;
    }

    @JsonValue
    @Override
    public String toString() {
        return encoding;
    }

    public static RPCEncoding getEncoding(String name){
        String lowerCased = name.toLowerCase();
        for (RPCEncoding encoding : RPCEncoding.values()){
            if (lowerCased.equals(encoding.encoding)){
                return encoding;
            }
        }
        return null;
    }
}
