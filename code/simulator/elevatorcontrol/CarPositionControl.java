/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file CarPositionControl.java
 */

package simulator.elevatorcontrol;

import java.net.NetworkInterface;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.elevatormodules.CarLevelPositionCanPayloadTranslator;
import simulator.framework.Controller;
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
    private CarLevelPositionCanPayloadTranslator mCarLevelPosition;
    
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
        mCarLevelPosition = new CarLevelPositionCanPayloadTranslator(
                networkCarLevelPosition);
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
        
        timer.start(period);
    }
    
    /**
     * @param callbackData
     * Sets the output to the appropriate state value and determines the next 
     * state value that should be set
     */
    @Override
    public void timerExpired(Object callbackData) {
        int currentFloor;
        switch(state) {
            case STATE_DISPLAY:
                currentFloor = mAtFloor.getCurrentFloor();
                // CurrentFloor returns -1 if the car is not at a floor, so 
                // account for that.
                // TODO: Figure out how to clear the position indicator
                if (currentFloor >= 0)
                    carPositionIndicator.set(currentFloor);
                break;
            default:
                throw new RuntimeException("State: " + state + " was not recognized");
        }
        log("Current floor: ", mAtFloor.getCurrentFloor(), 
                " DesiredFloor ", mDesiredFloor.getFloor(),
                " Drive Speed ", mDriveSpeed.getSpeed(),
                " CarPosition: ", carPositionIndicator.floor());
        
        // There's only one state, so set it at the end.
        setState(STATE_KEY, state.toString());
        
        // Restart the timer
        timer.start(period);
    }

}