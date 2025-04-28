package mediator.mediator;
import mediator.ui.Dashboard;
import mediator.strategy.FifoScheduler;
import mediator.strategy.RunwayScheduler;
import mediator.model.Aircraft;

import java.util.LinkedList;
import java.util.Queue;

//The control tower that mediates communication between aircraft
//Uses a RunwayScheduler strategy to allocate runway access
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
    // strategy for runway allocation
    private RunwayScheduler scheduler;


    //Constructor for ControlTower
    //@param dashboard The dashboard for GUI updates
    public ControlTower(Dashboard dashboard) {
        this(dashboard, new FifoScheduler());
    }

    //Constructor for ControlTower with a specific scheduler
    //@param dashboard The dashboard for GUI updates
    //@param scheduler The runway scheduler strategy
    //
    public ControlTower(Dashboard dashboard, RunwayScheduler scheduler) {
        this.dashboard = dashboard;
        this.scheduler = scheduler;
    }

    //Sets a new runway scheduler strategy at runtime
    //@param scheduler The new scheduler to use
    public void setScheduler(RunwayScheduler scheduler) {
        this.scheduler = scheduler;
        // log scheduler change
        System.out.println("Runway scheduler changed to: " + scheduler.getClass().getSimpleName());
        dashboard.addLogMessage("Runway scheduler changed to: " + scheduler.getClass().getSimpleName());
    }

    // Broadcasts a message to all aircraft
    //@param msg The message to broadcast
    // @param sender The aircraft sending the message
    @Override
    public void broadcast(String msg, Aircraft sender) {
        System.out.println("tower broadcasts from " + sender.getId() + ": " + msg);
        dashboard.addLogMessage("tower broadcasts from " + sender.getId() + ": " + msg);
    }

    //Handles a runway request (default to landing)
    // @param aircraft The aircraft requesting the runway
    // @return True if the request is granted, false otherwise
    @Override
    public synchronized boolean requestRunway(Aircraft aircraft) {
        return requestRunway(aircraft, true);
    }

    //Handles a runway request with landing/takeoff option
    //@param aircraft The aircraft requesting the runway
    // @param isLanding True for landing, false for takeoff
    //@return True if the request is granted, false otherwise
    @Override
    public synchronized boolean requestRunway(Aircraft aircraft, boolean isLanding) {
        // if simulation has ended, reject new requests
        if (simulationEnded) {
            System.out.println("Simulation has ended, rejecting request from " + aircraft.getId());
            dashboard.addLogMessage("Simulation has ended, rejecting request from " + aircraft.getId());
            return false;
        }

        // log request
        System.out.println(aircraft.getId() + " requests runway (" + (isLanding ? "landing" : "takeoff") + ")");
        dashboard.addLogMessage(aircraft.getId() + " requests runway (" + (isLanding ? "landing" : "takeoff") + ")");

        // check for mayday
        if (aircraft.getId().equals("MAYDAY")) {
            clearRunwayForEmergency(aircraft);
            return true;
        }

        // check for low fuel (outside of scheduler to log prioritization)
        if (aircraft.isEmergency() && isLanding) {
            System.out.println(aircraft.getId() + " has low fuel, prioritizing");
            dashboard.addLogMessage(aircraft.getId() + " has low fuel, prioritizing");
            landingQueue.add(aircraft);
            dashboard.updateQueueLengths(landingQueue.size(), takeoffQueue.size());
            processQueues();
            return currentRunwayUser == aircraft;
        }

        // add to appropriate queue
        if (isLanding) {
            landingQueue.add(aircraft);
        } else {
            takeoffQueue.add(aircraft);
        }
        // update queue lengths
        dashboard.updateQueueLengths(landingQueue.size(), takeoffQueue.size());
        // process queues
        processQueues();
        // check if cleared
        return currentRunwayUser == aircraft;
    }

    //Processes the queues to allocate the runway to the next aircraft
    private void processQueues() {
        // update queue lengths
        dashboard.updateQueueLengths(landingQueue.size(), takeoffQueue.size());
        // runway busy, exit
        if (currentRunwayUser != null) {
            return;
        }

        // use scheduler to select the next aircraft
        currentRunwayUser = scheduler.selectNextAircraft(landingQueue, takeoffQueue);
        if (currentRunwayUser != null) {
            // determine if it was a landing or takeoff request
            boolean isLanding = landingQueue.contains(currentRunwayUser) || currentRunwayUser.isEmergency();
            // log clearance
            String action = isLanding ? "cleared to land" : "cleared for takeoff";
            System.out.println(currentRunwayUser.getId() + " " + action);
            dashboard.addLogMessage(currentRunwayUser.getId() + " " + action);
            // notify aircraft
            currentRunwayUser.receive(action);
            // update runway status
            dashboard.updateRunwayStatus("Occupied by " + currentRunwayUser.getId(), true);
        } else if (simulationEnded) {
            // if simulation has ended and no aircraft are left, close dashboard
            System.out.println("No aircraft left, closing dashboard");
            dashboard.addLogMessage("No aircraft left, closing dashboard");
            dashboard.updateSimulationStatus(false);
            dashboard.close();
        }

        // simulate runway usage
        if (currentRunwayUser != null) {
            RunwayUsageSimulator simulator = new RunwayUsageSimulator(currentRunwayUser);
            new Thread(simulator).start();
        }
    }

    //Clears the runway for an emergency landing (MAYDAY)
    //@param aircraft The aircraft declaring an emergency
    private void clearRunwayForEmergency(Aircraft aircraft) {
        System.out.println("emergency: " + aircraft.getId() + " declared mayday");
        dashboard.addLogMessage("emergency: " + aircraft.getId() + " declared mayday");
        // clear queues
        landingQueue.clear();
        takeoffQueue.clear();
        dashboard.updateQueueLengths(landingQueue.size(), takeoffQueue.size());
        // check runway user
        if (currentRunwayUser != null) {
            System.out.println(currentRunwayUser.getId() + " hold position");
            dashboard.addLogMessage(currentRunwayUser.getId() + " hold position");
            currentRunwayUser.receive("hold position due to emergency");
            currentRunwayUser = null;
            dashboard.updateRunwayStatus("Free", false);
        }
        // set emergency aircraft
        currentRunwayUser = aircraft;
        System.out.println(aircraft.getId() + " cleared for emergency landing");
        dashboard.addLogMessage(aircraft.getId() + " cleared for emergency landing");
        aircraft.receive("cleared for emergency landing");
        dashboard.updateRunwayStatus("Occupied by " + aircraft.getId(), true);
        // simulate runway usage
        RunwayUsageSimulator simulator = new RunwayUsageSimulator(aircraft);
        new Thread(simulator).start();
    }

    // Marks the simulation as ended
    public void endSimulation() {
        simulationEnded = true;
        dashboard.updateSimulationStatus(false);
        processQueues();
    }

    //Simulates runway usage by an aircraft (2 seconds)
    private class RunwayUsageSimulator implements Runnable {
        // aircraft using the runway
        private Aircraft runwayUser;

        // Constructor for RunwayUsageSimulator
        //@param runwayUser The aircraft using the runway
        public RunwayUsageSimulator(Aircraft runwayUser) {
            this.runwayUser = runwayUser;
        }

        //Simulates runway usage for 2 seconds
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                synchronized (ControlTower.this) {
                    if (runwayUser == currentRunwayUser) {
                        System.out.println(currentRunwayUser.getId() + " completed runway action");
                        dashboard.addLogMessage(currentRunwayUser.getId() + " completed runway action");
                        currentRunwayUser = null;
                        dashboard.updateRunwayStatus("Free", false);
                        processQueues();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("RunwayUsageSimulator interrupted for " + runwayUser.getId());
                dashboard.addLogMessage("RunwayUsageSimulator interrupted for " + runwayUser.getId());
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