package mediator.mediator;

import mediator.model.Aircraft;

public interface TowerMediator {
    // sends message to all aircraft
    void broadcast(String str, Aircraft sender);
    // handles runway request for landing
    boolean requestRunway(Aircraft aircraft);
    // handles runway request with takeoff option
    boolean requestRunway(Aircraft aircraft, boolean isLanding);
}