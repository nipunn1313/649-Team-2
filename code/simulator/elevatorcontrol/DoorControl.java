/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file DoorControl.java
 */

package simulator.elevatorcontrol;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.elevatorcontrol.Utility.DoorClosedArray;
import simulator.elevatorcontrol.Utility.DoorOpenedArray;
import simulator.elevatorcontrol.Utility.DoorReversalArray;
import simulator.elevatormodules.CarWeightCanPayloadTranslator;
import simulator.framework.Controller;
import simulator.framework.Direction;
import simulator.framework.DoorCommand;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.framework.ReplicationComputer;
import simulator.framework.Side;
import simulator.framework.Speed;
import simulator.payloads.CanMailbox;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.DoorMotorPayload;
import simulator.payloads.DoorMotorPayload.WriteableDoorMotorPayload;

/**
 * Door Control controls Door Motor and interfaces with the CANNetwork
 * @author ben
 *
 */
public class DoorControl extends Controller {

    private final double EPSILON = 0.01;
    
    /***************************************************************
     * Declarations
     **************************************************************/
    //local physical state
    private WriteableDoorMotorPayload localDoorMotor;

    // network interface
    // AtFloorArray creates CanPayloadTranslators for AtFloor
    private AtFloorArray mAtFloors;
    // receive DriveSpeed from network
    private ReadableCanMailbox networkDriveSpeedIn;
    // translator for DriveSpeed message
    private DriveSpeedCanPayloadTranslator mDriveSpeed;
    // DoorClosedArray creates CanPayloadTranslators for DoorClosed
    private DoorClosedArray mDoorClosedFront, mDoorClosedBack;
    // DoorOpenedArray creates CanPayloadTranslators for DoorOpened
    private DoorOpenedArray mDoorOpenedFront, mDoorOpenedBack;
    // DoorReversayArray creates CanPayloadTranslators for DoorReversal
    private DoorReversalArray mDoorReversalFront, mDoorReversalBack;
    //receive CarWeight from network
    private ReadableCanMailbox networkCarWeightIn;
    // translator for CarWeight message
    private CarWeightCanPayloadTranslator mCarWeight;
    // receive DesiredFloor from network
    private ReadableCanMailbox networkDesiredFloorIn;
    // translator for DesiredFloor message
    private DesiredFloorCanPayloadTranslator mDesiredFloor;
    //	// receive DesiredDwell from network -- TODO: FOR USE IN DISPATCHER LAB
    //	private ReadableCanMailbox networkDesiredDwellIn;
    //	// translator for DesiredDwell message
    //	private DesiredDwellCanPayloadTranslator mDesiredDwell;
    // send DoorMotor to network
    private WriteableCanMailbox networkDoorMotorOut;
    // translator for DoorMotor message
    private DoorMotorCanPayloadTranslator mDoorMotor;

    // Countdown state variable for closing door
    private SimTime countdown;
    private SimTime dwell;

    // keep track of which instance this is.
    private final Hallway hallway;
    private final Side side;

    //store the period for the controller
    private SimTime period;

    // additional internal state variables
    private SimTime counter = SimTime.ZERO;

    //enumerate states
    private enum State {
        STATE_DOORS_CLOSED,
        STATE_DOORS_OPENING,
        STATE_DOORS_OPENED,
        STATE_DOORS_NUDGE,
    }
    //state variable initialized to the initial state DOORS_CLOSED
    private State state = State.STATE_DOORS_CLOSED;		

    public DoorControl(Hallway hallway, Side side, SimTime period, boolean verbose) {
        // call to the Controller superclass constructor is required
        super("DoorControl" + ReplicationComputer.makeReplicationString(hallway, side), verbose);

        this.period = period;
        this.hallway = hallway;
        this.side = side;

        /* The log() method is inherited from the Controller class. */
        log("Created Door controller with period = ", period);

        //initialize physical state
        localDoorMotor = DoorMotorPayload.getWriteablePayload(hallway, side);
        //register the payload with the physical interface (as in input) -- it will be updated
        //periodically when the hall call button state is modified.
        physicalInterface.sendTimeTriggered(localDoorMotor,period);

        //initialize network interface
        // Register mAtFloors
        mAtFloors = new AtFloorArray(canInterface);

        // Register mDriveSpeed
        networkDriveSpeedIn = CanMailbox.getReadableCanMailbox(MessageDictionary.DRIVE_SPEED_CAN_ID);
        mDriveSpeed = new DriveSpeedCanPayloadTranslator(networkDriveSpeedIn);
        canInterface.registerTimeTriggered(networkDriveSpeedIn);

        //Register mDoorCloseds
        mDoorClosedFront = new DoorClosedArray(Hallway.FRONT, canInterface);
        mDoorClosedBack = new DoorClosedArray(Hallway.BACK, canInterface);

        //Register mDoorOpeneds
        mDoorOpenedFront = new DoorOpenedArray(Hallway.FRONT, canInterface);
        mDoorOpenedBack = new DoorOpenedArray(Hallway.BACK, canInterface);
        
        //Register mDoorReversals
        mDoorReversalFront = new DoorReversalArray(Hallway.FRONT, canInterface);
        mDoorReversalBack = new DoorReversalArray(Hallway.BACK, canInterface);

        // Register mCarWeight
        networkCarWeightIn = CanMailbox.getReadableCanMailbox(MessageDictionary.CAR_WEIGHT_CAN_ID);
        mCarWeight = new CarWeightCanPayloadTranslator(networkCarWeightIn);
        canInterface.registerTimeTriggered(networkCarWeightIn);

        // Register mDesiredFloor
        networkDesiredFloorIn = CanMailbox.getReadableCanMailbox(MessageDictionary.DESIRED_FLOOR_CAN_ID);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(networkDesiredFloorIn);
        canInterface.registerTimeTriggered(networkDesiredFloorIn);

        // Register mDesiredDwell TODO: FOR USE IN DISPATCHER LAB
        //        networkDesiredDwellIn = CanMailbox.getReadableCanMailbox(MessageDictionary.DESIRED_DWELL_BASE_CAN_ID + ReplicationComputer.computeReplicationId(hallway));
        //        mDesiredDwell = new DesiredDwellCanPayloadTranslator(networkDesiredDwellIn);
        //        canInterface.registerTimeTriggered(networkDesiredDwellIn);
        dwell = new SimTime("5s");

        // Register mDoorMotor
        networkDoorMotorOut = CanMailbox.getWriteableCanMailbox(MessageDictionary.DOOR_MOTOR_COMMAND_BASE_CAN_ID + ReplicationComputer.computeReplicationId(this.hallway, this.side));
        mDoorMotor = new DoorMotorCanPayloadTranslator(networkDoorMotorOut, hallway, side);
        canInterface.sendTimeTriggered(networkDoorMotorOut, period);

        timer.start(period);
    }

    @Override
    public void timerExpired(Object callbackData) {
        State newState = state;
        int currentFloor;
        // We do not need mDesiredDwell yet since there is no Dispatcher.
        // Just use a dwell constant of 5 seconds for now
        //dwell = mDesiredDwell.getDwell(); -- TODO: FIX FOR DISPATCHER
        switch (state) {
            case STATE_DOORS_CLOSED:
                //state actions for 'DOORS_CLOSED'
                localDoorMotor.set(DoorCommand.STOP);
                mDoorMotor.setDoorCommand(DoorCommand.STOP);

                //transitions
                //#transition 'DoT 1'
                currentFloor = mAtFloors.getCurrentFloor();
                if ((currentFloor == mDesiredFloor.getFloor() &&
                        mAtFloors.isAtFloor(currentFloor, hallway) &&
                        ((mDriveSpeed.getScaledSpeed()==0) || 
                                mDriveSpeed.getDirection()==Direction.STOP))) {
                    newState = State.STATE_DOORS_OPENING;
                } else {
                    newState = state;
                }
                break;
            case STATE_DOORS_OPENING:
                //state actions for 'DOORS_OPENING'
                localDoorMotor.set(DoorCommand.OPEN);
                mDoorMotor.setDoorCommand(DoorCommand.OPEN);

                // Set local state variables
                countdown = dwell;

                //transitions
                //#transition 'DoT 2'
                if (mDoorOpenedFront.getBothOpened() &&
                        mDoorOpenedBack.getBothOpened()) {
                    newState = State.STATE_DOORS_OPENED;
                } else {
                    newState = state;
                }
                break;
            case STATE_DOORS_OPENED:
                //state actions for 'DOORS_OPENED'
                localDoorMotor.set(DoorCommand.STOP);
                mDoorMotor.setDoorCommand(DoorCommand.STOP);

                // Set local state variables
                countdown = SimTime.subtract(countdown, period);

                //transitions
                //#transition 'DoT 3'
                if (countdown.isLessThanOrEqual(SimTime.ZERO) &&
                        mCarWeight.getValue() < Elevator.MaxCarCapacity) {
                    newState = State.STATE_DOORS_NUDGE;
                } else {
                    newState = state;
                }
                break;
            case STATE_DOORS_NUDGE:
                //state actions for 'DOORS_OPENING'
                localDoorMotor.set(DoorCommand.NUDGE);
                mDoorMotor.setDoorCommand(DoorCommand.NUDGE);

                currentFloor = mAtFloors.getCurrentFloor();
                //transitions
                //#transition 'DoT 4'
                if (mDoorClosedFront.getBothClosed() &&
                        mDoorClosedBack.getBothClosed()) {
                    newState = State.STATE_DOORS_CLOSED;
                }
                //#transition 'DoT 5'
                else if ((mCarWeight.getValue() >= Elevator.MaxCarCapacity) &&
                        mAtFloors.isAtFloor(currentFloor, hallway)) {
                    newState = State.STATE_DOORS_OPENING;
                }
                //#transition 'DoT 5'
                else if (mDoorReversalFront.getAnyReversal()) {
                    newState = State.STATE_DOORS_OPENING;
                }
                //#transition 'DoT 5'
                else if (mDoorReversalBack.getAnyReversal()) {
                    newState = State.STATE_DOORS_OPENING;
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
