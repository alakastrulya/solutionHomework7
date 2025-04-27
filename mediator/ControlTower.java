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
    // dashboard for gui
    private Dashboard dashboard;

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

    // handles runway request
    @Override
    public synchronized boolean requestRunway(Aircraft aircraft) {
        // log to console
        System.out.println(aircraft.getId() + " requests runway");
        // log to dashboard
        dashboard.addLogMessage(aircraft.getId() + " requests runway");

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
            dashboard.updateRunwayStatus("Occupied by " + currentRunwayUser.getId());
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
            dashboard.updateRunwayStatus("Occupied by " + currentRunwayUser.getId());
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
        // log to console
        System.out.println("emergency: " + aircraft.getId() + " declared mayday");
        // log to dashboard
        dashboard.addLogMessage("emergency: " + aircraft.getId() + " declared mayday");
        // clear landing queue
        landingQueue.clear();
        // clear takeoff queue
        takeoffQueue.clear();
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
            dashboard.updateRunwayStatus("Free");
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
        dashboard.updateRunwayStatus("Occupied by " + aircraft.getId());
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
                    // log to console
                    System.out.println(currentRunwayUser.getId() + " completed runway action");
                    // log to dashboard
                    dashboard.addLogMessage(currentRunwayUser.getId() + " completed runway action");
                    // clear runway
                    currentRunwayUser = null;
                    // update runway status
                    dashboard.updateRunwayStatus("Free");
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