package simulator.elevatorcontrol;

import java.util.BitSet;
import simulator.framework.Direction;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.translators.CanPayloadTranslator;


/**
 * Can payload translator for the drive speed sensor, which includes a speed value and a direction.
 * 
 * DriveSpeed Can Payload Translator is the same as DriveCommand Can
 * Payload Translator. DriveSpeed is the sensor whereas DriveCommand is the
 * actuator.
 * 
 * @author Ben Clark, Modified by Nick Mazurek
 */
public class DriveSpeedCanPayloadTranslator extends CanPayloadTranslator {

    // CAN can only send Integers, but it's possible that the speed of 
    // the elevator should be slow enough that truncation could be a problem, so before
    // sending the message over the wire, multiply the double by 100 to preserve the value 
    // to the nearest hundredth of a m/s.  Then divide upon receiving to get the double back.
    private static final int SCALE_VAL = 100;
    
    public DriveSpeedCanPayloadTranslator(WriteableCanMailbox p) {
      super(p, 2, MessageDictionary.DRIVE_SPEED_CAN_ID);
    }
	
    public DriveSpeedCanPayloadTranslator(ReadableCanMailbox p) {
      super(p, 2, MessageDictionary.DRIVE_SPEED_CAN_ID);
    }
	
    /**
     * This method is required for setting values by reflection in the
     * MessageInjector.  The order of parameters in .mf files should match the
     * signature of this method.
     * All translators must have a set() method with the signature that contains
     * all the parameter values.
     *
     * @param speed
     * @param dir
     */
    public void set(double speed, Direction dir) {
        setSpeed(speed);
        setDirection(dir);
    }
    
    public void setSpeed(double speed) {
        BitSet b = getMessagePayload();
        addIntToBitset(b, (int)(speed*SCALE_VAL), 0, 16);
        setMessagePayload(b, getByteSize());
    }

    public double getSpeed() {
        int val = getIntFromBitset(getMessagePayload(), 0, 16);
        return ((double)val)/SCALE_VAL;
    }

    public int getScaledSpeed() {
        return getIntFromBitset(getMessagePayload(), 0, 16);
    }
    
    public void setDirection(Direction dir) {
        BitSet b = getMessagePayload();
        addIntToBitset(b, dir.ordinal(), 16, 16);
        setMessagePayload(b, getByteSize());
    }

    public Direction getDirection() {
        int val = getIntFromBitset(getMessagePayload(), 16, 16);
        for (Direction d : Direction.values()) {
            if (d.ordinal() == val) {
                return d;
            }
        }
        throw new RuntimeException("Unrecognized Direction Value " + val);
    }

    @Override
    public String payloadToString() {
        return "DriveSpeed:  speed=" + getSpeed() + " direction=" + getDirection();
    }
}
