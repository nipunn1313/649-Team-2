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
        STATE_COMMIT_POINT_NOT_REACHED,
    };
    private static final int TIME = 50;
    private int timer = TIME;
    private boolean approachingFloor = false;
    
    private State state = State.STATE_STOPPED;
    
    public void onTimerExpired(ReadableDriveSpeedPayload driveActualSpeed, 
            ReadableCarLevelPositionPayload carLevelPosition,
            ReadableDrivePayload driveCommandedSpeed,
            DesiredFloorCanPayloadTranslator mDesiredFloor,
            List<String> warnings, List<String> messages) {
        
        State nextState = state;
        
        switch (state) {
            case STATE_CAN_GO_FASTER:
                if (driveActualSpeed.speed() == DriveObject.StopSpeed)
                    nextState = State.STATE_STOPPED;
                else if (driveActualSpeed.speed() > DriveObject.SlowSpeed)
                    nextState = State.STATE_MOVING;
                break;
            case STATE_STOPPED:
                approachingFloor = false;
                if (driveActualSpeed.speed() != DriveObject.StopSpeed) 
                    nextState = State.STATE_MOVING;
                break;
            case STATE_MOVING:
                timer = TIME;
                if (driveActualSpeed.speed() == DriveObject.StopSpeed)
                    nextState = State.STATE_STOPPED;
                else if (driveActualSpeed.speed() <= DriveObject.SlowSpeed && 
                    !Utility.reachedCommitPoint(mDesiredFloor.getFloor(),
                            carLevelPosition.position(),
                            driveActualSpeed.speed(),
                            driveCommandedSpeed.direction()) &&
                            approachingFloor == false)
                    nextState = State.STATE_COMMIT_POINT_NOT_REACHED;
                else
                    nextState = state;
                break;
            case STATE_COMMIT_POINT_NOT_REACHED:
                if (driveActualSpeed.speed() <= DriveObject.SlowSpeed && 
                        timer == 0)
                    nextState = State.STATE_CAN_GO_FASTER;
                else if ((driveActualSpeed.speed() > DriveObject.SlowSpeed &&
                        timer != 0) || 
                        driveActualSpeed.speed() <= DriveObject.SlowSpeed && 
                        Utility.reachedCommitPoint(mDesiredFloor.getFloor(), 
                                carLevelPosition.position(),
                                driveActualSpeed.speed(),
                                driveCommandedSpeed.direction())) {
                    approachingFloor = true;
                    nextState = State.STATE_MOVING;
                }
                else
                    timer--;
                break;  
            default:
                throw new RuntimeException("Unknown state in R_T6RuntimeMonitor");
        }
        
        if (state != nextState && nextState == State.STATE_CAN_GO_FASTER) {
            warnings.add("Car is driving at slow speed at position: " + carLevelPosition.position() + 
                    " but can drive at fast speed!");
        }
        state = nextState;
    }
}
