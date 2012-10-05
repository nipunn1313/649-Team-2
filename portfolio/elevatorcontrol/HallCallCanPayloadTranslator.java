/**
* 18-648 Fall 2012
* Nick Mazurek (nmazurek)
* Jacob Olson (jholson)
* Ben Clark (brclark)
* Nipunn Koorapati (nkoorapa)
* @file HallCallCanPayloadTranslator.java
*/

package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.translators.BooleanCanPayloadTranslator;

public class HallCallCanPayloadTranslator extends BooleanCanPayloadTranslator {

    public HallCallCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload);
    }
    
    public HallCallCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload);
    }
}
