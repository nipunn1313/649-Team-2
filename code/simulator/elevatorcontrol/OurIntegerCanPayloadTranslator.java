/**
 * 18649 <Fall 2012>
 * Nipunn Koorapati (nkoorapa)
 * Jacob Olson (jholson)
 * Benet Clark (brclark)
 * Nick Mazurek (nmazurek)
 * @file OurIntegerCanPayloadTranslator.java
 */

package simulator.elevatorcontrol;

import java.util.BitSet;
import simulator.payloads.CanMailbox.ReadableCanMailbox;
import simulator.payloads.CanMailbox.WriteableCanMailbox;
import simulator.payloads.translators.CanPayloadTranslator;

/**
 * This class takes a single integer or boolean value and translates it to a
 * n byte CanMailbox
 * @author nmazurek
 */
public class OurIntegerCanPayloadTranslator extends CanPayloadTranslator {

    // Stores the number of bits in the translator
    private int numBits;
    
    /**
     * Constructor for use with WriteableCanMailbox objects
     * @param payload
     * @param numBits: Rounded up to the nearest Byte
     */
    public OurIntegerCanPayloadTranslator(WriteableCanMailbox payload, int numBits) {
        super(payload, (numBits + 7)/8);
        this.numBits = numBits;
    }

    /**
     * Constructor for use with ReadableCanMailbox objects
     * @param payload
     * @param numBits: Rounded up to the nearest Byte for the translator
     */

    public OurIntegerCanPayloadTranslator(ReadableCanMailbox payload, int numBits) {
        super(payload, (numBits + 7)/8);
        this.numBits = numBits;
    }

    
    //required for reflection
    public void set(int value) {
        setValue(value);
    }
    
    public void setValue(int value) {
        BitSet b = new BitSet();
        addUnsignedIntToBitset(b, value, 0, numBits);
        setMessagePayload(b, getByteSize());
    }
    
    public int getValue() {
        return getUnsignedIntFromBitset(getMessagePayload(), 0, numBits);
    }
    
    @Override
    public String payloadToString() {
        return "0x" + Integer.toString(getValue(),numBits);
    }
}
