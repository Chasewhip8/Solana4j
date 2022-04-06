package dev.whips.solana4j.client.data.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.filters.Filter;

import java.util.List;

public class ProgramAccountConfig {
    private RPCEncoding encoding;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Filter> filters;

    public ProgramAccountConfig(List<Filter> filters) {
        this.filters = filters;
        this.encoding = null;
    }

    public ProgramAccountConfig(RPCEncoding encoding) {
        this.encoding = encoding;
        this.filters = null;
    }

    public ProgramAccountConfig() {

    }

    public RPCEncoding getEncoding() {
        return encoding;
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
