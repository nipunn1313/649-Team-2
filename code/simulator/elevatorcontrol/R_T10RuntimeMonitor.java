/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file R_T10RuntimeMonitor.java
 */

package simulator.elevatorcontrol;

import java.util.List;

import simulator.elevatormodules.DriveObject;
import simulator.framework.DoorCommand;
import simulator.payloads.CarLevelPositionPayload.ReadableCarLevelPositionPayload;
import simulator.payloads.DoorMotorPayload.ReadableDoorMotorPayload;
import simulator.payloads.DoorReversalPayload.ReadableDoorReversalPayload;
import simulator.payloads.DrivePayload.ReadableDrivePayload;
import simulator.payloads.DriveSpeedPayload.ReadableDriveSpeedPayload;

public class R_T10RuntimeMonitor {
    //enumerate states
    private enum State {
        STATE_DOOR_CLOSED,
        STATE_DOOR_OPENING,
        STATE_DOOR_OPENED,
        STATE_DOOR_CLOSE,
        STATE_DOOR_REOPEN,
        STATE_DOOR_REOPENED,
        STATE_DOOR_NUDGE,
        STATE_INVALID_DOOR_NUDGE
    };
    
    private static final double FAST_SPEED = 1.0;
    private State state = State.STATE_DOOR_CLOSED;
    
    public void onTimerExpired(ReadableDoorMotorPayload[][] doorMotors, 
            ReadableDoorReversalPayload[][] doorReversals) {
        
        State nextState = state;
        
        switch (state) {
            case STATE_DOOR_CLOSED:
                for (int i = 0; i < doorMotors.length; i++)
                    for (int j = 0; j < doorMotors[0].length; j++)
                        if (doorMotors[i][j].command() == DoorCommand.OPEN)
                            nextState = State.STATE_DOOR_OPENING;
                break;
            case STATE_DOOR_OPENING:
                for (int i = 0; i < doorMotors.length; i++)
                    for (int j = 0; j < doorMotors[0].length; j++)
                        if (doorMotors[i][j].command() == DoorCommand.STOP)
                            nextState = State.STATE_DOOR_OPENED;
                break;
            case STATE_DOOR_OPENED:
                for (int i = 0; i < doorMotors.length; i++)
                    for (int j = 0; j < doorMotors[0].length; j++) {
                        if (doorMotors[i][j].command() == DoorCommand.NUDGE)
                            nextState = State.STATE_INVALID_DOOR_NUDGE;
                        else if (doorMotors[i][j].command() == DoorCommand.CLOSE)
                            nextState = State.STATE_DOOR_CLOSE;
                    }               
                break;
            case STATE_SHOULD_SLOW:
                if (driveActualSpeed.speed() == DriveObject.SlowSpeed &&
                        Utility.reachedCommitPoint(mDesiredFloor.getFloor(),
                                carLevelPosition.position(),
                                driveActualSpeed.speed(),
                                driveCommandedSpeed.direction()))
                    nextState = State.STATE_MOVING;
                break;
            default:
                throw new RuntimeException("Unknown state in R_T6RuntimeMonitor");
        }
        
        if (state != nextState && nextState == State.STATE_CAN_GO_FASTER) {
            warnings.add("Car is driving at slow speed at position: " + carLevelPosition.position() + 
                    " but can drive at fast speed!");
        }
        else if (state != nextState && nextState == State.STATE_SHOULD_SLOW)
            warnings.add("Car is driving at fast speed at position: "  + carLevelPosition.position() + 
                    " but the car has reached the commit point for desired floor: " + mDesiredFloor.getFloor());
        state = nextState;
    }
}
