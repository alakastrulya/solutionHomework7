package mediator.strategy;

import mediator.model.Aircraft;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class FuelPriorityScheduler implements RunwayScheduler {
    /**
     * Selects the next aircraft to use the runway, prioritizing low fuel.
     * @param landingQueue The queue of aircraft requesting landing.
     * @param takeoffQueue The queue of aircraft requesting takeoff.
     * @return The next aircraft to use the runway, or null if none available.
     */
    @Override
    public Aircraft selectNextAircraft(Queue<Aircraft> landingQueue, Queue<Aircraft> takeoffQueue) {
        // create a priority queue to sort by fuel level (ascending)
        PriorityQueue<Aircraft> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(Aircraft::getFuelLevel)
        );

        // add all aircraft from landing queue
        for (Aircraft aircraft : landingQueue) {
            priorityQueue.add(aircraft);
        }
        // add all aircraft from takeoff queue
        for (Aircraft aircraft : takeoffQueue) {
            priorityQueue.add(aircraft);
        }

        // get the aircraft with lowest fuel
        Aircraft selected = priorityQueue.poll();
        if (selected == null) {
            return null;
        }

        // remove the selected aircraft from the appropriate queue
        if (landingQueue.contains(selected)) {
            landingQueue.remove(selected);
            return selected;
        } else if (takeoffQueue.contains(selected)) {
            takeoffQueue.remove(selected);
            return selected;
        }
        return null;
    }
}