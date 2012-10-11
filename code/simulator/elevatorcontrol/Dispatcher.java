/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file Dispatcher.java
 */

package simulator.elevatorcontrol;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.elevatorcontrol.Utility.CarCallArray;
import simulator.elevatorcontrol.Utility.DoorClosedArray;
import simulator.elevatorcontrol.Utility.HallCallArray;
import simulator.elevatormodules.CarWeightCanPayloadTranslator;
import simulator.framework.Controller;
import simulator.framework.Direction;
import simulator.framework.Hallway;
import simulator.framework.ReplicationComputer;
import simulator.payloads.CanMailbox;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;

public class Dispatcher extends Controller {
    // Network interface
    // Receive at floor messages
    private AtFloorArray mAtFloors;

    // Receive door closed messages (from both hallways)
    private DoorClosedArray mDoorClosedFront;
    private DoorClosedArray mDoorClosedBack;

    // Receive hall call messages
    private HallCallArray mHallCalls;

    // Receive car call messages
    private CarCallArray mCarCalls;

    // Receive car weight messages
    private ReadableCanMailbox networkCarWeight;
    private CarWeightCanPayloadTranslator mCarWeight;

    // Send desired floor messages
    private WriteableCanMailbox networkDesiredFloor;
    private DesiredFloorCanPayloadTranslator mDesiredFloor;

    // Send desired dwell messages (for both hallways)
    private WriteableCanMailbox networkDesiredDwellFront;
    private DesiredDwellCanPayloadTranslator mDesiredDwellFront;
    private WriteableCanMailbox networkDesiredDwellBack;
    private DesiredDwellCanPayloadTranslator mDesiredDwellBack;

    // Store the period for the controller
    private final SimTime period;

    // Store the number of floors
    private final int numFloors;

    // Enumerate states
    private enum State {
        STATE_DOORS_CLOSED,
        STATE_DOORS_OPEN_AT_FLOOR,
        STATE_DOORS_OPEN_BETWEEN_FLOORS
    }

    // State variable initialized to the initial state STATE_DOORS_CLOSED
    private State state = State.STATE_DOORS_CLOSED;

    // State variable holding the desired floor, initialized to 1
    private int target = 1;

    public Dispatcher(int numFloors, SimTime period, boolean verbose) {
        // Make a call to the Controller superclass
        super("Dispatcher", verbose);

        // Store the arguments in internal state
        this.period = period;
        this.numFloors = numFloors;

        // Initialize network interfaces
        // Create mAtFloor interface
        mAtFloors = new AtFloorArray(canInterface);
        
        // Create mDoorClosed interfaces
        mDoorClosedFront = new DoorClosedArray(Hallway.FRONT, canInterface);
        mDoorClosedBack = new DoorClosedArray(Hallway.BACK, canInterface);
        
        // Create mHallCall interface
        mHallCalls = new HallCallArray(canInterface);
        
        // Create mCarCall interface
        mCarCalls = new CarCallArray(canInterface);
        
        // Create mCarWeight interface
        networkCarWeight = CanMailbox.getReadableCanMailbox(MessageDictionary.CAR_WEIGHT_CAN_ID);
        mCarWeight = new CarWeightCanPayloadTranslator(networkCarWeight);
        canInterface.registerTimeTriggered(networkCarWeight);
        
        // Create mDesiredFloor interface
        networkDesiredFloor = CanMailbox.getWriteableCanMailbox(MessageDictionary.DESIRED_FLOOR_CAN_ID);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(networkDesiredFloor);
        canInterface.sendTimeTriggered(networkDesiredFloor, period);
        
        // Create mDesiredDwell interface
        networkDesiredDwellFront = CanMailbox.getWriteableCanMailbox(MessageDictionary.DESIRED_DWELL_BASE_CAN_ID +
                ReplicationComputer.computeReplicationId(Hallway.FRONT));
        mDesiredDwellFront = new DesiredDwellCanPayloadTranslator(networkDesiredDwellFront);
        canInterface.sendTimeTriggered(networkDesiredDwellFront, period);
        networkDesiredDwellBack = CanMailbox.getWriteableCanMailbox(MessageDictionary.DESIRED_DWELL_BASE_CAN_ID +
                ReplicationComputer.computeReplicationId(Hallway.BACK));
        mDesiredDwellBack = new DesiredDwellCanPayloadTranslator(networkDesiredDwellBack);
        canInterface.sendTimeTriggered(networkDesiredDwellBack, period);

        // Start off the timer
        timer.start(period);
    }

    public void timerExpired(Object callbackData) {
        State newState = state;
        switch (state) {
            case STATE_DOORS_CLOSED:
                // State actions for 'DOORS CLOSED'
                mDesiredFloor.set(target, Utility.getLandings(target), Direction.STOP);
                mDesiredDwellFront.set(5000);
                mDesiredDwellBack.set(5000);
                
                // Transitions
                // #transition 'DIST1'
                if (!(mDoorClosedBack.getBothClosed() && mDoorClosedFront.getBothClosed()) &&
                    mAtFloors.getCurrentFloor() == MessageDictionary.NONE) {
                    newState = State.STATE_DOORS_OPEN_BETWEEN_FLOORS;
                // #transition 'DIST3'
                } else if (!(mDoorClosedBack.getBothClosed() && mDoorClosedFront.getBothClosed()) &&
                           mAtFloors.getCurrentFloor() != MessageDictionary.NONE) {
                    newState = State.STATE_DOORS_OPEN_AT_FLOOR;
                } else {
                    newState = state;
                }
                break;
            case STATE_DOORS_OPEN_AT_FLOOR:
                // State actions for 'DOORS OPEN AT FLOOR'
                if (mAtFloors.getCurrentFloor() == MessageDictionary.NONE) {
                    target = 1;
                } else {
                    target = (mAtFloors.getCurrentFloor() % numFloors) + 1;
                }
                mDesiredFloor.set(target, Utility.getLandings(target), Direction.STOP);
                mDesiredDwellFront.set(5000);
                mDesiredDwellBack.set(5000);
                
                // Transitions
                // #transition 'DIST4'
                if (mDoorClosedBack.getBothClosed() && mDoorClosedFront.getBothClosed()) {
                    newState = State.STATE_DOORS_CLOSED;
                // #transition 'DIST5'
                } else if (!(mDoorClosedBack.getBothClosed() && mDoorClosedFront.getBothClosed()) &&
                           mAtFloors.getCurrentFloor() == MessageDictionary.NONE) {
                    newState = State.STATE_DOORS_OPEN_BETWEEN_FLOORS;
                } else {
                    newState = state;
                }
                break;
            case STATE_DOORS_OPEN_BETWEEN_FLOORS:
                // State actions for 'DOORS OPEN BETWEEN FLOORS'
                target = 1;
                mDesiredFloor.set(target, Hallway.NONE, Direction.STOP);
                mDesiredDwellFront.set(5000);
                mDesiredDwellBack.set(5000);
                
                // Transitions
                // #transition 'DIST2'
                if (mDoorClosedBack.getBothClosed() && mDoorClosedFront.getBothClosed()) {
                    newState = State.STATE_DOORS_CLOSED;
                // #transition 'DIST6'
                } else if (!(mDoorClosedBack.getBothClosed() && mDoorClosedFront.getBothClosed()) &&
                           mAtFloors.getCurrentFloor() != MessageDictionary.NONE) {
                    newState = State.STATE_DOORS_OPEN_AT_FLOOR;
                } else {
                    newState = state;
                }
                break;
            default:
                throw new RuntimeException("State " + state + " was not recognized.");
        }

        // Log the state transition (if any)
        if (state == newState) {
            log("remains in state: ", state);
        } else {
            log("Transition: ", state, "->", newState);
        }

        // Update the state variable
        state = newState;
        
        // Report the current state
        setState(STATE_KEY, newState.toString());
        setState("TARGET", Integer.toString(target));
        
        // Schedule the next controller iteration
        timer.start(period);
    }
}