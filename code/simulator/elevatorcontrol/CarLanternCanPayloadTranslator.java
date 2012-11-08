/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file CarLanternCanPayloadTranslator.java
 */

package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;

public class CarLanternCanPayloadTranslator extends OurBooleanCanPayloadTranslator {
    public CarLanternCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload);
    }
    
    public CarLanternCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload);
    }
    
}
