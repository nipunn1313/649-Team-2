package simulator.elevatorcontrol;

import java.util.BitSet;
import simulator.framework.Direction;
import simulator.framework.Hallway;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.translators.CanPayloadTranslator;

/**
 * This is an example CAN payload translator for desired floor messages.  It
 * takes three data fields (floor, hall, direction) and packages them into
 * a bit-level representation of the message.
 * 
 * CanPayloadTranslator provides a lot of utility classes.  See the javadoc for
 * that class for more details.
 * 
 * @author Justin Ray
 */
public class DesiredFloorCanPayloadTranslator extends CanPayloadTranslator {

    /**
     * Constructor for WriteableCanMailbox.  You should always implement both a 
     * Writeable and Readable constructor so the same translator can be used for
     * both objects
     * @param payload
     */
    public DesiredFloorCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload, (MessageLengths.mDesiredFloor + 7)/8, 
                MessageDictionary.DESIRED_FLOOR_CAN_ID);
    }

    /**
     * Constructor for ReadableCanMailbox.  You should always implement both a 
     * Writeable and Readable constructor so the same translator can be used for
     * both objects
     * @param payload
     */
    public DesiredFloorCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload, (MessageLengths.mDesiredFloor + 7)/8, 
                MessageDictionary.DESIRED_FLOOR_CAN_ID);
    }
    
    /**
     * This method is required for setting values by reflection in the
     * MessageInjector.  The order of parameters in .mf files should match the
     * signature of this method.
     * All translators must have a set() method with the signature that contains
     * all the parameter values.
     *
     * @param floor
     * @param dir
     * @param hallway
     */
    public void set(int floor, Direction dir, Hallway hallway) {
        setFloor(floor);
        setDirection(dir);
        setHallway(hallway);
    }
    
    /**
     * Similar to the other set method, but the Hallway/Dir field order reversed.
     * @param floor
     * @param hallway
     * @param dir
     */
    public void set(int floor, Hallway hallway, Direction dir) {
        setFloor(floor);
        setDirection(dir);
        setHallway(hallway);
    }


    /**
     * Set the floor for mDesiredFloor into the loweest 32 bits of the payload
     * @param floor
     */
    public void setFloor(int floor) {
        BitSet b = getMessagePayload();
        addUnsignedIntToBitset(b, floor, MessageLengths.mDesiredFloor_Floor_off,
                MessageLengths.mDesiredFloor_Floor_len);
        setMessagePayload(b, getByteSize());
    }

    /**
     *
     * @return the floor value from the can message payload
     */
    public int getFloor() {
        return getUnsignedIntFromBitset(getMessagePayload(), MessageLengths.mDesiredFloor_Floor_off,
                MessageLengths.mDesiredFloor_Floor_len);
    }

    /**
     * Set the direction for mDesiredFloor in bits 32-47 of the can payload
     * @param dir
     */
    public void setDirection(Direction dir) {
        BitSet b = getMessagePayload();
        addUnsignedIntToBitset(b, dir.ordinal(), MessageLengths.mDesiredFloor_Direction_off,
                MessageLengths.mDesiredFloor_Direction_len);
        setMessagePayload(b, getByteSize());
    }

    /**
     * 
     * @return the direction value from the can payload
     */
    public Direction getDirection() {
        int val = getUnsignedIntFromBitset(getMessagePayload(), MessageLengths.mDesiredFloor_Direction_off,
                MessageLengths.mDesiredFloor_Direction_len);
        for (Direction d : Direction.values()) {
            if (d.ordinal() == val) {
                return d;
            }
        }
        throw new RuntimeException("Unrecognized Direction Value " + val);
    }

    /**
     * Set the hallway for mDesiredFloor in bits 48-63 of the can payload
     * @param hallway
     */
    public void setHallway(Hallway hallway) {
        BitSet b = getMessagePayload();
        addUnsignedIntToBitset(b, hallway.ordinal(), MessageLengths.mDesiredFloor_Hallway_off,
                MessageLengths.mDesiredFloor_Hallway_len);
        setMessagePayload(b, getByteSize());
    }

    /**
     * 
     * @return the hallway value from the CAN payload.
     */
    public Hallway getHallway() {
        int val = getUnsignedIntFromBitset(getMessagePayload(), MessageLengths.mDesiredFloor_Hallway_off,
                MessageLengths.mDesiredFloor_Hallway_len);
        for (Hallway h : Hallway.values()) {
            if (h.ordinal() == val) {
                return h;
            }
        }
        throw new RuntimeException("Unrecognized Hallway Value " + val);
    }

    /**
     * Implement a printing method for the translator.
     * @return
     */
    @Override
    public String payloadToString() {
        return "DesiredFloor = " + getFloor() + ", DesiredDirection = " + getDirection() + ", DesiredHallway = " + getHallway();
    }
}
