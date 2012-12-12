/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file R_T81RuntimeMonitor.java
 */

package simulator.elevatorcontrol;
import jSimPack.SimTime;

import java.util.List;
import java.util.concurrent.Delayed;

import simulator.elevatorcontrol.Proj11RuntimeMonitor.DoorStateMachine;
import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.payloads.AtFloorPayload.ReadableAtFloorPayload;
import simulator.payloads.CarLanternPayload.ReadableCarLanternPayload;
import simulator.payloads.CarLightPayload.ReadableCarLightPayload;
import simulator.payloads.HallLightPayload.ReadableHallLightPayload;
public class R_T81RuntimeMonitor {
    //enumerate states
    private enum State {
        STATE_DOORS_CLOSED,
        STATE_DOORS_DELAY,
        STATE_LANTERN_ON,
        STATE_LANTERN_OFF_PENDING_CALL
    };
    
    private State state = State.STATE_DOORS_CLOSED;
    private SimTime delay;
    private boolean checkCallsOnOtherFloors = false;
    
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
        boolean callOnAnotherFloor = false;
        Hallway h = Hallway.NONE;
        for (int i = 0; i < Elevator.numFloors; i++) {
            for (Hallway h2 : Hallway.replicationValues) {
                if (atFloors[i][h2.ordinal()].getValue()) {
                    f = i;
                    h = h2;
                } else if (!checkCallsOnOtherFloors && 
                		(carLights[i][h2.ordinal()].lighted() ||
                		hallLights[i][h2.ordinal()][Direction.UP.ordinal()].lighted() ||
                		hallLights[i][h2.ordinal()][Direction.DOWN.ordinal()].lighted())) {
                    callOnAnotherFloor = true;
                }
            }
        }
        
        switch (state) {
            case STATE_DOORS_CLOSED:
            	delay = SimTime.multiply(MessageDictionary.LANTERN_CONTROL_PERIOD, 3);
            	checkCallsOnOtherFloors = false;
                if (doorState.anyDoorOpen()) {
                	nextState = State.STATE_DOORS_DELAY;
                	checkCallsOnOtherFloors = true;
                }
                break;
            case STATE_DOORS_DELAY:
            	delay = SimTime.subtract(delay, runtimePeriod);
            	if (delay.isLessThanOrEqual(SimTime.ZERO)) {
            		if (upLantern || downLantern) {
            			nextState = State.STATE_LANTERN_ON;
            		} else if (callOnAnotherFloor) {
            			nextState = State.STATE_LANTERN_OFF_PENDING_CALL;
            		}
            	}
                break;
            case STATE_LANTERN_ON:
                if (!doorState.anyDoorOpen())
                    nextState = State.STATE_DOORS_CLOSED;
                break;
            case STATE_LANTERN_OFF_PENDING_CALL:
                if (!doorState.anyDoorOpen())
                    nextState = State.STATE_DOORS_CLOSED;
                break;
        }
        
        if (nextState != state && nextState == State.STATE_LANTERN_OFF_PENDING_CALL) {
            warnings.add("RT 8-1 Lanterns are off when doors are open at floor " + (f+1) + " hallway " + h +
                    " but there are pending calls");
        }
        
        state = nextState;
    }
}
