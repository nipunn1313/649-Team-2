/**
 * 18-648 Fall 2012
 * Nick Mazurek (nmazurek)
 * Jacob Olson (jholson)
 * Ben Clark (brclark)
 * Nipunn Koorapati (nkoorapa)
 * @file MessageLengths.java
 */
package simulator.elevatorcontrol;

/**
 * @class MessageLengths
 * @author nick
 *
 */
public class MessageLengths {

    /**
     * Defines the bit lengths of the message content for the corresponding
     * messages.
     */
    public static final int mDriveSpeed = 12;
    public static final int mDrive = 4;
    public static final int mDoorMotorCommand = 2;
    public static final int mDesiredFloor = 8;
    public static final int mCarPosition = 4;
    public static final int mCarLevelPosition = 16;
    public static final int mDesiredDwell = 16;
}
