package dev.whips.solana4j.client.data.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.filters.DataSliceFilter;
import dev.whips.solana4j.client.data.filters.Filter;

import java.util.List;

public class ProgramAccountConfig {
    private RPCEncoding encoding;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DataSliceFilter dataSlice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Filter> filters;

    public ProgramAccountConfig(RPCEncoding encoding, DataSliceFilter dataSlice, List<Filter> filters) {
        this.encoding = encoding;
        this.dataSlice = dataSlice;
        this.filters = filters;
    }

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

    public DataSliceFilter getDataSlice() {
        return dataSlice;
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
