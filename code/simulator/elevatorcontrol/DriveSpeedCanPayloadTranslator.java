package simulator.elevatorcontrol;

import java.util.BitSet;
import simulator.framework.Direction;
import simulator.framework.Speed;
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
 * @author Ben Clark
 */
public class DriveSpeedCanPayloadTranslator extends CanPayloadTranslator {

    public DriveSpeedCanPayloadTranslator(WriteableCanMailbox p) {
      super(p, 8, MessageDictionary.DRIVE_SPEED_CAN_ID);
    }
	
    public DriveSpeedCanPayloadTranslator(ReadableCanMailbox p) {
      super(p, 8, MessageDictionary.DRIVE_SPEED_CAN_ID);
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
    public void set(Speed speed, Direction dir) {
        setSpeed(speed);
        setDirection(dir);
    }
    
    public void setSpeed(Speed speed) {
        BitSet b = getMessagePayload();
        addIntToBitset(b, speed.ordinal(), 0, 32);
        setMessagePayload(b, getByteSize());
    }

    public Speed getSpeed() {
        int val = getIntFromBitset(getMessagePayload(), 0, 32);
        for (Speed s : Speed.values()) {
            if (s.ordinal() == val) {
                return s;
            }
        }
        throw new RuntimeException("Unrecognized Speed Value " + val);
    }

    public void setDirection(Direction dir) {
        BitSet b = getMessagePayload();
        addIntToBitset(b, dir.ordinal(), 32, 32);
        setMessagePayload(b, getByteSize());
    }

    public Direction getDirection() {
        int val = getIntFromBitset(getMessagePayload(), 32, 32);
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
