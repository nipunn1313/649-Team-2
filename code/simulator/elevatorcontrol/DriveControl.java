/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file DriveControl.java
 */
package simulator.elevatorcontrol;

import javax.management.RuntimeErrorException;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.elevatorcontrol.Utility.DoorClosedArray;
import simulator.elevatormodules.CarLevelPositionCanPayloadTranslator;
import simulator.elevatormodules.CarWeightCanPayloadTranslator;
import simulator.elevatormodules.HoistwayLimitSensorCanPayloadTranslator;
import simulator.elevatormodules.LevelingCanPayloadTranslator;
import simulator.framework.Controller;
import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.framework.ReplicationComputer;
import simulator.framework.Speed;
import simulator.payloads.CanMailbox;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.DrivePayload;
import simulator.payloads.DrivePayload.WriteableDrivePayload;
import simulator.payloads.DriveSpeedPayload;
import simulator.payloads.DriveSpeedPayload.ReadableDriveSpeedPayload;
import simulator.payloads.translators.BooleanCanPayloadTranslator;


/**
 * @class DriveControl
 * @extends Controller
 * @author Nick Mazurek
 */
public class DriveControl extends Controller {  
    /**
     * Outputs
     */
    // Drive
    private WriteableDrivePayload drive;
    private WriteableCanMailbox networkDriveBox;
    // mDrive
    private DriveCommandCanPayloadTranslator mDrive;
    private WriteableCanMailbox networkDriveSpeed;
    //mDriveSpeed
    private DriveSpeedCanPayloadTranslator mDriveSpeed;
    
    /**
     * Inputs
     */
    // DriveSpeed
    private ReadableDriveSpeedPayload driveSpeed;
    

    // mAtFloor 
    private AtFloorArray mAtFloor;

    /* 
     * mLevel 
     */
    // Level message for top
    private ReadableCanMailbox networkLevelUp;
    private LevelingCanPayloadTranslator mLevelUp;

    // Level message for bottom
    private ReadableCanMailbox networkLevelDown;
    private LevelingCanPayloadTranslator mLevelDown;

    // mCarLevelPosition
    private ReadableCanMailbox networkCarLevelPosition;
    private CarLevelPositionCanPayloadTranslator mCarLevelPosition;
    
    /* mDoorClosed */
    // DoorClosed Message for front
    private DoorClosedArray mDoorClosedFront;
    
    // DoorClosed Message for front
    private DoorClosedArray mDoorClosedBack; 
    
    // mEmergencyBrake
    private ReadableCanMailbox networkEmergencyBrake;
    private BooleanCanPayloadTranslator mEmergencyBrake;
    
    // mDesiredFloor
    private ReadableCanMailbox networkDesiredFloor;
    private DesiredFloorCanPayloadTranslator mDesiredFloor;
    
    /* 
     * mHoistwayLimit 
     */
    // Hoistway Limit up
    private ReadableCanMailbox networkHoistwayLimitUp;
    private HoistwayLimitSensorCanPayloadTranslator mHoistwayLimitUp;
    
    // Hoistway Limit down
    private ReadableCanMailbox networkHoistwayLimitDown;
    private HoistwayLimitSensorCanPayloadTranslator mHoistwayLimitDown;
    
    // mCarWeight
    private ReadableCanMailbox networkCarWeight;
    private CarWeightCanPayloadTranslator mCarWeight;
    
    /**
     * Instance Tracking and period
     */
    private final SimTime period;
    
    /**
     * State variables
     */
    private Direction currentDirection;
    
    /**
     *  States
     */
    // DriveControl has 3 possible states
    private enum State {
        STATE_STOPPED,
        STATE_NOT_AT_DESIRED_FLOOR,
        STATE_LEVELING
    }
    
    // Set the state to the initial value (stopped)
    private State state = State.STATE_STOPPED;

    /**
     * @constructor DriveControl
     * @param name
     * @param verbose
     */
    public DriveControl(SimTime period, boolean verbose) {
        super("DriveControl", verbose);
        
        // Store arguments
        this.period = period;
        log("DriveControl set with period: ", period);
        
        // Initialize outputs
        drive = DrivePayload.getWriteablePayload();
        physicalInterface.sendTimeTriggered(drive, period);
        
        networkDriveBox = CanMailbox.getWriteableCanMailbox(
                MessageDictionary.DRIVE_COMMAND_CAN_ID);
        mDrive = new DriveCommandCanPayloadTranslator(networkDriveBox);
        canInterface.sendTimeTriggered(networkDriveBox, period);
        
        networkDriveSpeed = CanMailbox.getWriteableCanMailbox(
                MessageDictionary.DRIVE_SPEED_CAN_ID);
        mDriveSpeed = new DriveSpeedCanPayloadTranslator(networkDriveSpeed);
        canInterface.sendTimeTriggered(networkDriveSpeed, period);
        
        // Initialize inputs
        driveSpeed = DriveSpeedPayload.getReadablePayload();
        physicalInterface.registerTimeTriggered(driveSpeed);
        
        mAtFloor = new AtFloorArray(canInterface);
        
        networkLevelUp = CanMailbox.getReadableCanMailbox(
                MessageDictionary.LEVELING_BASE_CAN_ID + 
                ReplicationComputer.computeReplicationId(Direction.UP));
        mLevelUp = new LevelingCanPayloadTranslator(networkLevelUp, Direction.UP);
        canInterface.registerTimeTriggered(networkLevelUp);
        
        networkLevelDown = CanMailbox.getReadableCanMailbox(
                MessageDictionary.LEVELING_BASE_CAN_ID +
                ReplicationComputer.computeReplicationId(Direction.DOWN));
        mLevelDown = new LevelingCanPayloadTranslator(networkLevelDown, Direction.DOWN);
        canInterface.registerTimeTriggered(networkLevelDown);
        
        networkCarLevelPosition = CanMailbox.getReadableCanMailbox(
                MessageDictionary.CAR_LEVEL_POSITION_CAN_ID);
        mCarLevelPosition = new CarLevelPositionCanPayloadTranslator(
                networkCarLevelPosition);
        canInterface.registerTimeTriggered(networkCarLevelPosition);
        
        mDoorClosedFront = new DoorClosedArray(Hallway.FRONT, canInterface);
        mDoorClosedBack = new DoorClosedArray(Hallway.BACK, canInterface);
        
        networkEmergencyBrake = CanMailbox.getReadableCanMailbox(
                MessageDictionary.EMERGENCY_BRAKE_CAN_ID);
        mEmergencyBrake = new BooleanCanPayloadTranslator(networkEmergencyBrake);
        canInterface.registerTimeTriggered(networkEmergencyBrake);
        
        networkDesiredFloor = CanMailbox.getReadableCanMailbox(
                MessageDictionary.DESIRED_FLOOR_CAN_ID);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(networkDesiredFloor);
        canInterface.registerTimeTriggered(networkDesiredFloor);
        
        networkHoistwayLimitUp = CanMailbox.getReadableCanMailbox(
                MessageDictionary.HOISTWAY_LIMIT_BASE_CAN_ID + 
                ReplicationComputer.computeReplicationId(Direction.UP));
        mHoistwayLimitUp = new HoistwayLimitSensorCanPayloadTranslator(
                networkHoistwayLimitUp, Direction.UP);
        canInterface.registerTimeTriggered(networkHoistwayLimitUp);
        
        networkHoistwayLimitDown = CanMailbox.getReadableCanMailbox(
                MessageDictionary.HOISTWAY_LIMIT_BASE_CAN_ID + 
                ReplicationComputer.computeReplicationId(Direction.DOWN));
        mHoistwayLimitDown = new HoistwayLimitSensorCanPayloadTranslator(
                networkHoistwayLimitDown, Direction.DOWN);
        canInterface.registerTimeTriggered(networkHoistwayLimitDown);
        
        networkCarWeight = CanMailbox.getReadableCanMailbox(
                MessageDictionary.CAR_WEIGHT_CAN_ID);
        mCarWeight = new CarWeightCanPayloadTranslator(networkCarWeight);
        canInterface.registerTimeTriggered(networkCarWeight);
        
        // Start the timer
        timer.start(period);
    }

    /**
     * @param callbackData
     * Set the outputs to the appropriate state values and determine
     * the next state that should be set
     */
    @Override
    public void timerExpired(Object callbackData) {
        State newState = state;
        
        log ("State=", state, " DesiredFloor=", mDesiredFloor.getFloor(),
                " CurrentFloor=", mAtFloor.getCurrentFloor(),
                " Doors Closed=(", mDoorClosedFront.getBothClosed(), ",", 
                mDoorClosedBack.getBothClosed(), ") CarWeight=", mCarWeight.getWeight(), 
                " EmergencyBrake=" , mEmergencyBrake.getValue(),
                " LevelUp=", mLevelUp.getValue(),
                " LevelDown=", mLevelDown.getValue());
        switch(state) {
            case STATE_STOPPED:               
                // State actions for STATE_STOPPED
                currentDirection = Direction.STOP;
                drive.set(Speed.STOP, currentDirection);
                mDrive.set(Speed.STOP, currentDirection);
                mDriveSpeed.set(driveSpeed.speed(), driveSpeed.direction());
                
                // #transition DRT1
                if (mDesiredFloor.getFloor() != mAtFloor.getCurrentFloor() &&
                    (mDesiredFloor.getFloor() >= 1) &&
                    (mDesiredFloor.getFloor() <= Elevator.numFloors) &&
                    (mDoorClosedFront.getBothClosed() && mDoorClosedBack.getBothClosed()) &&
                    getSafe() && !getObese()) {
                    newState = State.STATE_NOT_AT_DESIRED_FLOOR;
                // #transition DRT6
                } else if (!(mLevelUp.getValue() && mLevelDown.getValue()) &&
                           (mDesiredFloor.getFloor() == mAtFloor.getCurrentFloor() ||
                            !(mDoorClosedFront.getBothClosed() && mDoorClosedBack.getBothClosed())) &&
                           getSafe()) {
                    newState = State.STATE_LEVELING;
                }
                break;
            case STATE_NOT_AT_DESIRED_FLOOR:
                // State actions for STATE_AT_UNDESIRED_FLOOR
                currentDirection = getDesiredFloorDirection();
                drive.set(Speed.SLOW, currentDirection);
                mDrive.set(Speed.SLOW, currentDirection);
                mDriveSpeed.set(driveSpeed.speed(), driveSpeed.direction());
                
                // #transition DRT2
                if (!getSafe() || getObese()) {
                    newState = State.STATE_STOPPED;
                // #transition DRT3
                } else if (mDesiredFloor.getFloor() == mAtFloor.getCurrentFloor() &&
                           getSafe() && !getObese()) {
                    newState = State.STATE_LEVELING;
                }
                break;
            case STATE_LEVELING:
                // State actions for STATE_LEVELING_AT_DESIRED_FLOOR
                currentDirection = getLevelingDirection();
                drive.set(Speed.LEVEL, currentDirection);
                mDrive.set(Speed.LEVEL, currentDirection);
                mDriveSpeed.set(driveSpeed.speed(), driveSpeed.direction());
                
                // #transition DRT4
                if (mAtFloor.getCurrentFloor() == MessageDictionary.NONE &&
                    getSafe() && !getObese()) {
                    newState = State.STATE_NOT_AT_DESIRED_FLOOR;
                // #transition DRT5
                } else if ((mLevelUp.getValue() && mLevelDown.getValue()) ||
                           ((mAtFloor.getCurrentFloor() != MessageDictionary.NONE) &&
                           (mDesiredFloor.getFloor() != mAtFloor.getCurrentFloor())) ||
                           !getSafe()) {
                    newState = State.STATE_STOPPED;
                }
                break;
            default:
                throw new RuntimeException("State: " + state + " was not recognized");
        }
        
        if (state == newState) {
            log("Remains in state ", state);
        }
        else {
            log("Transition",state,"->",newState);
        }
        
        state = newState;
        // Move to the new state
        setState(STATE_KEY, newState.toString());
        setState("CURRENTDIRECTION", currentDirection.toString());
        
        // Restart the timer
        timer.start(period);
    }
    
    private Direction getDesiredFloorDirection() {
        int currentFloor = mAtFloor.getCurrentFloor();
        int desiredFloor = mDesiredFloor.getFloor();
        
        if (currentFloor == MessageDictionary.NONE) {
            return currentDirection;
        }
        
        if (currentFloor < desiredFloor) {
            return Direction.UP;
        } else if (currentFloor > desiredFloor) {
            return Direction.DOWN;
        } else {
            return Direction.STOP;
        }
    }
    
    private Direction getLevelingDirection() {
        if (mLevelUp.getValue() == false) {
            return Direction.UP;
        } else if (mLevelDown.getValue() == false) {
            return Direction.DOWN;
        } else {
            return Direction.STOP;
        }
    }
    
    private boolean getSafe() {
        return mEmergencyBrake.getValue() == false;
    }
    
    private boolean getObese() {
        return mCarWeight.getWeight() > Elevator.MaxCarCapacity;
    }
}
