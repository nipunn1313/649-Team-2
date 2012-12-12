/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file R_T83RuntimeMonitor.java
 */

package simulator.elevatorcontrol;
import java.util.List;

import simulator.elevatorcontrol.Proj11RuntimeMonitor.DoorStateMachine;
import simulator.elevatormodules.DriveObject;
import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.payloads.AtFloorPayload.ReadableAtFloorPayload;
import simulator.payloads.CarLanternPayload.ReadableCarLanternPayload;
import simulator.payloads.CarLightPayload.ReadableCarLightPayload;
import simulator.payloads.DriveSpeedPayload.ReadableDriveSpeedPayload;
import simulator.payloads.HallLightPayload.ReadableHallLightPayload;
public class R_T83RuntimeMonitor {
    //enumerate states
    private enum State {
        STATE_DOOR_OPEN_LANTERN_OFF,
        STATE_STOPPED_AT_FLOOR,
        STATE_MOVE_TO_CORRECT_FLOOR,
        STATE_DOOR_OPEN_LANTERN_ON,
        STATE_READY_TO_MOVE,
        STATE_MOVE_TO_INCORRECT_FLOOR
    };
    
    private State state = State.STATE_STOPPED_AT_FLOOR;
    Direction direction;
    
    public void onTimerExpired(ReadableDriveSpeedPayload driveActualSpeed, 
            ReadableAtFloorPayload[][] atFloors,
            DoorStateMachine doorState, ReadableHallLightPayload[][][] hallLights, 
            ReadableCarLightPayload[][] carLights, 
            ReadableCarLanternPayload[] carLanterns, 
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
            case STATE_DOOR_OPEN_LANTERN_OFF:
                if (!doorState.anyDoorOpen()) {
                    nextState = State.STATE_STOPPED_AT_FLOOR;
                }
                break;
            case STATE_STOPPED_AT_FLOOR:
                if (doorState.anyDoorOpen()) {
                    if (!upLantern && !downLantern) {
                        nextState = State.STATE_DOOR_OPEN_LANTERN_OFF;
                    } else {
                        nextState = State.STATE_DOOR_OPEN_LANTERN_ON;
                        direction = upLantern ? Direction.UP : Direction.DOWN;
                    }
                } else if (driveActualSpeed.speed() > 0.0) {
                    nextState = State.STATE_MOVE_TO_CORRECT_FLOOR;
                }
                break;
            case STATE_MOVE_TO_CORRECT_FLOOR:
                if (driveActualSpeed.speed() == 0.0) {
                    nextState = State.STATE_STOPPED_AT_FLOOR;
                }
                break;
            case STATE_DOOR_OPEN_LANTERN_ON:
                if (!doorState.anyDoorOpen()) {
                    nextState = State.STATE_READY_TO_MOVE;
                }
                break;
            case STATE_READY_TO_MOVE:
                if (driveActualSpeed.speed() > DriveObject.LevelingSpeed) {
                    if (driveActualSpeed.direction() == direction) {
                        nextState = State.STATE_MOVE_TO_CORRECT_FLOOR;
                    } else {
                        nextState = State.STATE_MOVE_TO_INCORRECT_FLOOR;
                    }
                }
                break;
            case STATE_MOVE_TO_INCORRECT_FLOOR:
                if (driveActualSpeed.speed() == 0.0) {
                    nextState = State.STATE_STOPPED_AT_FLOOR;
                }
                break;
        }
        
        if (nextState != state && nextState == State.STATE_MOVE_TO_INCORRECT_FLOOR) {
            warnings.add("Warning RT 8.3: Car started moving from floor " + (f+1) + " hallway " + h +
                    " in direction " + driveActualSpeed.direction() + " when lantern indicated " +
                    "that it should go " + direction);
        }
        
        state = nextState;
    }
}
