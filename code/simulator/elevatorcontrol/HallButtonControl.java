/**
* 18-648 Fall 2012
* Nick Mazurek (nmazurek)
* Jacob Olson (jholson)
* Ben Clark (brclark)
* Nipunn Koorapati (nkoorapa)
* @file HallButton.java
*/

package simulator.elevatorcontrol;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.elevatorcontrol.Utility.DoorClosedArray;
import simulator.framework.Controller;
import simulator.framework.Direction;
import simulator.framework.Hallway;
import simulator.framework.ReplicationComputer;
import simulator.payloads.CanMailbox;
import simulator.payloads.HallCallPayload;
import simulator.payloads.HallLightPayload;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.HallCallPayload.ReadableHallCallPayload;
import simulator.payloads.HallLightPayload.WriteableHallLightPayload;

public class HallButtonControl extends Controller {
    // Local physical state
    private ReadableHallCallPayload localHallCall;
    private WriteableHallLightPayload localHallLight;
    
    // Network interface
    // Receive at floor messages
    private AtFloorArray mAtFloors;
    
    // Receive desired floor messages
    private ReadableCanMailbox networkDesiredFloor;
    private DesiredFloorCanPayloadTranslator mDesiredFloor;
    
    // Receive door closed messages
    private DoorClosedArray mDoorClosed;
    
    // Send hall light messages
    private WriteableCanMailbox networkHallLight;
    private HallLightCanPayloadTranslator mHallLight;
    
    // Send hall call messages
    private WriteableCanMailbox networkHallCall;
    private HallCallCanPayloadTranslator mHallCall;
    
    // These variables keep track of which instance this is
    private final int floor;
    private final Hallway hallway;
    private final Direction direction;
    
    // Store the period for the controller
    private SimTime period;
    
    // Enumerate states
    private enum State {
        STATE_LIGHT_OFF,
        STATE_PRESSED,
    }
    
    // State variable initialized to the initial state STATE_LIGHT_OFF
    private State state = State.STATE_LIGHT_OFF;
    
    public HallButtonControl(int floor, Hallway hallway, Direction direction, SimTime period, boolean verbose) {
        // Make a call to the Controller superclass
        super("HallButtonControl" + ReplicationComputer.makeReplicationString(floor, hallway, direction), verbose);
        
        // Store the arguments in internal state
        this.period = period;
        this.floor = floor;
        this.hallway = hallway;
        this.direction = direction;
        
        // Initialize physical state
        // Create a hall call payload object and register it
        localHallCall = HallCallPayload.getReadablePayload(floor, hallway, direction);
        physicalInterface.registerTimeTriggered(localHallCall);
        // Create a hall light payload object and register it
        localHallLight = HallLightPayload.getWriteablePayload(floor, hallway, direction);
        physicalInterface.sendTimeTriggered(localHallLight, period);
        
        // Initialize network interfaces
        // Create mAtFloor interface
        mAtFloors = new AtFloorArray(canInterface);
        // Create mDesired interface and register it
        networkDesiredFloor = CanMailbox.getReadableCanMailbox(MessageDictionary.DESIRED_FLOOR_CAN_ID);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(networkDesiredFloor);
        canInterface.registerTimeTriggered(networkDesiredFloor);
        // Create mDoorClosed interface
        mDoorClosed = new DoorClosedArray(canInterface);
        // Create mHallLight interface and register it
        networkHallLight = CanMailbox.getWriteableCanMailbox(MessageDictionary.HALL_LIGHT_BASE_CAN_ID +
                ReplicationComputer.computeReplicationId(floor, hallway, direction));
        mHallLight = new HallLightCanPayloadTranslator(networkHallLight);
        canInterface.sendTimeTriggered(networkHallLight, period);
        // Create mHallCall interface and register it
        networkHallCall = CanMailbox.getWriteableCanMailbox(MessageDictionary.HALL_CALL_BASE_CAN_ID +
                ReplicationComputer.computeReplicationId(floor, hallway, direction));
        mHallCall = new HallCallCanPayloadTranslator(networkHallCall);
        canInterface.sendTimeTriggered(networkHallCall, period);
        
        // Start off the timer
        timer.start(period);
    }
    
    public void timerExpired(Object callbackData) {
        State newState = state;
        switch (state) {
            case STATE_LIGHT_OFF:
                // State actions for 'LIGHT OFF'
                localHallLight.set(false);
                mHallLight.set(false);
                mHallCall.set(false);
                
                // Transitions
                // #transition 'HBT1'
                if (localHallCall.pressed() == true) {
                    newState = State.STATE_PRESSED;
                } 
                break;
            case STATE_PRESSED:
                // State actions for 'PRESSED'
                localHallLight.set(true);
                mHallLight.set(true);
                mHallCall.set(true);
                
                // Transitions
                // #transition 'HBT2'
                if (localHallCall.pressed() == false &&
                        mDesiredFloor.getFloor() == mAtFloors.getCurrentFloor() &&
                        (mDesiredFloor.getHallway() == this.hallway ||
                         mDesiredFloor.getHallway() == Hallway.BOTH) &&
                        (mDesiredFloor.getDirection() == this.direction ||
                         mDesiredFloor.getDirection() == Direction.STOP) &&
                        mAtFloors.getCurrentFloor() == floor &&
                        !mDoorClosed.getBothClosed(this.hallway)) {
                    newState = State.STATE_LIGHT_OFF;
                } 
                break;
            default:
                throw new RuntimeException("State " + state + " was not recognized.");
        }
        
        // Log the state transition (if any)
        if (state == newState) {
            log("remains in state: ",state);
        } else {
            log("Transition: ",state,"->",newState);
        }
        
        // Update the state variable
        state = newState;
        
        // Report the current state
        setState(STATE_KEY, newState.toString());
        
        // Schedule the next controller iteration
        timer.start(period);
    }
}
