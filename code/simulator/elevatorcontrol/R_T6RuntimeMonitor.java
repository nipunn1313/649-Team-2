/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file R_T6RuntimeMonitor.java
 */

package simulator.elevatorcontrol;

import java.util.List;

import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.payloads.AtFloorPayload.ReadableAtFloorPayload;
import simulator.payloads.CarLightPayload.ReadableCarLightPayload;
import simulator.payloads.DriveSpeedPayload.ReadableDriveSpeedPayload;
import simulator.payloads.HallLightPayload.ReadableHallLightPayload;

public class R_T6RuntimeMonitor {
    //enumerate states
    private enum State {
        STATE_CAR_IS_MOVING,
        STATE_STOP_WITH_CALL,
        STATE_STOP_NO_CALL
    };
    
    private State state = State.STATE_STOP_WITH_CALL;
    
    public void onTimerExpired(ReadableDriveSpeedPayload driveActualSpeed, 
            ReadableAtFloorPayload[][] atFloors,
            ReadableHallLightPayload[][][] hallLights,
            ReadableCarLightPayload[][] carLights, List<String> warnings, List<String> messages) {
        
        State nextState = state;
        
        int f = 0;
        for (int i = 0; i < Elevator.numFloors; i++)
            for (Hallway h : Hallway.replicationValues)
                if (atFloors[i][h.ordinal()].getValue())
                    f = i;
        
        switch (state) {
            case STATE_CAR_IS_MOVING:
                if (driveActualSpeed.speed() == 0.0) {
                    if (f != 0) {
                        boolean hasCall = false;
                        for (Hallway h2 : Hallway.replicationValues) {
                            if (carLights[f][h2.ordinal()].lighted()) hasCall = true;
                            for (Direction d : Direction.replicationValues) {
                                if (hallLights[f][h2.ordinal()][d.ordinal()].lighted()) hasCall = true;
                            }
                        }
                        if (hasCall) {
                            nextState = State.STATE_STOP_WITH_CALL;
                        } else {
                            nextState = State.STATE_STOP_NO_CALL;
                        }
                    }
                }
                break;
            case STATE_STOP_WITH_CALL:
                if (driveActualSpeed.speed() != 0)
                    nextState = State.STATE_CAR_IS_MOVING;
                break;
            case STATE_STOP_NO_CALL:
                if (driveActualSpeed.speed() != 0)
                    nextState = State.STATE_CAR_IS_MOVING;
                break;
            default:
                throw new RuntimeException("Unknown state in R_T6RuntimeMonitor");
        }
        
        if (state != nextState && nextState == State.STATE_STOP_NO_CALL) {
            warnings.add("Car is stopped on floor " + f +
                    " but no CarCall or HallCall is true!");
        }
        
        state = nextState;
    }
}
