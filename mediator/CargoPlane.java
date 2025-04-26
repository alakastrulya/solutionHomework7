package mediator;

public class CargoPlane extends Aircraft {
    public CargoPlane(String id, int fuelLevel, TowerMediator mediator) {
        super(id, fuelLevel, mediator);
    }

    @Override
    public void receive(String msg) {
        System.out.println("Cargo plane " + id + " received: " + msg);
    }
}