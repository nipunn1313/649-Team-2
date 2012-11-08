/**
* 18-648 Fall 2012
* Nick Mazurek (nmazurek)
* Jacob Olson (jholson)
* Ben Clark (brclark)
* Nipunn Koorapati (nkoorapa)
* @file HallLightCanPayloadTranslator.java
*/

package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;

public class HallLightCanPayloadTranslator extends OurBooleanCanPayloadTranslator {
    public HallLightCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload);
    }
    
    public HallLightCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload);
    }
}
