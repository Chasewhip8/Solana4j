package dev.whips.solana4j.programs;

public interface ProgramUpdateListener {
    void programUpdate(long slot);
}
