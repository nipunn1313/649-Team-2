/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file R_T9RuntimeMonitor.java
 */

package simulator.elevatorcontrol;

import java.util.List;

import simulator.elevatormodules.DriveObject;
import simulator.payloads.CarLevelPositionPayload.ReadableCarLevelPositionPayload;
import simulator.payloads.DrivePayload.ReadableDrivePayload;
import simulator.payloads.DriveSpeedPayload.ReadableDriveSpeedPayload;

public class R_T9RuntimeMonitor {
    //enumerate states
    private enum State {
        STATE_STOPPED,
        STATE_CAN_GO_FASTER,
        STATE_MOVING,
        STATE_SHOULD_SLOW
    };
    
    private static final double FAST_SPEED = DriveObject.FastSpeed;
    private State state = State.STATE_STOPPED;
    
    public void onTimerExpired(ReadableDriveSpeedPayload driveActualSpeed, 
            ReadableCarLevelPositionPayload carLevelPosition,
            ReadableDrivePayload driveCommandedSpeed,
            DesiredFloorCanPayloadTranslator mDesiredFloor,
            List<String> warnings, List<String> messages) {
        
        State nextState = state;
        
        switch (state) {
            case STATE_STOPPED:
                if (driveActualSpeed.speed() != DriveObject.FastSpeed) {
                    nextState = State.STATE_MOVING;
                }
                break;
            case STATE_CAN_GO_FASTER:
                if (driveActualSpeed.speed() == 0)
                    nextState = State.STATE_STOPPED;
                else if (driveActualSpeed.speed() >= FAST_SPEED && 
                        !Utility.reachedCommitPoint(mDesiredFloor.getFloor(),
                                carLevelPosition.position(),
                                driveActualSpeed.speed(),
                                driveCommandedSpeed.direction()))
                    nextState = State.STATE_MOVING;
                break;
            case STATE_MOVING:
                if (driveActualSpeed.speed() == DriveObject.SlowSpeed && 
                    !Utility.reachedCommitPoint(mDesiredFloor.getFloor(),
                            carLevelPosition.position(),
                            driveActualSpeed.speed(),
                            driveCommandedSpeed.direction()))
                    nextState = State.STATE_CAN_GO_FASTER;
                else if (driveActualSpeed.speed() == DriveObject.FastSpeed && 
                        Utility.reachedCommitPoint(mDesiredFloor.getFloor(),
                                carLevelPosition.position(),
                                driveActualSpeed.speed(),
                                driveCommandedSpeed.direction()))
                    nextState = State.STATE_SHOULD_SLOW;
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
