package mediator;

public abstract class Aircraft {
    // unique aircraft identifier
    protected String id;
    // fuel level (0-100)
    protected int fuelLevel;
    // reference to mediator
    protected TowerMediator mediator;

    // constructor
    public Aircraft(String id, int fuelLevel, TowerMediator mediator) {
        // set id
        this.id = id;
        // set fuel level
        this.fuelLevel = fuelLevel;
        // set mediator
        this.mediator = mediator;
    }

    // receives message from tower
    public abstract void receive(String msg);

    // sends message via mediator
    public void send(String msg) {
        // broadcast message
        mediator.broadcast(msg, this);
    }

    // requests runway for landing
    public boolean requestRunway() {
        // request via mediator (default to landing)
        return mediator.requestRunway(this);
    }

    // requests runway for landing or takeoff
    public boolean requestRunway(boolean isLanding) {
        // request via mediator
        return mediator.requestRunway(this, isLanding);
    }

    // gets aircraft id
    public String getId() {
        // return id
        return id;
    }

    // gets fuel level
    public int getFuelLevel() {
        // return fuel level
        return fuelLevel;
    }

    // checks if aircraft has low fuel
    public boolean isEmergency() {
        // low fuel threshold
        return fuelLevel <= 20;
    }
}