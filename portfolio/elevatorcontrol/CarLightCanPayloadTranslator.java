/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file CarLightCanPayloadTranslator.java
 */

package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.translators.BooleanCanPayloadTranslator;

public class CarLightCanPayloadTranslator extends BooleanCanPayloadTranslator {

    public CarLightCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload);
    }
    
    public CarLightCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload);
    }

}
