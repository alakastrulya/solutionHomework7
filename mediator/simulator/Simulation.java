package mediator.simulator;

import mediator.mediator.ControlTower;
import mediator.mediator.TowerMediator;
import mediator.model.Aircraft;
import mediator.model.PassengerPlane;
import mediator.strategy.FifoScheduler;
import mediator.strategy.FuelPriorityScheduler;
import mediator.strategy.VipPriorityScheduler;
import mediator.ui.Dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Simulator
public class Simulation {
    public static void main(String[] args) {
        // create dashboard
        Dashboard dashboard = new Dashboard();
        // create tower with initial FIFO scheduler
        ControlTower tower = new ControlTower(dashboard, new FifoScheduler());
        // create random generator
        Random random = new Random();

        // load aircraft types using ServiceLoader
        ServiceLoader<Aircraft> aircraftLoader = ServiceLoader.load(Aircraft.class);
        List<Aircraft> aircraftList = new ArrayList<>();
        int aircraftCount = 10;
        for (int i = 0; i < aircraftCount; i++) {
            int fuelLevel = random.nextInt(100) + 1;
            String id = "AC" + (i + 1);
            // 20% chance of being VIP
            boolean isVip = random.nextInt(100) < 20;
            Aircraft aircraftPrototype = null;
            for (Aircraft prototype : aircraftLoader) {
                try {
                    aircraftPrototype = prototype.getClass()
                            .getConstructor(String.class, int.class, TowerMediator.class, boolean.class)
                            .newInstance(id, fuelLevel, tower, isVip);
                    break;
                } catch (Exception e) {
                    System.err.println("Failed to instantiate aircraft: " + e.getMessage());
                }
            }
            if (aircraftPrototype == null) {
                aircraftPrototype = new PassengerPlane(id, fuelLevel, tower, isVip);
            }
            aircraftList.add(aircraftPrototype);
        }
        Aircraft[] aircraft = aircraftList.toArray(new Aircraft[0]);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        // create request simulator
        RunwayRequestSimulator requestSimulator = new RunwayRequestSimulator(aircraft, random, tower);
        // schedule requests every second
        scheduler.scheduleAtFixedRate(requestSimulator, 0, 1, TimeUnit.SECONDS);

        // schedule strategy changes
        // switch to FuelPriorityScheduler after 7 seconds
        scheduler.schedule(() -> {
            tower.setScheduler(new FuelPriorityScheduler());
        }, 7, TimeUnit.SECONDS);

        // switch to VipPriorityScheduler after 14 seconds
        scheduler.schedule(() -> {
            tower.setScheduler(new VipPriorityScheduler());
        }, 14, TimeUnit.SECONDS);

        try {
            // run for 20 seconds
            Thread.sleep(20000);
            // shutdown scheduler
            scheduler.shutdown();
            // wait for termination
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
                System.out.println("Scheduler forcibly shutdown");
                dashboard.addLogMessage("Scheduler forcibly shutdown");
            }
            // notify tower that simulation has ended
            tower.endSimulation();
        } catch (InterruptedException e) {
            e.printStackTrace();
            scheduler.shutdownNow();
            tower.endSimulation();
        }
    }

    //Simulates runway requests by randomly selecting aircraft
    private static class RunwayRequestSimulator implements Runnable {
        // aircraft array
        private Aircraft[] aircraft;
        // random generator
        private Random random;
        // tower mediator
        private TowerMediator tower;


        public RunwayRequestSimulator(Aircraft[] aircraft, Random random, TowerMediator tower) {
            this.aircraft = aircraft;
            this.random = random;
            this.tower = tower;
        }

        //runs the simulation step, requesting runway access for random aircraft
        @Override
        public void run() {
            int index = random.nextInt(aircraft.length);
            Aircraft selected = aircraft[index];
            if (random.nextInt(100) < 5) {
                selected = new PassengerPlane("MAYDAY", 10, tower, false);
                selected.requestRunway(true);
            } else {
                boolean isLanding = random.nextBoolean();
                selected.requestRunway(isLanding);
            }
        }
    }
}