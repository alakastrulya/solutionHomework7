package mediator.strategy;

import mediator.model.Aircraft;

import java.util.Queue;

public class FifoScheduler implements RunwayScheduler {
    //Selects the next aircraft to use the runway using FIFO ordering
    //@param landingQueue The queue of aircraft requesting landing
    //@param takeoffQueue The queue of aircraft requesting takeoff
    //@return The next aircraft to use the runway, or null if none available
    @Override
    public Aircraft selectNextAircraft(Queue<Aircraft> landingQueue, Queue<Aircraft> takeoffQueue) {
        if (!landingQueue.isEmpty()) {
            return landingQueue.poll();
        } else if (!takeoffQueue.isEmpty()) {
            return takeoffQueue.poll();
        }
        return null;
    }
}