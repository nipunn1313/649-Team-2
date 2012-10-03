package simulator.elevatorcontrol;

import jSimPack.SimTime;
import simulator.framework.Controller;
import simulator.framework.DoorCommand;
import simulator.framework.Harness;
import simulator.framework.Direction;
import simulator.framework.Hallway;
import simulator.framework.ReplicationComputer;
import simulator.framework.Side;

//import simulator.elevatorcontrol.TestLight.State;
/* Import all necessary Door control elevator modules */
import simulator.elevatormodules.*;
//import simulator.elevatormodules.Door;
//import simulator.elevatormodules.DoorClosedCanPayloadTranslator;
//import simulator.elevatormodules.DoorClosedSensor;
//import simulator.elevatormodules.DoorMotor;
//import simulator.elevatormodules.DoorOpenedCanPayloadTranslator;
//import simulator.elevatormodules.DoorOpenedSensor;
//import simulator.elevatormodules.DoorReversalCanPayloadTranslator;
//import simulator.elevatormodules.DoorReversalSensor;

import simulator.payloads.*;
import simulator.payloads.CanMailbox.*;
import simulator.payloads.DoorMotorPayload.*;
//import simulator.payloads.CanMailbox;
//import simulator.payloads.CanMailbox.ReadableCanMailbox;
//import simulator.payloads.CanMailbox.WriteableCanMailbox;
//import simulator.payloads.DoorMotorPayload;
//import simulator.payloads.DoorMotorPayload.ReadableDoorMotorPayload;
//import simulator.payloads.DoorMotorPayload.WriteableDoorMotorPayload;
//import simulator.payloads.translators.BooleanCanPayloadTranslator;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Door Control controls Door Motor and interfaces with the CANNetwork
 * @author ben
 *
 */
public class DoorControl extends Controller{

	/***************************************************************
	 * Declarations
	 **************************************************************/
	//local physical state
	private WriteableDoorMotorPayload localDoorMotor;
	
	// network interface
	// receive AtFloor from network
	private ArrayList<ReadableCanMailbox> networkAtFloorInArray = new ArrayList<ReadableCanMailbox>(10);
	// translator for AtFloor message
	private AtFloorCanPayloadTranslator mAtFloor;
	// receive DesiredFloor from network
	private ReadableCanMailbox networkDesiredFloorIn;
	// translator for DesiredFloor message
	private DesiredFloorCanPayloadTranslator mDesiredFloor;
	// receive DoorOpened from network
	private ReadableCanMailbox networkDoorOpenedIn;
	// translator for DoorOpened message
	private DoorOpenedCanPayloadTranslator mDoorOpened;
	// receive DriveSpeed from network
	private ReadableCanMailbox networkDriveSpeedIn;
	// translator for DriveSpeed message
	private DriveSpeedCanPayloadTranslator mDriveSpeed;
	// receive DoorClosed from network
	private ReadableCanMailbox networkDoorClosedIn;
	// translator for DoorOpened message
	private DoorClosedCanPayloadTranslator mDoorClosed;
	
	
	// keep track of which instance this is.
	private final Hallway hallway;
	private final Side side;
	
	//store the period for the controller
	private SimTime period;
	
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
		
        /* 
         * The log() method is inherited from the Controller class.  It takes an
         * array of objects which will be converted to strings and concatenated
         * only if the log message is actually written.  
         * 
         * For performance reasons, call with comma-separated lists, e.g.:
         *   log("object=",object);
         * Do NOT call with concatenated objects like:
         *   log("object=" + object);
         */
        log("Created Door controller with period = ", period);
        
        //initialize physical state
        localDoorMotor = DoorMotorPayload.getWriteablePayload(hallway, side);
        //register the payload with the physical interface (as in input) -- it will be updated
        //periodically when the hall call button state is modified.
        physicalInterface.sendTimeTriggered(localDoorMotor,period);
        
        //initialize network interface
        networkAtFloorInArray.add(CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + ReplicationComputer.computeReplicationId(1, Hallway.BOTH)));
        networkAtFloorInArray.add(CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + ReplicationComputer.computeReplicationId(2, Hallway.BACK)));
        networkAtFloorInArray.add(CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + ReplicationComputer.computeReplicationId(3, Hallway.FRONT)));
        networkAtFloorInArray.add(CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + ReplicationComputer.computeReplicationId(4, Hallway.FRONT)));
        networkAtFloorInArray.add(CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + ReplicationComputer.computeReplicationId(5, Hallway.FRONT)));
        networkAtFloorInArray.add(CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + ReplicationComputer.computeReplicationId(6, Hallway.FRONT)));
        networkAtFloorInArray.add(CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + ReplicationComputer.computeReplicationId(7, Hallway.BOTH)));
        networkAtFloorInArray.add(CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + ReplicationComputer.computeReplicationId(8, Hallway.FRONT)));
        
        
        for(Iterator<ReadableCanMailbox> i=networkAtFloorInArray.iterator();i.hasNext(); ) {
        	canInterface.registerTimeTriggered(i.next());
        }
        
	}
	
	public void timerExpired(Object callbackData) {
		State newState = state;
		switch (state) {
			case STATE_DOORS_CLOSED:
				//state actions for 'DOORS_CLOSED'
				localDoorMotor.set(DoorCommand.STOP);
				break;
			case STATE_DOORS_OPENING:
				//state actions for 'DOORS_OPENING'
				localDoorMotor.set(DoorCommand.OPEN);
				break;
			case STATE_DOORS_OPENED:
				//state actions for 'DOORS_OPENED'
				localDoorMotor.set(DoorCommand.STOP);
				break;
			case STATE_DOORS_NUDGE:
				//state actions for 'DOORS_OPENING'
				localDoorMotor.set(DoorCommand.NUDGE);
				break;
			default:
				throw new RuntimeException("State " + state + " was not recognized.");
		}
		
	}
	
}
