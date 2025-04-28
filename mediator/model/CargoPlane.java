package mediator.model;

import mediator.mediator.TowerMediator;

public class CargoPlane extends Aircraft {
    public CargoPlane(String id, int fuelLevel, TowerMediator mediator, boolean vip) {
        super(id, fuelLevel, mediator,vip);
    }
    public CargoPlane(String id, int fuelLevel, TowerMediator mediator) {
        super(id, fuelLevel, mediator);
    }

    @Override
    public void receive(String msg) {
        System.out.println("Cargo plane " + id + " received: " + msg);
    }

    public boolean isVip() {
        return vip;
    }
}