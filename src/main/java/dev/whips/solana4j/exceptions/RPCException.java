package dev.whips.solana4j.exceptions;

import dev.whips.solana4j.client.data.SolanaError;

public class RPCException extends Exception{
    public RPCException(String message) {
        super(message);
    }

    public RPCException(SolanaError error) {
        super(error.getCode() + ": " + error.getMessage());
    }
}
