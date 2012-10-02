package simulator.elevatorcontrol;

import jSimPack.SimTime;
import simulator.elevatorcontrol.Utility.AtFloorArray;
import simulator.elevatorcontrol.Utility.DoorClosedArray;
import simulator.elevatormodules.CarWeightCanPayloadTranslator;
import simulator.elevatormodules.DoorClosedCanPayloadTranslator;
import simulator.elevatormodules.DoorOpenedCanPayloadTranslator;
import simulator.framework.Controller;
import simulator.framework.DoorCommand;
import simulator.framework.Hallway;
import simulator.framework.ReplicationComputer;
import simulator.framework.Side;
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
	//receive CarWeight from network
	private ReadableCanMailbox networkCarWeightIn;
	// translator for CarWeight message
	private CarWeightCanPayloadTranslator mCarWeight;
	// send DoorMotor to network
	private WriteableCanMailbox networkDoorMotorOut;
	// translator for DoorMotor message
	private DoorMotorCanPayloadTranslator mDoorMotor;
	
	
	
	// receive DesiredFloor from network
//	private ReadableCanMailbox networkDesiredFloorIn;
	// translator for DesiredFloor message
//	private DesiredFloorCanPayloadTranslator mDesiredFloor;

	
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
        
        // Register mCarWeight
        networkCarWeightIn = CanMailbox.getReadableCanMailbox(MessageDictionary.CAR_WEIGHT_CAN_ID);
        mCarWeight = new CarWeightCanPayloadTranslator(networkCarWeightIn);
        canInterface.registerTimeTriggered(networkCarWeightIn);
        
        
	}
	
	//@Override
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
