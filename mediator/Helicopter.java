package mediator;

public class Helicopter extends Aircraft {
    public Helicopter(String id, int fuelLevel, TowerMediator mediator) {
        super(id, fuelLevel, mediator);
    }

    @Override
    public void receive(String msg) {
        System.out.println("Helicopter " + id + " received: " + msg);
    }
}