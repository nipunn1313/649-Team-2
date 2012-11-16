/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file R_T81RuntimeMonitor.java
 */

package simulator.elevatorcontrol;
import java.util.List;

import simulator.elevatorcontrol.Proj11RuntimeMonitor.DoorStateMachine;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.payloads.AtFloorPayload.ReadableAtFloorPayload;
import simulator.payloads.CarLightPayload.ReadableCarLightPayload;
import simulator.payloads.HallLightPayload.ReadableHallLightPayload;
public class R_T81RuntimeMonitor {
    //enumerate states
    private enum State {
        STATE_LANTERN_OFF_NO_CALL,
        STATE_LANTERN_ON,
        STATE_LANTERN_OFF_PENDING_CALL
    };
    
    private State state = State.STATE_LANTERN_OFF_NO_CALL;
    
    boolean[] validCall = new boolean[Elevator.numFloors];
    
    public void onTimerExpired(ReadableAtFloorPayload[][] atFloors, DoorStateMachine doorState,
            ReadableHallLightPayload[][][] hallLights, ReadableCarLightPayload[][] carLights,
            List<String> warnings, List<String> messages) {
        
        State nextState = state;
        
        int f = 0;
        Hallway h = Hallway.NONE;
        for (int i = 0; i < Elevator.numFloors; i++) {
            for (Hallway h2 : Hallway.replicationValues) {
                if (atFloors[i][h2.ordinal()].getValue()) {
                    f = i;
                    h = h2;
                }
                if (carLights[i][h2.ordinal()].lighted()) validCall[i] = true;
                if (hallLights[i][h2.ordinal()][Hallway.FRONT.ordinal()].lighted()) validCall[i] = true;
                if (hallLights[i][h2.ordinal()][Hallway.BACK.ordinal()].lighted()) validCall[i] = true;
            }
        }
        
        switch (state) {
        }
        
        if (nextState != state && nextState == State.STATE_DOORS_NOT_CLOSED_NO_CALL) {
            warnings.add("Doors opened on floor " + f + " hallway " + h +
                    " but there was no call on that floor");
        }
        
        state = nextState;
    }
}
