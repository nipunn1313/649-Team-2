/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.elevatorcontrol;

import jSimPack.SimTime;
import jSimPack.SimTime.SimTimeUnit;

/**
 * This class defines constants for CAN IDs that are used throughout the simulator.
 *
 * The default values will work for early projects.  Later on, you will modify these
 * values when you create a network schedule.
 *
 * @author justinr2
 */
public class MessageDictionary {

    //controller periods
    public final static int NONE = -1;
    public final static SimTime HALL_BUTTON_CONTROL_PERIOD = new SimTime(100, SimTimeUnit.MILLISECOND);
    public final static SimTime CAR_BUTTON_CONTROL_PERIOD = new SimTime(100, SimTimeUnit.MILLISECOND);
    public final static SimTime LANTERN_CONTROL_PERIOD = new SimTime(200, SimTimeUnit.MILLISECOND);
    public final static SimTime CAR_POSITION_CONTROL_PERIOD = new SimTime(50, SimTimeUnit.MILLISECOND);
    public final static SimTime DISPATCHER_PERIOD = new SimTime(50, SimTimeUnit.MILLISECOND);
    public final static SimTime DOOR_CONTROL_PERIOD = new SimTime(10, SimTimeUnit.MILLISECOND);
    public final static SimTime DRIVE_CONTROL_PERIOD = new SimTime(10, SimTimeUnit.MILLISECOND);

    //controller message IDs
    /* Messages with 10ms period */
    public final static int DRIVE_SPEED_CAN_ID =                0x0800B500;
    public final static int DRIVE_COMMAND_CAN_ID =              0x0801B500;
    public final static int DOOR_MOTOR_COMMAND_BASE_CAN_ID =    0x0802B800;
    public final static int DOOR_REVERSAL_SENSOR_BASE_CAN_ID =  0x08036400;
    public final static int LEVELING_BASE_CAN_ID =              0x08041400;
    
    /* Messages with 50ms period */   
    public final static int EMERGENCY_BRAKE_CAN_ID =            0x08101400;
    public final static int AT_FLOOR_BASE_CAN_ID =              0x08112800;
    public final static int DESIRED_FLOOR_CAN_ID =              0x0812B600;
    public final static int DESIRED_DWELL_BASE_CAN_ID =         0x0813B600;
    public final static int CAR_POSITION_CAN_ID =               0x0814B700;
    public final static int CAR_LEVEL_POSITION_CAN_ID =         0x08153C00;

    /* Messages with 100ms period */
    public final static int HALL_LIGHT_BASE_CAN_ID =            0x0820B900;
    public final static int HALL_CALL_BASE_CAN_ID =             0x0821B900;
    public final static int DOOR_CLOSED_SENSOR_BASE_CAN_ID =    0x08225100;
    public final static int CAR_LIGHT_BASE_CAN_ID =             0x0823BA00;
    public final static int CAR_CALL_BASE_CAN_ID =              0x0824BA00;
    
    //module message IDs
    public final static int DOOR_OPEN_SENSOR_BASE_CAN_ID =      0x0840A000;
    public final static int HOISTWAY_LIMIT_BASE_CAN_ID =        0x0841B400;
    public final static int CAR_LANTERN_BASE_CAN_ID =           0x0842BB00;
    public final static int CAR_WEIGHT_CAN_ID =                 0x08437800;
    public final static int CAR_WEIGHT_ALARM_CAN_ID =           0x08448C00;

    
}
