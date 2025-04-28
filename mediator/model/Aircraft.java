package mediator.model;

import mediator.mediator.TowerMediator;

public abstract class Aircraft {
    // unique aircraft identifier
    protected String id;
    // fuel level (0-100)
    protected int fuelLevel;
    // reference to mediator
    protected TowerMediator mediator;
    // vip status
    protected boolean vip;

    public Aircraft(String id, int fuelLevel, TowerMediator mediator) {
        this.id = id;
        this.fuelLevel = fuelLevel;
        this.mediator = mediator;
        this.vip = false;
    }


    public Aircraft(String id, int fuelLevel, TowerMediator mediator, boolean vip) {
        this.id = id;
        this.fuelLevel = fuelLevel;
        this.mediator = mediator;
        this.vip = vip;
    }

    // receives message from tower
    public abstract void receive(String str);

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

    public boolean isVip() {
        return vip;
    }
}