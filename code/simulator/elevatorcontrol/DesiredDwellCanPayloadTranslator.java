/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file DesiredDwellCanPayloadTranslator.java
 */
package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.translators.IntegerCanPayloadTranslator;


public class DesiredDwellCanPayloadTranslator extends IntegerCanPayloadTranslator {

    /**
     * CAN payload translator for Desired dwell message
     * @param p  CAN payload object whos message is interpreted by this translator
     */
    public DesiredDwellCanPayloadTranslator(WriteableCanMailbox p) {
        super(p);
    }

    /**
     * CAN payload translator for Desired dwell message
     * @param p  CAN payload object whose message is interpreted by this translator
     */
    public DesiredDwellCanPayloadTranslator(ReadableCanMailbox p) {
        super(p);
    }

    // TODO: THIS IS WRONG. SHOULD RETURN A SIMTIME
    public int getDwell() {
        return getValue();
    }

    public void setDwell(int dwell) {
        setValue(dwell);
    }
}
