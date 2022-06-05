package dev.whips.solana4j.programs.layout;

import dev.whips.solana4j.programs.BaseProgram;
import dev.whips.solana4j.utils.DataReader;

public class SysvarClock extends BaseProgram {
    private final long slot;
    private final long epoch_start_timestamp;
    private final long epoch;
    private final long leader_schedule_epoch;
    private final long unix_timestamp;

    public SysvarClock(DataReader dataReader){
        this.slot = dataReader.readRawLong(8);
        this.epoch_start_timestamp = dataReader.readRawLong(8);
        this.epoch = dataReader.readRawLong(8);
        this.leader_schedule_epoch = dataReader.readRawLong(8);
        this.unix_timestamp = dataReader.readRawLong(8);
    }

    public long getSlot() {
        return slot;
    }

    public long getEpoch_start_timestamp() {
        return epoch_start_timestamp;
    }

    public long getEpoch() {
        return epoch;
    }

    public long getLeader_schedule_epoch() {
        return leader_schedule_epoch;
    }

    public long getUnix_timestamp() {
        return unix_timestamp;
    }
}
