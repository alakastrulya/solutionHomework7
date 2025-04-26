package mediator;

import java.util.LinkedList;
import java.util.Queue;

public class ControlTower implements TowerMediator {
    // queue for landing requests
    private Queue<Aircraft> landingQueue = new LinkedList<>();
    // queue for takeoff requests
    private Queue<Aircraft> takeoffQueue = new LinkedList<>();
    // current aircraft on runway
    private Aircraft currentRunwayUser = null;

    // broadcasts message
    @Override
    public void broadcast(String msg, Aircraft sender) {
        // log broadcast
        System.out.println("tower broadcasts from " + sender.getId() + ": " + msg);
    }

    // handles runway request
    @Override
    public synchronized boolean requestRunway(Aircraft aircraft) {
        // log request
        System.out.println(aircraft.getId() + " requests runway");

        // check for mayday
        if (aircraft.getId().equals("MAYDAY")) {
            // handle emergency
            clearRunwayForEmergency(aircraft);
            // grant access
            return true;
        }

        // check for low fuel
        if (aircraft.isEmergency()) {
            // log low fuel
            System.out.println(aircraft.getId() + " has low fuel, prioritizing");
            // add to landing queue
            landingQueue.add(aircraft);
            // process queues
            processQueues();
            // check if cleared
            return currentRunwayUser == aircraft;
        }

        // add to landing queue
        landingQueue.add(aircraft);
        // process queues
        processQueues();
        // check if cleared
        return currentRunwayUser == aircraft;
    }

    // processes queues
    private void processQueues() {
        // runway busy, exit
        if (currentRunwayUser != null) {
            return;
        }

        // check landing queue
        if (!landingQueue.isEmpty()) {
            // get next aircraft
            currentRunwayUser = landingQueue.poll();
            // log clearance
            System.out.println(currentRunwayUser.getId() + " cleared to land");
            // notify aircraft
            currentRunwayUser.receive("cleared to land");
        } else if (!takeoffQueue.isEmpty()) {
            // get next aircraft
            currentRunwayUser = takeoffQueue.poll();
            // log clearance
            System.out.println(currentRunwayUser.getId() + " cleared for takeoff");
            // notify aircraft
            currentRunwayUser.receive("cleared for takeoff");
        }

        // simulate runway usage
        if (currentRunwayUser != null) {
            // create simulator
            RunwayUsageSimulator simulator = new RunwayUsageSimulator();
            // start thread
            new Thread(simulator).start();
        }
    }

    // clears runway for emergency
    private void clearRunwayForEmergency(Aircraft aircraft) {
        // log emergency
        System.out.println("emergency: " + aircraft.getId() + " declared mayday");
        // clear landing queue
        landingQueue.clear();
        // clear takeoff queue
        takeoffQueue.clear();
        // check runway user
        if (currentRunwayUser != null) {
            // log hold
            System.out.println(currentRunwayUser.getId() + " hold position");
            // notify current user
            currentRunwayUser.receive("hold position due to emergency");
            // clear runway
            currentRunwayUser = null;
        }
        // set emergency aircraft
        currentRunwayUser = aircraft;
        // log clearance
        System.out.println(aircraft.getId() + " cleared for emergency landing");
        // notify aircraft
        aircraft.receive("cleared for emergency landing");
    }

    // simulates runway usage
    private class RunwayUsageSimulator implements Runnable {
        // run method
        @Override
        public void run() {
            try {
                // simulate 2 seconds
                Thread.sleep(2000);
                synchronized (ControlTower.this) {
                    // log completion
                    System.out.println(currentRunwayUser.getId() + " completed runway action");
                    // clear runway
                    currentRunwayUser = null;
                    // process queues
                    processQueues();
                }
            } catch (InterruptedException e) {
                // log error
                e.printStackTrace();
            }
        }
    }
}
