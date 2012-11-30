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
import simulator.elevatorcontrol.Utility.DesiredFloor;
import simulator.elevatorcontrol.Utility.DoorClosedArray;
import simulator.elevatorcontrol.Utility.HallCallArray;
import simulator.elevatormodules.DriveObject;
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
    private DoorClosedArray mDoorClosed;

    // Receive hall call messages
    private HallCallArray mHallCalls;

    // Receive car call messages
    private CarCallArray mCarCalls;

    // Receive car weight messages
    private ReadableCanMailbox networkCarWeight;
    //private CarWeightCanPayloadTranslator mCarWeight;
    
    // Receive car level position
    private ReadableCanMailbox networkCarLevelPosition;
    private OurCarLevelPositionCanPayloadTranslator mCarLevelPosition;

    // Receive drive speed
    private ReadableCanMailbox networkDriveSpeed;
    private DriveSpeedCanPayloadTranslator mDriveSpeed;
    
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
    
    // Store dwell time
    public static final int DWELL_TIME = 5000;

    // Store the number of floors
    //private final int numFloors;

    // Enumerate states
    private enum State {
        STATE_DOORS_CLOSED,
        STATE_DOORS_OPEN_AT_FLOOR,
        STATE_MOVING,
        STATE_APPROACHING,
        STATE_DOORS_OPEN_BETWEEN_FLOORS
    }

    // State variable initialized to the initial state STATE_DOORS_CLOSED
    private State state = State.STATE_DOORS_CLOSED;

    // State variable holding the desired floor, initialized to 1
    private int TargetFloor = 1;
    private Direction TargetDirection = Direction.STOP;
    private Hallway TargetHallway = Hallway.NONE;
    
    public Dispatcher(int numFloors, SimTime period, boolean verbose) {
        // Make a call to the Controller superclass
        super("Dispatcher", verbose);

        // Store the arguments in internal state
        this.period = period;
        //this.numFloors = numFloors;

        // Initialize network interfaces
        // Create mAtFloor interface
        mAtFloors = new AtFloorArray(canInterface);
        
        // Create mDoorClosed interfaces
        mDoorClosed = new DoorClosedArray(canInterface);
        
        // Create mHallCall interface
        mHallCalls = new HallCallArray(canInterface);
        
        // Create mCarCall interface
        mCarCalls = new CarCallArray(canInterface);
        
        // Create mCarWeight interface
        networkCarWeight = CanMailbox.getReadableCanMailbox(MessageDictionary.CAR_WEIGHT_CAN_ID);
        //mCarWeight = new CarWeightCanPayloadTranslator(networkCarWeight);
        canInterface.registerTimeTriggered(networkCarWeight);
        
        // Create mCarLevelPosition interface
        networkCarLevelPosition = CanMailbox.getReadableCanMailbox(MessageDictionary.CAR_LEVEL_POSITION_CAN_ID);
        mCarLevelPosition = new OurCarLevelPositionCanPayloadTranslator(networkCarLevelPosition);
        canInterface.registerTimeTriggered(networkCarLevelPosition);

        // Create mDriveSpeed interface
        networkDriveSpeed = CanMailbox.getReadableCanMailbox(MessageDictionary.DRIVE_SPEED_CAN_ID);
        mDriveSpeed = new DriveSpeedCanPayloadTranslator(networkDriveSpeed);
        canInterface.registerTimeTriggered(networkDriveSpeed);
        
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
        DesiredFloor nextFloor;
        int currentFloor = mAtFloors.getCurrentFloor();
        switch (state) {
            case STATE_DOORS_CLOSED:
                // State actions for 'DOORS CLOSED'
                nextFloor = Utility.getNextFloorDoorsClosed(mCarLevelPosition.getPosition(), 
                        mDriveSpeed.getSpeed(), mDriveSpeed.getDirection(), mCarCalls,
                        mHallCalls, currentFloor, TargetFloor);
                TargetFloor = nextFloor.getFloor();
                TargetHallway = nextFloor.getHallway();
                TargetDirection = nextFloor.getDirection();
                
                mDesiredFloor.set(TargetFloor, TargetHallway, TargetDirection);
                mDesiredDwellFront.set(DWELL_TIME);
                mDesiredDwellBack.set(DWELL_TIME);
                
                // Transitions
                // #transition 'DIST1'
                if (!mDoorClosed.getAllClosed() &&
                    mAtFloors.getCurrentFloor() == MessageDictionary.NONE) {
                    newState = State.STATE_DOORS_OPEN_BETWEEN_FLOORS;
                } else if (mAtFloors.getCurrentFloor() != TargetFloor &&
                           mAtFloors.getCurrentFloor() != MessageDictionary.NONE &&
                           mDriveSpeed.getSpeed() > DriveObject.LevelingSpeed) {
                    newState = State.STATE_MOVING;
                } else if (mAtFloors.getCurrentFloor() == TargetFloor &&
                           TargetHallway != Hallway.NONE) {
                    newState = State.STATE_APPROACHING;
                } else {
                    newState = state;
                }
                break;
            case STATE_DOORS_OPEN_AT_FLOOR:
                // State actions for 'DOORS OPEN AT FLOOR'
                nextFloor = Utility.getNextFloorDoorsOpen(mCarLevelPosition.getPosition(), 
                        mDriveSpeed.getSpeed(), mDriveSpeed.getDirection(), mCarCalls,
                        mHallCalls, TargetDirection, currentFloor);
                TargetFloor = nextFloor.getFloor();
                TargetHallway = nextFloor.getHallway();

                mDesiredFloor.set(TargetFloor, TargetHallway, TargetDirection);
                mDesiredDwellFront.set(DWELL_TIME);
                mDesiredDwellBack.set(DWELL_TIME);
                
                // Transitions
                // #transition 'DIST4'
                if (mDoorClosed.getAllClosed()) {
                    newState = State.STATE_DOORS_CLOSED;
                // #transition 'DIST5'
                } else if (!mDoorClosed.getAllClosed() &&
                           mAtFloors.getCurrentFloor() == MessageDictionary.NONE) {
                    newState = State.STATE_DOORS_OPEN_BETWEEN_FLOORS;
                } else {
                    newState = state;
                }
                break;
            case STATE_DOORS_OPEN_BETWEEN_FLOORS:
                // State actions for 'DOORS OPEN BETWEEN FLOORS'
                TargetFloor = 1;
                mDesiredFloor.set(TargetFloor, Hallway.NONE, Direction.STOP);
                mDesiredDwellFront.set(DWELL_TIME);
                mDesiredDwellBack.set(DWELL_TIME);
                
                // Transitions
                // #transition 'DIST2'
                if (mDoorClosed.getAllClosed()) {
                    newState = State.STATE_DOORS_CLOSED;
                // #transition 'DIST6'
                } else if (!mDoorClosed.getAllClosed() &&
                           mAtFloors.getCurrentFloor() != MessageDictionary.NONE) {
                    newState = State.STATE_DOORS_OPEN_AT_FLOOR;
                } else {
                    newState = state;
                }
                break;
            case STATE_MOVING:
                // State actions for 'MOVING'
                nextFloor = Utility.getNextFloorMoving(mCarLevelPosition.getPosition(), 
                        mDriveSpeed.getSpeed(), mDriveSpeed.getDirection(), mCarCalls,
                        mHallCalls, TargetDirection, currentFloor);
                if (nextFloor == null) {
                    if (mAtFloors.getCurrentFloor() == MessageDictionary.NONE) {
                        throw new RuntimeException("Dispatcher is confused as to why we're moving");
                    }
                    // Don't update state variable here.
                } else {
                    TargetFloor = nextFloor.getFloor();
                    TargetHallway = nextFloor.getHallway();
                    TargetDirection = nextFloor.getDirection();
                }
                
                mDesiredFloor.set(TargetFloor, TargetHallway, TargetDirection);
                mDesiredDwellFront.set(DWELL_TIME);
                mDesiredDwellBack.set(DWELL_TIME);
                
                if (Utility.nextReachableFloorWhenMoving(mCarLevelPosition.getPosition(),
                        mDriveSpeed.getSpeed(), 
                        mDriveSpeed.getDirection()) == TargetFloor) {
                    newState = State.STATE_APPROACHING;
                } else if (!mDoorClosed.getAllClosed()) {
                    newState = State.STATE_DOORS_OPEN_BETWEEN_FLOORS;
                } else {
                    newState = state;
                }
                break;
            case STATE_APPROACHING:
                // State actions for 'DOORS CLOSED'
                nextFloor = Utility.getNextFloorDoorsClosed(mCarLevelPosition.getPosition(), 
                        mDriveSpeed.getSpeed(), mDriveSpeed.getDirection(), mCarCalls,
                        mHallCalls, currentFloor, TargetFloor);
                
                mDesiredFloor.set(TargetFloor, TargetHallway, TargetDirection);
                mDesiredDwellFront.set(DWELL_TIME);
                mDesiredDwellBack.set(DWELL_TIME);
                
                // Transitions
                if (!mDoorClosed.getAllClosed() &&
                    mAtFloors.getCurrentFloor() == MessageDictionary.NONE) {
                    newState = State.STATE_DOORS_OPEN_BETWEEN_FLOORS;
                } else if (!mDoorClosed.getAllClosed() &&
                           mAtFloors.getCurrentFloor() == TargetFloor) {
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
        setState("TARGET_FLOOR", Integer.toString(TargetFloor));
        setState("TARGET_HALLWAY", TargetHallway.toString());
        setState("TARGET_DIRECTION", TargetDirection.toString());
        
        
        // Schedule the next controller iteration
        timer.start(period);
    }
}
