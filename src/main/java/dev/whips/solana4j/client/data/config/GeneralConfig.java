package dev.whips.solana4j.client.data.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.whips.solana4j.client.data.enums.RPCCommitment;
import dev.whips.solana4j.client.data.enums.RPCEncoding;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralConfig that = (GeneralConfig) o;
        return encoding == that.encoding && commitment == that.commitment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(encoding, commitment);
    }
}
