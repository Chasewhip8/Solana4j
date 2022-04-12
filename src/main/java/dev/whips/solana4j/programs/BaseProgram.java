package dev.whips.solana4j.programs;

import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.utils.DataReader;

public class BaseProgram {
    protected static void checkProgramSize(DataReader dataReader, int size) throws RPCException {
        if (dataReader.getRemainingBytes() < size){
            throw new RPCException("Invalid Contract Structure Size, required "
                    + size + ", only found " + dataReader.getRemainingBytes());
        }
    }
}
