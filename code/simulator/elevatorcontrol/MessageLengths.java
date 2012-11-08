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
    
    public static final int mDriveSpeed_Speed_len = 10;
    public static final int mDriveSpeed_Speed_off = 0;
    public static final int mDriveSpeed_Direction_len = 2;
    public static final int mDriveSpeed_Direction_off = mDriveSpeed_Speed_len + mDriveSpeed_Speed_off;
    public static final int mDriveSpeed = mDriveSpeed_Speed_len + mDriveSpeed_Direction_len;
    
    
    public static final int mDrive_Speed_len = 2;
    public static final int mDrive_Speed_off = 0;
    public static final int mDrive_Direction_len = 2;
    public static final int mDrive_Direction_off = mDrive_Speed_off + mDrive_Speed_len;
    public static final int mDrive = mDrive_Speed_len + mDrive_Direction_len;
    
    public static final int mDesiredFloor_Floor_len = 4;
    public static final int mDesiredFloor_Floor_off = 0;
    public static final int mDesiredFloor_Hallway_len = 2;
    public static final int mDesiredFloor_Hallway_off = mDesiredFloor_Floor_off + mDesiredFloor_Floor_len;
    public static final int mDesiredFloor_Direction_len = 2;
    public static final int mDesiredFloor_Direction_off = mDesiredFloor_Hallway_off + mDesiredFloor_Hallway_len;
    public static final int mDesiredFloor = mDesiredFloor_Floor_len + mDesiredFloor_Hallway_len + mDesiredFloor_Direction_len;
    
    public static final int mDoorMotorCommand = 2;
    public static final int mCarPosition = 4;
    public static final int mCarLevelPosition = 32;
    public static final int mDesiredDwell = 16;
}
