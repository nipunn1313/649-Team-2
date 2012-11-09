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
    private boolean signed;
    
    /**
     * Constructor for use with WriteableCanMailbox objects
     * @param payload
     * @param numBits: Rounded up to the nearest Byte
     */
    public OurIntegerCanPayloadTranslator(WriteableCanMailbox payload, int numBits, boolean signed) {
        super(payload, (numBits + 7)/8);
        this.numBits = numBits;
        this.signed = signed;
    }

    /**
     * Constructor for use with ReadableCanMailbox objects
     * @param payload
     * @param numBits: Rounded up to the nearest Byte for the translator
     */
    public OurIntegerCanPayloadTranslator(ReadableCanMailbox payload, int numBits, boolean signed) {
        super(payload, (numBits + 7)/8);
        this.numBits = numBits;
        this.signed = signed;
    }

    
    //required for reflection
    public void set(int value) {
        setValue(value);
    }
    
    public void setValue(int value) {
        BitSet b = new BitSet();
        if (signed)
            addIntToBitset(b, value, 0, numBits);
        else
            addUnsignedIntToBitset(b, value, 0, numBits);
        setMessagePayload(b, getByteSize());
    }
    
    public int getValue() {
        return signed ? getIntFromBitset(getMessagePayload(), 0, numBits) :
                getUnsignedIntFromBitset(getMessagePayload(), 0, numBits);
    }
    
    @Override
    public String payloadToString() {
        return "0x" + Integer.toString(getValue(),numBits);
    }
}
