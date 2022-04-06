package dev.whips.solana4j.client.exceptions;

public class ContractException extends RuntimeException{
    public ContractException(String message) {
        super(message);
    }
}
