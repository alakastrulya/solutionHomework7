package mediator.strategy;

import mediator.model.Aircraft;

import java.util.Queue;
/**
 * interface for scheduling runway access
 * use for how to select the next aircraft from landing and takeoff queues
 */
public interface RunwayScheduler {
    Aircraft selectNextAircraft(Queue<Aircraft> landingQueue, Queue<Aircraft> takeoffQueue);
}
