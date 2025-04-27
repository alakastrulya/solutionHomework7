package mediator;import java.util.LinkedList;
import java.util.Queue;

public class ControlTower implements TowerMediator {
    // queue for landing requests
    private Queue<Aircraft> landingQueue = new LinkedList<>();
    // queue for takeoff requests
    private Queue<Aircraft> takeoffQueue = new LinkedList<>();
    // current aircraft on runway
    private Aircraft currentRunwayUser = null;
    // dashboard for gui
    private Dashboard dashboard;
    // flag to indicate simulation has ended
    private boolean simulationEnded = false;

    // constructor
    public ControlTower(Dashboard dashboard) {
        // set dashboard
        this.dashboard = dashboard;
    }

    // broadcasts message
    @Override
    public void broadcast(String msg, Aircraft sender) {
        // log to console
        System.out.println("tower broadcasts from " + sender.getId() + ": " + msg);
        // log to dashboard
        dashboard.addLogMessage("tower broadcasts from " + sender.getId() + ": " + msg);
    }

    // handles runway request (default to landing)
    @Override
    public synchronized boolean requestRunway(Aircraft aircraft) {
        // default to landing
        return requestRunway(aircraft, true);
    }

    // handles runway request with landing/takeoff option
    @Override
    public synchronized boolean requestRunway(Aircraft aircraft, boolean isLanding) {
        // if simulation has ended, reject new requests
        if (simulationEnded) {
            // log to console
            System.out.println("Simulation has ended, rejecting request from " + aircraft.getId());
            // log to dashboard
            dashboard.addLogMessage("Simulation has ended, rejecting request from " + aircraft.getId());
            return false;
        }

        // log to console
        System.out.println(aircraft.getId() + " requests runway (" + (isLanding ? "landing" : "takeoff") + ")");
        // log to dashboard
        dashboard.addLogMessage(aircraft.getId() + " requests runway (" + (isLanding ? "landing" : "takeoff") + ")");

        // check for mayday
        if (aircraft.getId().equals("MAYDAY")) {
            // handle emergency
            clearRunwayForEmergency(aircraft);
            // grant access
            return true;
        }

        // check for low fuel
        if (aircraft.isEmergency()) {
            // log to console
            System.out.println(aircraft.getId() + " has low fuel, prioritizing");
            // log to dashboard
            dashboard.addLogMessage(aircraft.getId() + " has low fuel, prioritizing");
            // add to landing queue (low fuel always lands)
            landingQueue.add(aircraft);
            // update queue lengths
            dashboard.updateQueueLengths(landingQueue.size(), takeoffQueue.size());
            // process queues
            processQueues();
            // check if cleared
            return currentRunwayUser == aircraft;
        }

        // add to appropriate queue
        if (isLanding) {
            // add to landing queue
            landingQueue.add(aircraft);
        } else {
            // add to takeoff queue
            takeoffQueue.add(aircraft);
        }
        // update queue lengths
        dashboard.updateQueueLengths(landingQueue.size(), takeoffQueue.size());
        // process queues
        processQueues();
        // check if cleared
        return currentRunwayUser == aircraft;
    }

    // processes queues
    private void processQueues() {
        // update queue lengths
        dashboard.updateQueueLengths(landingQueue.size(), takeoffQueue.size());
        // runway busy, exit
        if (currentRunwayUser != null) {
            return;
        }

        // check landing queue
        if (!landingQueue.isEmpty()) {
            // get next aircraft
            currentRunwayUser = landingQueue.poll();
            // log to console
            System.out.println(currentRunwayUser.getId() + " cleared to land");
            // log to dashboard
            dashboard.addLogMessage(currentRunwayUser.getId() + " cleared to land");
            // notify aircraft
            currentRunwayUser.receive("cleared to land");
            // update runway status
            dashboard.updateRunwayStatus("Occupied by " + currentRunwayUser.getId(), true);
        } else if (!takeoffQueue.isEmpty()) {
            // get next aircraft
            currentRunwayUser = takeoffQueue.poll();
            // log to console
            System.out.println(currentRunwayUser.getId() + " cleared for takeoff");
            // log to dashboard
            dashboard.addLogMessage(currentRunwayUser.getId() + " cleared for takeoff");
            // notify aircraft
            currentRunwayUser.receive("cleared for takeoff");
            // update runway status
            dashboard.updateRunwayStatus("Occupied by " + currentRunwayUser.getId(), true);
        } else if (simulationEnded) {
            // if simulation has ended and no aircraft are left, close dashboard
            System.out.println("No aircraft left, closing dashboard");
            dashboard.addLogMessage("No aircraft left, closing dashboard");
            // update simulation status
            dashboard.updateSimulationStatus(false);
            // close dashboard
            dashboard.close();
        }

        // simulate runway usage
        if (currentRunwayUser != null) {
            // create simulator
            RunwayUsageSimulator simulator = new RunwayUsageSimulator(currentRunwayUser);
            // start thread
            new Thread(simulator).start();
        }
    }

    // clears runway for emergency
    private void clearRunwayForEmergency(Aircraft aircraft) {
        // log to console
        System.out.println("emergency: " + aircraft.getId() + " declared mayday");
        // log to dashboard
        dashboard.addLogMessage("emergency: " + aircraft.getId() + " declared mayday");
        // clear landing queue
        landingQueue.clear();
        // clear takeoff queue
        takeoffQueue.clear();
        // update queue lengths after clearing
        dashboard.updateQueueLengths(landingQueue.size(), takeoffQueue.size());
        // check runway user
        if (currentRunwayUser != null) {
            // log to console
            System.out.println(currentRunwayUser.getId() + " hold position");
            // log to dashboard
            dashboard.addLogMessage(currentRunwayUser.getId() + " hold position");
            // notify current user
            currentRunwayUser.receive("hold position due to emergency");
            // clear runway
            currentRunwayUser = null;
            // update runway status
            dashboard.updateRunwayStatus("Free", false);
        }
        // set emergency aircraft
        currentRunwayUser = aircraft;
        // log to console
        System.out.println(aircraft.getId() + " cleared for emergency landing");
        // log to dashboard
        dashboard.addLogMessage(aircraft.getId() + " cleared for emergency landing");
        // notify aircraft
        aircraft.receive("cleared for emergency landing");
        // update runway status
        dashboard.updateRunwayStatus("Occupied by " + aircraft.getId(), true);
        // simulate runway usage for MAYDAY
        RunwayUsageSimulator simulator = new RunwayUsageSimulator(aircraft);
        new Thread(simulator).start();
    }

    // marks simulation as ended
    public void endSimulation() {
        // set flag
        simulationEnded = true;
        // update simulation status
        dashboard.updateSimulationStatus(false);
        // process queues to check if we can close
        processQueues();
    }

    // simulates runway usage
    private class RunwayUsageSimulator implements Runnable {
        // aircraft using the runway
        private Aircraft runwayUser;

        // constructor
        public RunwayUsageSimulator(Aircraft runwayUser) {
            // set runway user
            this.runwayUser = runwayUser;
        }

        // run method
        @Override
        public void run() {
            try {
                // simulate 2 seconds
                Thread.sleep(2000);
                synchronized (ControlTower.this) {
                    // check if this aircraft is still the current user
                    if (runwayUser == currentRunwayUser) {
                        // log to console
                        System.out.println(currentRunwayUser.getId() + " completed runway action");
                        // log to dashboard
                        dashboard.addLogMessage(currentRunwayUser.getId() + " completed runway action");
                        // clear runway
                        currentRunwayUser = null;
                        // update runway status
                        dashboard.updateRunwayStatus("Free", false);
                        // process queues
                        processQueues();
                    }
                }
            } catch (InterruptedException e) {
                // log interruption
                System.out.println("RunwayUsageSimulator interrupted for " + runwayUser.getId());
                // log to dashboard
                dashboard.addLogMessage("RunwayUsageSimulator interrupted for " + runwayUser.getId());
                // ensure runway is cleared
                synchronized (ControlTower.this) {
                    if (runwayUser == currentRunwayUser) {
                        currentRunwayUser = null;
                        dashboard.updateRunwayStatus("Free", false);
                        processQueues();
                    }
                }
            }
        }
    }
}