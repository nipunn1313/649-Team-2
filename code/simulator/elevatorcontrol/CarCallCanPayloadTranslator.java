package simulator.elevatorcontrol;

import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;

public class CarCallCanPayloadTranslator extends OurBooleanCanPayloadTranslator {

    public CarCallCanPayloadTranslator(ReadableCanMailbox payload) {
        super(payload);
    }
    
    public CarCallCanPayloadTranslator(WriteableCanMailbox payload) {
        super(payload);
    }

}
