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

import simulator.elevatormodules.DriveObject;
import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.payloads.AtFloorPayload.ReadableAtFloorPayload;
import simulator.payloads.CarCallPayload.ReadableCarCallPayload;
import simulator.payloads.CarLightPayload.ReadableCarLightPayload;
import simulator.payloads.DriveSpeedPayload.ReadableDriveSpeedPayload;
import simulator.payloads.HallCallPayload.ReadableHallCallPayload;
import simulator.payloads.HallLightPayload.ReadableHallLightPayload;

public class R_T6RuntimeMonitor {
    //enumerate states
    private enum State {
        STATE_CAR_IS_MOVING,
        STATE_STOP_WITH_CALL,
        STATE_STOP_NO_CALL
    };
    
    private State state = State.STATE_STOP_WITH_CALL;
    private ReadableCarLightPayload[][] prevCarLights = new ReadableCarLightPayload
            [Elevator.numFloors]
                    [Hallway.replicationValues.length];
    private ReadableHallLightPayload[][][] prevHallLights = new ReadableHallLightPayload
            [Elevator.numFloors]
            [Hallway.replicationValues.length]
            [Direction.replicationValues.length];
    
    public R_T6RuntimeMonitor(ReadableHallLightPayload[][][] prevHallLights,
            ReadableCarLightPayload[][] prevCarLights) {
        this.prevCarLights = prevCarLights;
        this.prevHallLights = prevHallLights;
    }
    
    public void onTimerExpired(ReadableDriveSpeedPayload driveActualSpeed, 
            ReadableAtFloorPayload[][] atFloors,
            ReadableHallLightPayload[][][] hallLights,
            ReadableCarLightPayload[][] carLights, List<String> warnings, List<String> messages) {
        
        State nextState = state;
        
        int f = -1;
        for (int i = 0; i < Elevator.numFloors; i++)
            for (Hallway h : Hallway.replicationValues)
                if (atFloors[i][h.ordinal()].getValue())
                    f = i;
        
        switch (state) {
            case STATE_CAR_IS_MOVING:
                if (driveActualSpeed.speed() < DriveObject.SlowSpeed) {
                    if (hasCall(f))
                        nextState = State.STATE_STOP_WITH_CALL;
                    else
                        nextState = State.STATE_STOP_NO_CALL;
                }
                else  {
                    prevCarLights = carLights;
                    prevHallLights = hallLights;
                    nextState = state; 
                }
                break;
            case STATE_STOP_WITH_CALL:
                if (driveActualSpeed.speed() >= DriveObject.SlowSpeed)
                    nextState = State.STATE_CAR_IS_MOVING;
                break;
            case STATE_STOP_NO_CALL:
                if (driveActualSpeed.speed() >= DriveObject.SlowSpeed)
                    nextState = State.STATE_CAR_IS_MOVING;
                break;
            default:
                throw new RuntimeException("Unknown state in R_T6RuntimeMonitor");
        }
        if (state != nextState && nextState == State.STATE_STOP_NO_CALL) {
            warnings.add("Car is stopped on floor " + (f+1) +
                    " but no CarCall or HallCall is true!");
        }
        
        state = nextState;
    }

    public boolean hasCall(int floor) {
        boolean carCall = false;
        if (floor == -1)
            return false;
        for (Hallway h : Hallway.replicationValues) {
            carCall = prevCarLights[floor][h.ordinal()].isLighted();
            for (Direction d : Direction.replicationValues)
                if ( carCall || prevHallLights[floor][h.ordinal()][d.ordinal()].lighted())
                    return true;
        }
        return false;
    }
}
