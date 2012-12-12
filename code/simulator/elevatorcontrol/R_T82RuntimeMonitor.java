/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file R_T82RuntimeMonitor.java
 */

package simulator.elevatorcontrol;
import jSimPack.SimTime;
import jSimPack.SimTime.SimTimeUnit;

import java.util.List;

import simulator.elevatorcontrol.Proj11RuntimeMonitor.DoorStateMachine;
import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.payloads.AtFloorPayload.ReadableAtFloorPayload;
import simulator.payloads.CarLanternPayload.ReadableCarLanternPayload;
import simulator.payloads.CarLightPayload.ReadableCarLightPayload;
import simulator.payloads.HallLightPayload.ReadableHallLightPayload;
public class R_T82RuntimeMonitor {
    //enumerate states
    private enum State {
        STATE_DOORS_CLOSED,
        STATE_DOORS_DELAY,
        STATE_DOORS_OPENED,
        STATE_LANTERNS_CHANGED
    };
    
    private State state = State.STATE_DOORS_CLOSED;
    boolean upLanternState;
    boolean downLanternState;
    private SimTime delay;
    
    public void onTimerExpired(ReadableAtFloorPayload[][] atFloors,
            DoorStateMachine doorState, ReadableHallLightPayload[][][] hallLights, 
            ReadableCarLightPayload[][] carLights, 
            ReadableCarLanternPayload[] carLanterns,
            SimTime runtimePeriod,
            List<String> warnings, List<String> messages) {
        
        State nextState = state;
        final boolean upLantern = carLanterns[Direction.UP.ordinal()].lighted();
        final boolean downLantern = carLanterns[Direction.DOWN.ordinal()].lighted();
        
        int f = 0;
        Hallway h = Hallway.NONE;
        for (int i = 0; i < Elevator.numFloors; i++) {
            for (Hallway h2 : Hallway.replicationValues) {
                if (atFloors[i][h2.ordinal()].getValue()) {
                    f = i;
                    h = h2;
                } else if (carLights[i][h2.ordinal()].lighted() ||
                        hallLights[i][h2.ordinal()][Direction.UP.ordinal()].lighted() ||
                        hallLights[i][h2.ordinal()][Direction.DOWN.ordinal()].lighted()) {
                }
            }
        }
        
        switch (state) {
            case STATE_DOORS_CLOSED:
            	delay = SimTime.multiply(MessageDictionary.LANTERN_CONTROL_PERIOD, 3);
                if (doorState.anyDoorOpen()) {
                    nextState = State.STATE_DOORS_DELAY;
                }
                break;
            case STATE_DOORS_DELAY:
            	delay = SimTime.subtract(delay, runtimePeriod);
            	if (delay.isLessThanOrEqual(SimTime.ZERO)) {
            		nextState = State.STATE_DOORS_OPENED;
            		upLanternState = upLantern;
            		downLanternState = downLantern;
            	}
            	break;
            case STATE_DOORS_OPENED:
                if (!doorState.anyDoorOpen()) {
                    nextState = State.STATE_DOORS_CLOSED;
                } else if (upLantern != upLanternState || downLantern != downLanternState) {
                    nextState = State.STATE_LANTERNS_CHANGED;
                }
                break;
            case STATE_LANTERNS_CHANGED:
                if (!doorState.anyDoorOpen()) {
                    nextState = State.STATE_DOORS_CLOSED;
                }
                break;
        }
        
        if (nextState != state && nextState == State.STATE_LANTERNS_CHANGED) {
            warnings.add("Warning RT 8.2: Lanterns changed when at floor " + (f+1) + " hallway " + h +
                    " from (" + upLanternState + "," + downLanternState + ") to (" +
                    upLantern + "," + downLantern + ") while doors were open");
        }
        
        state = nextState;
    }
}
