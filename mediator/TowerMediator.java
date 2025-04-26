package mediator;

public interface TowerMediator {
    // sends message to all aircraft
    void broadcast(String msg, Aircraft sender);
    // requests runway access, returns true if cleared
    boolean requestRunway(Aircraft aircraft);
}