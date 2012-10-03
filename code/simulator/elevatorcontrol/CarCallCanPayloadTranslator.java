package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.translators.BooleanCanPayloadTranslator;

public class CarCallCanPayloadTranslator extends BooleanCanPayloadTranslator {

    public CarCallCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload);
    }
    
    public CarCallCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload);
    }

}
