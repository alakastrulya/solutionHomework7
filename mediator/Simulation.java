package mediator;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;


public class Simulation {
    public static void main(String[] args) {
        // create tower
        ControlTower tower = new ControlTower();
        // create random generator
        Random random = new Random();

        // array for 10 aircraft
        Aircraft[] aircraft = new Aircraft[10];
        // spawn 10 random aircraft
        for (int i = 0; i < 10; i++) {
            // random fuel level (1-100)
            int fuelLevel = random.nextInt(100) + 1;
            // generate id
            String id = "AC" + (i + 1);
            // random aircraft type
            int type = random.nextInt(3);
            switch (type) {
                case 0:
                    // create passenger plane
                    aircraft[i] = new PassengerPlane(id, fuelLevel, tower);
                    break;
                case 1:
                    // create cargo plane
                    aircraft[i] = new CargoPlane(id, fuelLevel, tower);
                    break;
                case 2:
                    // create helicopter
                    aircraft[i] = new Helicopter(id, fuelLevel, tower);
                    break;
            }
        }

        // create scheduler
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // create request simulator
        RunwayRequestSimulator requestSimulator = new RunwayRequestSimulator(aircraft, random, tower);
        // schedule requests every second
        scheduler.scheduleAtFixedRate(requestSimulator, 0, 1, TimeUnit.SECONDS);

        try {
            // run for 20 seconds
            Thread.sleep(20000);
            // shutdown scheduler
            scheduler.shutdown();
            // wait for termination
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // log error
            e.printStackTrace();
        }
    }

    // simulates runway requests
    private static class RunwayRequestSimulator implements Runnable {
        // aircraft array
        private Aircraft[] aircraft;
        // random generator
        private Random random;
        // tower mediator
        private TowerMediator tower;

        // constructor
        public RunwayRequestSimulator(Aircraft[] aircraft, Random random, TowerMediator tower) {
            // set aircraft
            this.aircraft = aircraft;
            // set random
            this.random = random;
            // set tower
            this.tower = tower;
        }

        // run method
        @Override
        public void run() {
            // pick random aircraft
            int index = random.nextInt(10);
            Aircraft selected = aircraft[index];
            // 5% chance of mayday
            if (random.nextInt(100) < 5) {
                // simulate emergency
                selected = new PassengerPlane("MAYDAY", 10, tower);
            }
            // request runway
            selected.requestRunway();
        }
    }
}
