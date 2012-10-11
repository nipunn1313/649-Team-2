/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file LanternControl.java
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
import simulator.payloads.CarLanternPayload;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.CarLanternPayload.WriteableCarLanternPayload;

public class LanternControl extends Controller {

    /***************************************************************
     * Declarations
     **************************************************************/
    //local physical state
    private WriteableCarLanternPayload localCarLantern;
    
    // network interface
    // DoorClosedArray creates CanPayloadTranslators for DoorClosed
    private DoorClosedArray mDoorClosedFront, mDoorClosedBack;
    // receive DesiredFloor from network
    private ReadableCanMailbox networkDesiredFloorIn;
    // translator for DesiredFloor message
    private DesiredFloorCanPayloadTranslator mDesiredFloor;
    
    // send CarLantern to network
    private WriteableCanMailbox networkCarLanternOut;
    // translator for CarLantern message
    private CarLanternCanPayloadTranslator mCarLantern;
    
    // keep track of which instance this is.
    private final Direction direction;
    
    //store the period for the controller
    private SimTime period;
    
    //enumerate states
    private enum State {
        STATE_LIGHT_OFF,
        STATE_LIGHT_ON,
    }
    //state variable initialized to the initial state DOORS_CLOSED
    private State state = State.STATE_LIGHT_OFF;
    
    
    public LanternControl(Direction direction, SimTime period, boolean verbose) {
        // call to the Controller superclass constructor is required
        super("LanternControl" + ReplicationComputer.makeReplicationString(direction), verbose);
                
        this.period = period;
        this.direction = direction;
        
        /* The log() method is inherited from the Controller class. */
        log("Created Lantern controller with period = ", period);
        
        //initialize physical state
        localCarLantern = CarLanternPayload.getWriteablePayload(direction);
        //register the payload with the physical interface (as in input) -- it will be updated
        //periodically when the hall call button state is modified.
        physicalInterface.sendTimeTriggered(localCarLantern,period);    
        
        //initialize network interface
        
        //Register mDoorCloseds
        mDoorClosedFront = new DoorClosedArray(Hallway.FRONT, canInterface);
        mDoorClosedBack = new DoorClosedArray(Hallway.BACK, canInterface);
        
        // Register mDesiredFloor
        networkDesiredFloorIn = CanMailbox.getReadableCanMailbox(MessageDictionary.DESIRED_FLOOR_CAN_ID);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(networkDesiredFloorIn);
        canInterface.registerTimeTriggered(networkDesiredFloorIn);
        
        // Register mCarLantern
        networkCarLanternOut = CanMailbox.getWriteableCanMailbox(MessageDictionary.CAR_LANTERN_BASE_CAN_ID + ReplicationComputer.computeReplicationId(direction));
        mCarLantern = new CarLanternCanPayloadTranslator(networkCarLanternOut);
        canInterface.sendTimeTriggered(networkCarLanternOut, period);
        
        timer.start(period);
    }
    
    @Override
    public void timerExpired(Object callbackData) {
        State newState = state;
        Direction currentDirection;
        
        switch(state) {
            case STATE_LIGHT_OFF:
                // state actions for 'LIGHTS_OFF'
                localCarLantern.set(false);
                mCarLantern.set(false);
                
                //transitions
                //#transition 'LT 1'
                currentDirection = mDesiredFloor.getDirection();
                if (currentDirection == direction &&
                        !(mDoorClosedFront.getBothClosed() &&
                                mDoorClosedBack.getBothClosed())) {
                    newState = State.STATE_LIGHT_ON;
                } else {
                    newState = state;
                }
                break;
            case STATE_LIGHT_ON:
                // state actions for 'LIGHTS_ON'
                localCarLantern.set(true);
                mCarLantern.set(true);
                
                //transitions
                //#transition 'LT 2'
                currentDirection = mDesiredFloor.getDirection();
                if (currentDirection == Direction.STOP ||
                        (mDoorClosedFront.getBothClosed() &&
                                mDoorClosedBack.getBothClosed())) {
                    newState = State.STATE_LIGHT_OFF;
                } else {
                    newState = state;
                }
                break;
            default:
                throw new RuntimeException("State " + state + " was not recognized.");
                
        }
        
        //log the results of this iteration
        if (state == newState) {
            log("remains in state: ",state);
        } else {
            log("Transition:",state,"->",newState);
        }
        
        //update the state variable
        state = newState;

        //report the current state
        setState(STATE_KEY,newState.toString());

        //schedule the next iteration of the controller
        //you must do this at the end of the timer callback in order to restart
        //the timer
        timer.start(period);
    }

}
