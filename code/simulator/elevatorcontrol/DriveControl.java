/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file DriveControl.java
 */
package simulator.elevatorcontrol;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.elevatorcontrol.Utility.DoorClosedArray;
import simulator.elevatormodules.CarLevelPositionCanPayloadTranslator;
import simulator.elevatormodules.CarWeightCanPayloadTranslator;
import simulator.elevatormodules.LevelingCanPayloadTranslator;
import simulator.framework.Controller;
import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.framework.Speed;
import simulator.payloads.AtFloorPayload.ReadableAtFloorPayload;
import simulator.payloads.CanMailbox;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.CarPositionPayload.ReadableCarPositionPayload;
import simulator.payloads.DoorClosedPayload.ReadableDoorClosedPayload;
import simulator.payloads.DrivePayload;
import simulator.payloads.DrivePayload.WriteableDrivePayload;
import simulator.payloads.DriveSpeedPayload;
import simulator.payloads.DriveSpeedPayload.ReadableDriveSpeedPayload;
import simulator.payloads.EmergencyBrakePayload.ReadableEmergencyBrakePayload;
import simulator.payloads.HoistwayLimitPayload.ReadableHoistwayLimitPayload;
import simulator.payloads.LevelingPayload;
import simulator.payloads.LevelingPayload.ReadableLevelingPayload;
import simulator.payloads.translators.BooleanCanPayloadTranslator;
import simulator.payloads.translators.IntegerCanPayloadTranslator;


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
    private WriteableCanMailbox driveBox;
    // mDrive
    private DriveCommandCanPayloadTranslator mDrive;
    private WriteableCanMailbox driveSpeedBox;
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
    private ReadableCanMailbox levelUp;
    private LevelingCanPayloadTranslator mLevelUp;

    // Level message for bottom
    private ReadableCanMailbox levelDown;
    private LevelingCanPayloadTranslator mLevelDown;

    // mCarLevelPosition
    private ReadableCanMailbox carLevelPosition;
    private CarLevelPositionCanPayloadTranslator mCarLevelPosition;
    
    /* mDoorClosed */
    // DoorClosed Message for front
    private DoorClosedArray mDoorClosedFront;
    
    // DoorClosed Message for front
    private DoorClosedArray mDoorClosedBack; 
    
    // mEmergencyBrake
    private ReadableCanMailbox emergencyBrake;
    private BooleanCanPayloadTranslator mEmergencyBrake;
    
    // mDesiredFloor
    private ReadableCanMailbox desiredFloor;
    private DesiredFloorCanPayloadTranslator mDesiredFloor;
    
    /* 
     * mHoistwayLimit 
     */
    // Hoistway Limit up
    private ReadableCanMailbox hoistwayLimitUp;
    private BooleanCanPayloadTranslator mHoistwayLimitUp;
    
    // Hoistway Limit down
    private ReadableCanMailbox hoistwayLimitDown;
    private BooleanCanPayloadTranslator mHoistwayLimitDown;
    
    // mCarWeight
    private ReadableCanMailbox carWeight;
    private CarWeightCanPayloadTranslator mCarWeight;
    
    /**
     * Instance Tracking and period
     */
    private final String name;
    private final SimTime period = MessageDictionary.DRIVE_CONTROL_PERIOD;
    
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
    public DriveControl(String name, boolean verbose) {
        super(name, verbose);

        // Store arguments
        this.name = name;
        
        // Initialize outputs
        drive = DrivePayload.getWriteablePayload();
        physicalInterface.sendTimeTriggered(drive, period);
        
        driveBox = CanMailbox.getWriteableCanMailbox(
                MessageDictionary.DRIVE_COMMAND_CAN_ID);
        mDrive = new DriveCommandCanPayloadTranslator(driveBox);
        canInterface.sendTimeTriggered(driveBox, period);
        
        driveSpeedBox = CanMailbox.getWriteableCanMailbox(
                MessageDictionary.DRIVE_SPEED_CAN_ID);
        mDriveSpeed = new DriveSpeedCanPayloadTranslator(driveSpeedBox);
        canInterface.sendTimeTriggered(driveSpeedBox, period);
        
        // Initialize inputs
        driveSpeed = DriveSpeedPayload.getReadablePayload();
        physicalInterface.registerTimeTriggered(driveSpeed);
        
        mAtFloor = new AtFloorArray(canInterface);
        
        levelUp = CanMailbox.getReadableCanMailbox(
                MessageDictionary.LEVELING_BASE_CAN_ID);
        mLevelUp = new LevelingCanPayloadTranslator(levelUp, Direction.UP);
        canInterface.registerTimeTriggered(levelUp);
        
        levelDown = CanMailbox.getReadableCanMailbox(
                MessageDictionary.LEVELING_BASE_CAN_ID);
        mLevelDown = new LevelingCanPayloadTranslator(levelDown, Direction.DOWN);
        canInterface.registerTimeTriggered(levelDown);
        
        carLevelPosition = CanMailbox.getReadableCanMailbox(
                MessageDictionary.CAR_POSITION_CAN_ID);
        mCarLevelPosition = new CarLevelPositionCanPayloadTranslator(
                carLevelPosition);
        canInterface.registerTimeTriggered(carLevelPosition);
        
        mDoorClosedFront = new DoorClosedArray(Hallway.FRONT, canInterface);
        mDoorClosedBack = new DoorClosedArray(Hallway.BACK, canInterface);
        
        emergencyBrake = CanMailbox.getReadableCanMailbox(
                MessageDictionary.EMERGENCY_BRAKE_CAN_ID);
        mEmergencyBrake = new BooleanCanPayloadTranslator(emergencyBrake);
        
        desiredFloor = CanMailbox.getReadableCanMailbox(
                MessageDictionary.DESIRED_FLOOR_CAN_ID);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(desiredFloor);
        
        hoistwayLimitUp = CanMailbox.getReadableCanMailbox(
                MessageDictionary.HOISTWAY_LIMIT_BASE_CAN_ID);
        hoistwayLimitDown = CanMailbox.getReadableCanMailbox(
                MessageDictionary.HOISTWAY_LIMIT_BASE_CAN_ID);
        
        carWeight = CanMailbox.getReadableCanMailbox(
                MessageDictionary.CAR_WEIGHT_CAN_ID);
        mCarWeight = new CarWeightCanPayloadTranslator(carWeight);
        
        // Start the timer
        timer.start(period);
    }

    /**
     * @param callbackData
     * Set the outputs to the appopriate state values and determine
     * the next state that should be set
     */
    @Override
    public void timerExpired(Object callbackData) {
        int currentFloor;
        State newState = state;
        
        switch(state) {
            case STATE_STOPPED:
                log("State 1");
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
                    log("DRT1");
                    newState = State.STATE_NOT_AT_DESIRED_FLOOR;
                }
                break;
            case STATE_LEVELING_AT_DESIRED_FLOOR:
                log("State2");
                // State actions for Leveling
                drive.set(Speed.LEVEL, mDesiredFloor.getDirection());
                mDrive.set(Speed.LEVEL, mDesiredFloor.getDirection());
                mDriveSpeed.set(driveSpeed.speed(), driveSpeed.direction());
                
                // #transition DRT4
                if (mLevelUp.getValue() && mLevelDown.getValue() ||
                        mEmergencyBrake.getValue() ||
                        mCarWeight.getWeight() >= Elevator.MaxCarCapacity) {
                    log("DRT4");
                    newState = State.STATE_STOPPED;
                }
                break;
            case STATE_NOT_AT_DESIRED_FLOOR:
                // State actions for not at desired floor
                drive.set(Speed.SLOW, mDesiredFloor.getDirection());
                mDrive.set(Speed.SLOW, mDesiredFloor.getDirection());
                mDriveSpeed.set(driveSpeed.speed(), driveSpeed.direction());
                currentFloor = mAtFloor.getCurrentFloor();
                
                // #transition DRT2
                if (mEmergencyBrake.getValue() ||
                        mCarWeight.getWeight() >= Elevator.MaxCarCapacity) {
                    log("DRT2");
                    newState = State.STATE_STOPPED;
                }
                // #transition DRT3
                else if (mDesiredFloor.getFloor() == currentFloor &&
                        !mEmergencyBrake.getValue() &&
                        mCarWeight.getWeight() < Elevator.MaxCarCapacity) {
                    log("DRT3");
                    newState = State.STATE_LEVELING_AT_DESIRED_FLOOR;
                }
                break;
        }
        
        if (state == newState) {
            log("Remains in state", state);
        }
        else {
            log("Transition",state,"->",newState);
        }
        
        // Move to the new state
        setState(STATE_KEY, newState.toString());
        
        // Restart the timer
        timer.start(period);
    }
}
