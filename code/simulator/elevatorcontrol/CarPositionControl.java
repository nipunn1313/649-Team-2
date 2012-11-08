/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file CarPositionControl.java
 */

package simulator.elevatorcontrol;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.elevatormodules.CarLevelPositionCanPayloadTranslator;
import simulator.framework.Controller;
import simulator.framework.Direction;
import simulator.payloads.CanMailbox;
import simulator.payloads.CarPositionIndicatorPayload;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CarPositionIndicatorPayload.WriteableCarPositionIndicatorPayload;

/**
 * 
 * @author Nick Mazurek
 *
 */
public class CarPositionControl extends Controller {

    /**
     * Outputs
     */
    // CarPositionIndicator
    private WriteableCarPositionIndicatorPayload carPositionIndicator;
    /**
     * @note: Nothing uses mCarPositionINdicator yet, so it has not bee implemented.
     */
    
    /**
     * Inputs
     */
    // At Floor
    private AtFloorArray mAtFloor;
    
    // mCarLevelPosition
    private ReadableCanMailbox networkCarLevelPosition;
    private OurIntegerCanPayloadTranslator mCarLevelPosition;
    
    // mDesiredFloor
    private ReadableCanMailbox networkDesiredFloor;
    private DesiredFloorCanPayloadTranslator mDesiredFloor;
    
    // mDriveSpeed
    private ReadableCanMailbox networkDriveSpeed;
    private DriveSpeedCanPayloadTranslator mDriveSpeed;
       
    private enum State {
        STATE_DISPLAY;
    }
    
    private State state = State.STATE_DISPLAY;
    private int currentlyDisplayedFloor;
    private final SimTime period;
    
    /**
     * @Constructor CarPositionControl
     * @param period
     * @param verbose
     */
    public CarPositionControl(SimTime period, boolean verbose) {
        super("CarPositionControl", verbose);
        
        // Store arguments
        this.period = period;
        log("CarPositionControl with period: ", period);
        
        carPositionIndicator = CarPositionIndicatorPayload.getWriteablePayload();
        physicalInterface.sendTimeTriggered(carPositionIndicator, period);
        
        // Initialize Inputs
        mAtFloor = new AtFloorArray(canInterface);
        
        networkCarLevelPosition = CanMailbox.getReadableCanMailbox(
                MessageDictionary.CAR_LEVEL_POSITION_CAN_ID);
        mCarLevelPosition = new OurIntegerCanPayloadTranslator(
                networkCarLevelPosition, 16);
        canInterface.registerTimeTriggered(networkCarLevelPosition);
        
        networkDesiredFloor = CanMailbox.getReadableCanMailbox(
                MessageDictionary.DESIRED_FLOOR_CAN_ID);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(
                networkDesiredFloor);
        canInterface.registerTimeTriggered(networkDesiredFloor);
        
        networkDriveSpeed = CanMailbox.getReadableCanMailbox(
                MessageDictionary.DRIVE_SPEED_CAN_ID);
        mDriveSpeed = new DriveSpeedCanPayloadTranslator(
                networkDriveSpeed);
        canInterface.registerTimeTriggered(networkDriveSpeed);
        
        // Initialize the car position to the first floor
        carPositionIndicator.set(1);
        currentlyDisplayedFloor = 1;
        
        timer.start(period);
    }
    
    /**
     * @param callbackData
     * Sets the output to the appropriate state value and determines the next 
     * state value that should be set
     */
    @Override
    public void timerExpired(Object callbackData) {
        switch(state) {
            case STATE_DISPLAY:
                Direction d = mDriveSpeed.getDirection();
                double speed = mDriveSpeed.getSpeed();
                int carLevelPosition = mCarLevelPosition.getValue();
                currentlyDisplayedFloor = Utility.nextReachableFloor(currentlyDisplayedFloor,
                        carLevelPosition, speed, d);
                carPositionIndicator.set(currentlyDisplayedFloor);
                break;
            default:
                throw new RuntimeException("State: " + state + " was not recognized");
        }
        log("Floor=", mAtFloor.getCurrentFloor(), 
                " DesiredFloor=", mDesiredFloor.getFloor(),
                " DriveSpeed=", mDriveSpeed.getSpeed(), " ", mDriveSpeed.getDirection(),
                " CarLevelPos=", mCarLevelPosition.getValue(),
                " CarPos=", carPositionIndicator.floor());
        
        // There's only one state, so set it at the end.
        setState(STATE_KEY, state.toString());
        
        // Restart the timer
        timer.start(period);
    }

}
