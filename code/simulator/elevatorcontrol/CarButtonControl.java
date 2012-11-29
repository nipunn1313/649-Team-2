/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file CarButtonControl.java
 */

package simulator.elevatorcontrol;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.framework.Controller;
import simulator.framework.Hallway;
import simulator.framework.ReplicationComputer;
import simulator.payloads.CanMailbox;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.CarCallPayload;
import simulator.payloads.CarCallPayload.ReadableCarCallPayload;
import simulator.payloads.CarLightPayload;
import simulator.payloads.CarLightPayload.WriteableCarLightPayload;

public class CarButtonControl extends Controller {
    
    /* States! */
    private enum State {
        STATE_NO_CALL,
        STATE_CALLED;
    }
    private State state = State.STATE_NO_CALL;
    
    private final int floor;
    //private final Hallway hallway;
    private final SimTime period;

    /* Input Interface */
    ReadableCarCallPayload localCarButton;
    AtFloorArray mAtFloors;
    DesiredFloorCanPayloadTranslator mDesiredFloor;
    
    /* Output Interface */
    WriteableCarLightPayload localCarLight;
    CarLightCanPayloadTranslator mCarLight;
    CarCallCanPayloadTranslator mCarCall;
    
    /* Mailboxes */
    ReadableCanMailbox networkDesiredFloor;
    WriteableCanMailbox networkCarLight;
    WriteableCanMailbox networkCarCall;
    
    public CarButtonControl(int floor, Hallway hallway, 
            SimTime period, boolean verbose) {
        super("CarButtonControl" + ReplicationComputer.makeReplicationString(floor, hallway),
              verbose);
        
        log("Creating CarButtonControl(", floor, ",", hallway, ")");

        /* Save floor/hallway/period */
        this.floor = floor;
        //this.hallway = hallway;
        this.period = period;
        
        /* Setup mailboxes */
        networkDesiredFloor = CanMailbox.getReadableCanMailbox(
                MessageDictionary.DESIRED_FLOOR_CAN_ID);
        networkCarLight = CanMailbox.getWriteableCanMailbox(
                MessageDictionary.CAR_LIGHT_BASE_CAN_ID +
                ReplicationComputer.computeReplicationId(floor, hallway));
        networkCarCall = CanMailbox.getWriteableCanMailbox(
                MessageDictionary.CAR_CALL_BASE_CAN_ID +
                ReplicationComputer.computeReplicationId(floor, hallway));
        
        /* Setup input payloads/messages */
        localCarButton = CarCallPayload.getReadablePayload(floor, hallway);
        mAtFloors = new AtFloorArray(canInterface);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(networkDesiredFloor);
        
        /* Setup output payloads/messages */
        localCarLight = CarLightPayload.getWriteablePayload(floor, hallway);
        mCarLight = new CarLightCanPayloadTranslator(networkCarLight);
        mCarCall = new CarCallCanPayloadTranslator(networkCarCall);
        
        /* Setup input time-triggered updates (atFloor is taken care of) */
        physicalInterface.registerTimeTriggered(localCarButton);
        canInterface.registerTimeTriggered(networkDesiredFloor);
        
        /* Setup output time-triggered updates */
        physicalInterface.sendTimeTriggered(localCarLight, period);
        canInterface.sendTimeTriggered(networkCarLight, period);
        canInterface.sendTimeTriggered(networkCarCall, period);
        
        
        timer.start(period);
    }
    
    @Override
    public void timerExpired(Object callbackData) {
        log("Timer expired state=", state,
            " isPressed=", localCarButton.isPressed(),
            " currentFloor=", mAtFloors.getCurrentFloor(),
            " mDesiredFloor=", mDesiredFloor.getFloor());
        State newState = state;
        switch (state) {
            case STATE_NO_CALL:
                mCarCall.set(false);
                localCarLight.set(false);
                mCarLight.set(false);
                
                //#transition CBT 1
                if (localCarButton.isPressed()) {
                    newState = State.STATE_CALLED;
                }
                break;
            case STATE_CALLED:
                mCarCall.set(true);
                localCarLight.set(true);
                mCarLight.set(true);
                
                if (!localCarButton.isPressed()) {
                    //#transition CBT 2
                    if (mAtFloors.getCurrentFloor() == mDesiredFloor.getFloor() &&
                        mAtFloors.getCurrentFloor() == floor) {
                        newState = State.STATE_NO_CALL;
                    }
                }
                break;
            default:
                throw new RuntimeException("State " + state + " wasn't recognized");    
        }
        
        if (state != newState) {
            log("Transitioning from state ", state, " to ", newState);
        }
        
        state = newState;
        setState(STATE_KEY, newState.toString());
        timer.start(period);
    }

}
