/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file OurCarLevelPositionCanPayloadTranslator.java
 */

package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;

public class OurCarLevelPositionCanPayloadTranslator extends
        OurIntegerCanPayloadTranslator {

    public OurCarLevelPositionCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload, MessageLengths.mCarLevelPosition);
    }

    public OurCarLevelPositionCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload, MessageLengths.mCarLevelPosition);
    }
    
    public int getPosition() {
        return getValue();
    }

    public void setPosition(int position) {
        setValue(position);
    }
}
