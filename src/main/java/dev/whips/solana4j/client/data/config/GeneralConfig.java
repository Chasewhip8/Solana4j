package dev.whips.solana4j.client.data.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.whips.solana4j.client.data.enums.RPCCommitment;
import dev.whips.solana4j.client.data.enums.RPCEncoding;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralConfig {
    private RPCEncoding encoding;
    private RPCCommitment commitment;

    public GeneralConfig(RPCEncoding encoding, RPCCommitment commitment) {
        this.encoding = encoding;
        this.commitment = commitment;
    }

    public GeneralConfig(RPCEncoding encoding) {
        this.encoding = encoding;
    }

    public GeneralConfig(RPCCommitment commitment) {
        this.commitment = commitment;
    }

    public GeneralConfig() {

    }

    public RPCEncoding getEncoding() {
        return encoding;
    }

    public RPCCommitment getCommitment() {
        return commitment;
    }
}
