/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file DoorMotorCanPayloadTranslator.java
 */
package simulator.elevatorcontrol;

import java.util.BitSet;
import simulator.framework.DoorCommand;
import simulator.framework.Hallway;
import simulator.framework.ReplicationComputer;
import simulator.framework.Side;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.translators.CanPayloadTranslator;

public class DoorMotorCanPayloadTranslator extends CanPayloadTranslator {
    /**
     * CAN payload translator for Door motor message
     * @param p  CAN payload object whos message is interpreted by this translator
     */
    public DoorMotorCanPayloadTranslator(WriteableCanMailbox p, Hallway hallway, Side side) {
        super(p, (MessageLengths.mDoorMotorCommand + 7)/8,
                MessageDictionary.DOOR_MOTOR_COMMAND_BASE_CAN_ID + ReplicationComputer.computeReplicationId(hallway, side));
    }

    /**
     * CAN payload translator for Door motor message
     * @param p  CAN payload object whose message is interpreted by this translator
     */
    public DoorMotorCanPayloadTranslator(ReadableCanMailbox p, Hallway hallway, Side side) {
        super(p, (MessageLengths.mDoorMotorCommand + 7)/8,
                MessageDictionary.DOOR_MOTOR_COMMAND_BASE_CAN_ID + ReplicationComputer.computeReplicationId(hallway, side));
    }

    public DoorCommand getDoorCommand() {
        int val = getUnsignedIntFromBitset(getMessagePayload(), 0, MessageLengths.mDoorMotorCommand);
        for (DoorCommand d : DoorCommand.values()) {
            if (d.ordinal() == val) {
                return d;
            }
        }
        throw new RuntimeException("Unrecognized Door Command Value " + val);
    }

    public void setDoorCommand(DoorCommand command) {
        BitSet b = getMessagePayload();
        addUnsignedIntToBitset(b, command.ordinal(), 0, MessageLengths.mDoorMotorCommand);
        setMessagePayload(b, getByteSize());
    }

    @Override
    public String payloadToString() {
        return "DoorMotor: command=" + getDoorCommand();
    }
}
