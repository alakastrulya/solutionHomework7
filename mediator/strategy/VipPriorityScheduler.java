package mediator.strategy;

import mediator.model.Aircraft;

import java.util.PriorityQueue;
import java.util.Queue;

public class VipPriorityScheduler implements RunwayScheduler {
    @Override
    public Aircraft selectNextAircraft(Queue<Aircraft> landingQueue, Queue<Aircraft> takeoffQueue) {
        // create a priority queue to sort by VIP status (VIP first)
        PriorityQueue<Aircraft> priorityQueue = new PriorityQueue<>((a1, a2) -> {
            if (a1.isVip() && !a2.isVip()) return -1;
            if (!a1.isVip() && a2.isVip()) return 1;
            return 0; // FIFO for equal priority
        });

        // add all aircraft from landing queue
        for (Aircraft aircraft : landingQueue) {
            priorityQueue.add(aircraft);
        }
        // add all aircraft from takeoff queue
        for (Aircraft aircraft : takeoffQueue) {
            priorityQueue.add(aircraft);
        }

        // get the highest priority aircraft
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