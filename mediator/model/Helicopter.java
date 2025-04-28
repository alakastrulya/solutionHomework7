package mediator.model;

import mediator.mediator.TowerMediator;

public class Helicopter extends Aircraft {
    public Helicopter(String id, int fuelLevel, TowerMediator mediator) {
        super(id, fuelLevel, mediator);
    }

    public Helicopter(String id, int fuelLevel, TowerMediator mediator, boolean vip) {
        super(id, fuelLevel, mediator, vip);
    }

    @Override
    public void receive(String msg) {
        System.out.println("Helicopter " + id + " received: " + msg);
    }
}