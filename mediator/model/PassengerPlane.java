package mediator.model;

import mediator.mediator.TowerMediator;

public class PassengerPlane extends Aircraft {

    public PassengerPlane(String id, int fuelLevel, TowerMediator mediator, boolean vip) {
        super(id, fuelLevel, mediator, vip);
    }

    public PassengerPlane(String id, int fuelLevel, TowerMediator mediator) {
        super(id, fuelLevel, mediator);
    }

    @Override
    public void receive(String str) {
        System.out.println("Passenger plane " + id + " received: " + str);
    }
}