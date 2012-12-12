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

import simulator.elevatorcontrol.Proj11RuntimeMonitor.DoorStateMachine;
import simulator.framework.Hallway;
import simulator.framework.Side;
import simulator.payloads.DoorReversalPayload.ReadableDoorReversalPayload;

public class R_T10RuntimeMonitor {
    
    public R_T10RuntimeMonitor(Hallway h) {
        this.h = h;
    }
    
    //enumerate states
    private enum State {
        STATE_DOOR_CLOSED,
        STATE_NO_REVERSAL,
        STATE_REVERSAL,
        STATE_BAD_NUDGE
    };
    
    Hallway h;
    private State state = State.STATE_DOOR_CLOSED;
    
    public void onTimerExpired(DoorStateMachine doorState, 
            ReadableDoorReversalPayload[][] doorReversals, 
            List<String> warnings, List<String> messages) {
        
        State nextState = state;
        
        switch (state) {
            case STATE_DOOR_CLOSED:
                if (doorState.anyDoorOpen(h)) {
                    nextState = State.STATE_NO_REVERSAL;
                }
                break;
            case STATE_NO_REVERSAL:
                if (doorState.allDoorsClosed(h)) {
                    nextState = State.STATE_DOOR_CLOSED;
                } else if (doorReversals[h.ordinal()][Side.LEFT.ordinal()].isReversing() ||
                        doorReversals[h.ordinal()][Side.RIGHT.ordinal()].isReversing()) {
                    nextState = State.STATE_REVERSAL;
                } else if (doorState.anyDoorMotorNudging(h)) {
                    nextState = State.STATE_BAD_NUDGE;
                }
                break;
            case STATE_REVERSAL:
                if (doorState.allDoorsClosed(h)) {
                    nextState = State.STATE_DOOR_CLOSED;
                }
                break;
            case STATE_BAD_NUDGE:
                if (doorState.allDoorsClosed(h)) {
                    nextState = State.STATE_DOOR_CLOSED;
                }
                break;
            default:
                throw new RuntimeException("Unknown state in R_T10RuntimeMonitor");
        }
        
        if (state != nextState && nextState == State.STATE_BAD_NUDGE) {
            warnings.add("Warning RT 10: Doors in hallway " + h + " nudged without a reversal. YIKES");
        }
        
        state = nextState;
    }
}
