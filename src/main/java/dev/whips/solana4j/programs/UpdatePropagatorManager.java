package dev.whips.solana4j.programs;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class UpdatePropagatorManager {
    private final Set<UpdatePropagator<?>> propagators;

    public UpdatePropagatorManager(){
        this.propagators = new HashSet<>();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (UpdatePropagator<?> propagator : propagators){
                    propagator.checkWaitTimeAndPropagate();
                }
            }
        }, 0, 1);
    }

    public void registerPropagator(UpdatePropagator<?> propagator){
        propagators.add(propagator);
    }

    public void unRegisterPropagator(UpdatePropagator<?> propagator){
        propagators.remove(propagator);
    }
}
