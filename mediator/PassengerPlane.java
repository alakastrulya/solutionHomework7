package mediator;

public class PassengerPlane extends Aircraft {
    public PassengerPlane(String id, int fuelLevel, TowerMediator mediator) {
        super(id, fuelLevel, mediator);
    }

    @Override
    public void receive(String msg) {
        System.out.println("Passenger plane " + id + " received: " + msg);
    }
}