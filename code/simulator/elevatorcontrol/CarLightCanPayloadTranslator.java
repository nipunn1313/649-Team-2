/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file CarCallPayload.java
 */

package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;

public class CarLightCanPayloadTranslator extends OurBooleanCanPayloadTranslator {

    public CarLightCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload);
    }
    
    public CarLightCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload);
    }

}
