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
     *  States
     */
    // DriveControl has 3 possible states
    private enum State {
        STATE_STOPPED,
        STATE_LEVELING_AT_DESIRED_FLOOR,
        STATE_NOT_AT_DESIRED_FLOOR
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
        int currentFloor;
        State newState = state;
        
        log ("State=", state, " DesiredFloor=", mDesiredFloor.getFloor(),
                " CurrentFloor=", mAtFloor.getCurrentFloor(),
                " Doors Closed=(", mDoorClosedFront.getBothClosed(), ",", 
                mDoorClosedBack.getBothClosed(), ") CarWeight=", mCarWeight.getWeight(), 
                " EmergencyBrake=" , mEmergencyBrake.getValue());
        switch(state) {
            case STATE_STOPPED:               
                // State actions for STATE_STOPPED
                drive.set(Speed.STOP, Direction.STOP);
                mDrive.set(Speed.STOP, Direction.STOP);
                mDriveSpeed.set(driveSpeed.speed(), driveSpeed.direction());
                
                currentFloor = mAtFloor.getCurrentFloor();
                // #transition DRT1
                if (mDesiredFloor.getFloor() != currentFloor &&
                        mDoorClosedFront.getBothClosed() && 
                        mDoorClosedBack.getBothClosed() &&
                        !mEmergencyBrake.getValue() && 
                        mCarWeight.getWeight() < Elevator.MaxCarCapacity) {
                    newState = State.STATE_NOT_AT_DESIRED_FLOOR;
                }
                break;
            case STATE_LEVELING_AT_DESIRED_FLOOR:
                // State actions for Leveling
                drive.set(Speed.LEVEL, mDesiredFloor.getDirection());
                mDrive.set(Speed.LEVEL, mDesiredFloor.getDirection());
                mDriveSpeed.set(driveSpeed.speed(), driveSpeed.direction());
                // #transition DRT4
                if (mLevelUp.getValue() && mLevelDown.getValue() ||
                        mEmergencyBrake.getValue() ||
                        mCarWeight.getWeight() >= Elevator.MaxCarCapacity) {
                    newState = State.STATE_STOPPED;
                }
                break;
            case STATE_NOT_AT_DESIRED_FLOOR:
                log("DC: State3 (Not at desiredFloor)");
                // State actions for not at desired floor
                drive.set(Speed.SLOW, mDesiredFloor.getDirection());
                mDrive.set(Speed.SLOW, mDesiredFloor.getDirection());
                mDriveSpeed.set(driveSpeed.speed(), driveSpeed.direction());
                currentFloor = mAtFloor.getCurrentFloor();
                // #transition DRT2
                if (mEmergencyBrake.getValue() ||
                        mCarWeight.getWeight() >= Elevator.MaxCarCapacity) {
                    newState = State.STATE_STOPPED;
                }
                // #transition DRT3
                else if (mDesiredFloor.getFloor() == currentFloor &&
                        !mEmergencyBrake.getValue() &&
                        mCarWeight.getWeight() < Elevator.MaxCarCapacity) {
                    newState = State.STATE_LEVELING_AT_DESIRED_FLOOR;
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
        
        // Restart the timer
        timer.start(period);
    }
}
