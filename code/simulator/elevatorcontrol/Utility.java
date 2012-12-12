/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.elevatorcontrol;

import java.util.HashMap;
import simulator.elevatormodules.AtFloorCanPayloadTranslator;
import simulator.elevatormodules.DoorClosedCanPayloadTranslator;
import simulator.elevatormodules.DoorOpenedCanPayloadTranslator;
import simulator.elevatormodules.DoorReversalCanPayloadTranslator;
import simulator.elevatormodules.DriveObject;
import simulator.payloads.CANNetwork;
import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.framework.Harness;
import simulator.framework.PassengerController.Passenger;
import simulator.framework.ReplicationComputer;
import simulator.framework.Side;
import simulator.payloads.CanMailbox;
import simulator.payloads.CanMailbox.ReadableCanMailbox;

/**
 * This class provides some example utility classes that might be useful in more
 * than one spot.  It is okay to create new classes (or modify the ones given
 * below), but you may not use utility classes in such a way that they constitute
 * a communication channel between controllers.
 *
 * @author justinr2
 */
public class Utility {

    public static class DoorClosedArray {

        HashMap<Integer, DoorClosedCanPayloadTranslator> translatorArray = new HashMap<Integer, DoorClosedCanPayloadTranslator>();
        
        public DoorClosedArray(CANNetwork.CanConnection conn) {
            for (Hallway hallway : Hallway.replicationValues) {
                for (Side s : Side.values()) {
                    int index = ReplicationComputer.computeReplicationId(hallway, s);
                    ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.DOOR_CLOSED_SENSOR_BASE_CAN_ID + index);
                    DoorClosedCanPayloadTranslator t = new DoorClosedCanPayloadTranslator(m, hallway, s);
                    conn.registerTimeTriggered(m);
                    translatorArray.put(index, t);
                }
            }
        }

        public boolean getClosed(Hallway hallway, Side s) {
            return translatorArray.get(ReplicationComputer.computeReplicationId(hallway, s)).getValue();
        }
        
        public boolean getBothClosed(Hallway h) {
            return getClosed(h, Side.LEFT) && getClosed(h, Side.RIGHT); 
        }
        
        public boolean getAllClosed() {
            return getBothClosed(Hallway.FRONT) && getBothClosed(Hallway.BACK);
        }
    }

    public static class DoorOpenedArray {

        HashMap<Integer, DoorOpenedCanPayloadTranslator> translatorArray = new HashMap<Integer, DoorOpenedCanPayloadTranslator>();

        public DoorOpenedArray(CANNetwork.CanConnection conn) {
            for (Hallway hallway : Hallway.replicationValues) {
                for (Side s : Side.values()) {
                    int index = ReplicationComputer.computeReplicationId(hallway, s);
                    ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.DOOR_OPEN_SENSOR_BASE_CAN_ID + index);
                    DoorOpenedCanPayloadTranslator t = new DoorOpenedCanPayloadTranslator(m, hallway, s);
                    conn.registerTimeTriggered(m);
                    translatorArray.put(index, t);
                }
            }
        }
        
        public boolean getOpened(Hallway hallway, Side s) {
            return translatorArray.get(ReplicationComputer.computeReplicationId(hallway, s)).getValue();
        }

        public boolean getBothOpened(Hallway h) {
            return getOpened(h, Side.LEFT) && getOpened(h, Side.RIGHT);
        }
        
        public boolean getAllOpened() {
            return getBothOpened(Hallway.FRONT) && getBothOpened(Hallway.BACK); 
        }
    }

    public static class DoorReversalArray {

        HashMap<Integer, DoorReversalCanPayloadTranslator> translatorArray = new HashMap<Integer, DoorReversalCanPayloadTranslator>();
        public final Hallway hallway;

        public DoorReversalArray(Hallway hallway, CANNetwork.CanConnection conn) {
            this.hallway = hallway;
            for (Side s : Side.values()) {
                int index = ReplicationComputer.computeReplicationId(hallway, s);
                ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.DOOR_REVERSAL_SENSOR_BASE_CAN_ID + index);
                DoorReversalCanPayloadTranslator t = new DoorReversalCanPayloadTranslator(m, hallway, s);
                conn.registerTimeTriggered(m);
                translatorArray.put(index, t);
            }
        }

        public boolean getAnyReversal() {
            return translatorArray.get(ReplicationComputer.computeReplicationId(hallway, Side.LEFT)).getValue() ||
                    translatorArray.get(ReplicationComputer.computeReplicationId(hallway, Side.RIGHT)).getValue();
        }
    }

    public static class AtFloorArray {

        public HashMap<Integer, AtFloorCanPayloadTranslator> networkAtFloorsTranslators = new HashMap<Integer, AtFloorCanPayloadTranslator>();
        public final int numFloors = Elevator.numFloors;

        public AtFloorArray(CANNetwork.CanConnection conn) {
            for (int i = 0; i < numFloors; i++) {
                int floor = i + 1;
                for (Hallway h : Hallway.replicationValues) {
                    int index = ReplicationComputer.computeReplicationId(floor, h);
                    ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.AT_FLOOR_BASE_CAN_ID + index);
                    AtFloorCanPayloadTranslator t = new AtFloorCanPayloadTranslator(m, floor, h);
                    conn.registerTimeTriggered(m);
                    networkAtFloorsTranslators.put(index, t);
                }
            }
        }

        public boolean isAtFloor(int floor, Hallway hallway) {
            return networkAtFloorsTranslators.get(ReplicationComputer.computeReplicationId(floor, hallway)).getValue();
        }

        public int getCurrentFloor() {
            int retval = MessageDictionary.NONE;
            for (int i = 0; i < numFloors; i++) {
                int floor = i + 1;
                for (Hallway h : Hallway.replicationValues) {
                    int index = ReplicationComputer.computeReplicationId(floor, h);
                    AtFloorCanPayloadTranslator t = networkAtFloorsTranslators.get(index);
                    if (t.getValue()) {
                        if (retval == MessageDictionary.NONE) {
                            //this is the first true atFloor
                            retval = floor;
                        } else if (retval != floor) {
                            //found a second floor that is different from the first one
                            throw new RuntimeException("AtFloor is true for more than one floor at " + Harness.getTime());
                        }
                    }
                }
            }
            return retval;
        }
    }

    public static class HallCallArray {
        public HashMap<Integer, HallCallCanPayloadTranslator> networkHallCallsTranslators = new HashMap<Integer, HallCallCanPayloadTranslator>();
        public final int numFloors = Elevator.numFloors;

        public HallCallArray(CANNetwork.CanConnection conn) {
            for (int f = 1; f <= numFloors; f++) {
                for (Hallway b : Hallway.replicationValues) {
                    for (Direction d : Direction.replicationValues) {
                        int index = ReplicationComputer.computeReplicationId(f, b, d);
                        ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.HALL_CALL_BASE_CAN_ID + index);
                        HallCallCanPayloadTranslator t = new HallCallCanPayloadTranslator(m);
                        conn.registerTimeTriggered(m);
                        networkHallCallsTranslators.put(index, t);
                    }
                }
            }
        }
        
        public boolean getHallCall(int f, Hallway b, Direction d) {
            return networkHallCallsTranslators.get(ReplicationComputer.computeReplicationId(f,b,d)).getValue();
        }
        
        public boolean getAnyHallCallOnFloor(int f) {
            return (getHallCall(f,Hallway.FRONT,Direction.UP) ||
                    getHallCall(f,Hallway.BACK, Direction.UP) ||
                    getHallCall(f,Hallway.FRONT, Direction.DOWN) ||
                    getHallCall(f,Hallway.BACK, Direction.DOWN));
        }
    }

    public static class CarCallArray {
        public HashMap<Integer, CarCallCanPayloadTranslator> networkCarCallsTranslators = new HashMap<Integer, CarCallCanPayloadTranslator>();
        public final int numFloors = Elevator.numFloors;

        public CarCallArray(CANNetwork.CanConnection conn) {
            for (int f = 1; f <= numFloors; f++) {
                for (Hallway b : Hallway.replicationValues) {
                    int index = ReplicationComputer.computeReplicationId(f, b);
                    ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.CAR_CALL_BASE_CAN_ID + index);
                    CarCallCanPayloadTranslator t = new CarCallCanPayloadTranslator(m);
                    conn.registerTimeTriggered(m);
                    networkCarCallsTranslators.put(index, t);
                }
            }
        }
        
        public boolean getCarCall(int f, Hallway b) {
            return networkCarCallsTranslators.get(ReplicationComputer.computeReplicationId(f,b)).getValue();
        }
        
        public boolean getAnyCarCallForFloor(int f) {
            return (getCarCall(f,Hallway.FRONT) ||
                    getCarCall(f,Hallway.BACK));
        }
    }
    
    public static class CarLanternArray {
        public HashMap<Direction, CarLanternCanPayloadTranslator> networkCarLanternTranslators =
                new HashMap<Direction, CarLanternCanPayloadTranslator>();
        
        public CarLanternArray(CANNetwork.CanConnection conn) {
            for (Direction d : Direction.replicationValues) {
                int index = ReplicationComputer.computeReplicationId(d);
                ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.CAR_LANTERN_BASE_CAN_ID + index);
                CarLanternCanPayloadTranslator t = new CarLanternCanPayloadTranslator(m);
                conn.registerTimeTriggered(m);
                networkCarLanternTranslators.put(d, t);
            }
        }
        
        public boolean areBothOff() {
            boolean up = networkCarLanternTranslators.get(Direction.UP).getValue();
            boolean down = networkCarLanternTranslators.get(Direction.DOWN).getValue();
            return !(up || down);
        }
    }

    public static Hallway getLandings(int f) {
        boolean frontHallway = Elevator.hasLanding(f, Hallway.FRONT);
        boolean backHallway = Elevator.hasLanding(f, Hallway.BACK);

        if (frontHallway && backHallway) {
            return Hallway.BOTH;
        } else if (frontHallway) {
            return Hallway.FRONT;
        } else if (backHallway) {
            return Hallway.BACK;
        } else {
            return Hallway.NONE;
        }
    }
    
    
    public static boolean reachedCommitPointNoSlack(int f, int carLevelPositionMM,
            double driveSpeed, Direction driveDirection) {
        return reachedCommitPointCommon(f, carLevelPositionMM, driveSpeed, driveDirection, false);
    }
    
    public static boolean reachedCommitPoint(int f, int carLevelPositionMM, 
            double driveSpeed, Direction driveDirection) {
        
        return reachedCommitPointCommon(f, carLevelPositionMM, driveSpeed, driveDirection, true);
    }
    
    
    /* Helper for determining CommitPoint */
    private static boolean reachedCommitPointCommon(int f, int carLevelPositionMM, 
            double driveSpeed, Direction driveDirection, boolean withSlack) {
        
        if (driveSpeed == 0.0)
            return false;

        // Half meter slack on the commit point
        double slackFactor = withSlack ? 1.8 : 1.0;
        
        double floorHeight = (f - 1) * Elevator.DISTANCE_BETWEEN_FLOORS;
        double carLevelPosition = (double) carLevelPositionMM / 1000;
        
        double distanceToFloor = driveDirection == Direction.UP ?
                floorHeight - carLevelPosition :
                carLevelPosition - floorHeight;
        
        // Apply some physics. t = v / a. and d = .5 * a * t^2
        double timeToStop = driveSpeed / DriveObject.Acceleration;
        double stoppingDistance = .5 * DriveObject.Acceleration * timeToStop * timeToStop;
        
        // If we're going fast, provide slack. If we're going <= slow, no slack
        // DriveControl won't care, since it only uses commit point when going fast
        // CarPositionControl cares, since commit point -> switching floors
        return distanceToFloor <= stoppingDistance * (driveSpeed < DriveObject.SlowSpeed ? 1.0 : slackFactor);
    }
    
    public static int nextReachableFloorWhenMoving(int carLevelPositionMM,
            double driveSpeed, Direction driveDirection) {
        switch (driveDirection) {
            case UP: return nextReachableFloor(1, carLevelPositionMM, driveSpeed, driveDirection, true);
            case DOWN: return nextReachableFloor(8, carLevelPositionMM, driveSpeed, driveDirection, true);
            case STOP: 
            default:
                throw new RuntimeException("Not moving when calling nextReachableFloorWhileMoving");
        }
    }
    
    /* For the CarPositionControl and DriveControl */
    public static int nextReachableFloor(int currentFloor,
            int carLevelPositionMM, double driveSpeed, 
            Direction driveDirection, boolean withSlack) {
        
        // If we're slower than leveling, we're basically stopped anyway.
        if (driveSpeed <= DriveObject.LevelingSpeed)
            return currentFloor;
        
        switch (driveDirection) {
            case UP:
                for (int f = currentFloor; f <= Elevator.numFloors; f++) {
                    if (!Utility.reachedCommitPointCommon(f, carLevelPositionMM, driveSpeed, driveDirection, withSlack))
                        return f;
                }
                throw new RuntimeException("We're going to launch out the roof!");
            case DOWN:
                for (int f = currentFloor; f >= 0; f--) {
                    if (!Utility.reachedCommitPointCommon(f, carLevelPositionMM, driveSpeed, driveDirection, withSlack))
                        return f;
                }
                throw new RuntimeException("We're going to crash through the basement");
            case STOP:
                return currentFloor;
            default:
                throw new RuntimeException("driveDirection holds an unknown value??? " + driveDirection);
        }
    }
    
    /* For dispatcher to determine next floor WHILE moving in a direction */
    public static DesiredFloor getNextFloorMoving(int carLevelPositionMM, double driveSpeed,
            Direction driveDirection, CarCallArray carCalls, HallCallArray hallCalls,
            Direction targetDirection, int currentFloor, int carWeight) {
        
        DesiredFloor desired = null;
        
        if (driveDirection == Direction.UP) {
            desired = findDesiredAbove(carLevelPositionMM, driveSpeed, driveDirection, hallCalls, carCalls, carWeight);
        } else if (driveDirection == Direction.DOWN) {
            desired = findDesiredBelow(carLevelPositionMM, driveSpeed, driveDirection, hallCalls, carCalls, carWeight);
        }
        
        return desired;
    }
    
    public static DesiredFloor getNextFloorDoorsClosed(int carLevelPositionMM, double driveSpeed,
            Direction driveDirection, CarCallArray carCalls, HallCallArray hallCalls,
            int currentFloor, int targetFloor, int carWeight, Direction promisedDirection) { 
        
        Direction desiredDirection;
        if (promisedDirection == Direction.STOP)
            desiredDirection = Direction.UP;
        else desiredDirection = promisedDirection;
        
        // Basically, do the same thing you do when the doors are open, except you can't rely on
        // mDesiredFloor.d, so you compute based on currentFloor and mDesiredFloor.f
        return getNextFloorDoorsOpen(carLevelPositionMM, driveSpeed, driveDirection, carCalls,
                hallCalls, desiredDirection, currentFloor, carWeight);
    }
    
    public static DesiredFloor getNextFloorDoorsOpen(int carLevelPositionMM, double driveSpeed,
            Direction driveDirection, CarCallArray carCalls, HallCallArray hallCalls,
            Direction targetDirection, int currentFloor, int carWeight) {
        DesiredFloor desired;
        
        if (targetDirection == Direction.STOP)
            targetDirection = Direction.UP;
        
        if (targetDirection == Direction.UP) {
            if((desired = findDesiredAbove(carLevelPositionMM, driveSpeed, driveDirection, hallCalls, carCalls, carWeight)) != null)
                return desired;
            if ((desired = findDesiredBelow(carLevelPositionMM, driveSpeed, driveDirection, hallCalls, carCalls, carWeight)) != null)
                return desired;
        }

        if (targetDirection == Direction.DOWN) {
            if((desired = findDesiredBelow(carLevelPositionMM, driveSpeed, driveDirection, hallCalls, carCalls, carWeight)) != null)
                return desired;
            if ((desired = findDesiredAbove(carLevelPositionMM, driveSpeed, driveDirection, hallCalls, carCalls, carWeight)) != null)
                return desired;
        }
        
        if (currentFloor == MessageDictionary.NONE)
            currentFloor = 1;
        
        return new DesiredFloor(currentFloor, Hallway.NONE, Direction.STOP);
    }
    
    private static DesiredFloor findDesiredAbove(int carLevelPositionMM, double driveSpeed, Direction driveDirection,
            HallCallArray hallCalls, CarCallArray carCalls, int carWeight) {

        boolean carFull = carWeight + Passenger.weight > Elevator.MaxCarCapacity;
        
        /* Add 100 for 10cm granularity */
        int currentFloor = 1 + (int) (((100.0 + (double)carLevelPositionMM) / 1000.0) / Elevator.DISTANCE_BETWEEN_FLOORS);
        for (int f = currentFloor; f <= Elevator.numFloors; f++) {
               if (!Utility.reachedCommitPoint(f, carLevelPositionMM, driveSpeed, driveDirection)) {
                   if (!carFull) {
                       boolean frontHallCall = hallCalls.getHallCall(f,  Hallway.FRONT,  Direction.UP);
                       boolean backHallCall = hallCalls.getHallCall(f, Hallway.BACK, Direction.UP);
                       if(frontHallCall || backHallCall) {
                           Hallway desiredFloorHallway = frontHallCall && backHallCall ? Hallway.BOTH :
                                                         (frontHallCall ? Hallway.FRONT : Hallway.BACK);
                           return new DesiredFloor(f, desiredFloorHallway, Direction.UP);
                       }
                   }
                   
                   boolean frontCarCall = carCalls.getCarCall(f, Hallway.FRONT);
                   boolean backCarCall = carCalls.getCarCall(f, Hallway.BACK);
                   if(frontCarCall || backCarCall) {
                       
                       Hallway desiredFloorHallway = frontCarCall && backCarCall ? Hallway.BOTH : 
                                                     (frontCarCall ? Hallway.FRONT : Hallway.BACK);
                       for(int f2 = f+1;f2 <= Elevator.numFloors; f2++) {
                           if (hallCalls.getAnyHallCallOnFloor(f2) ||
                                   carCalls.getAnyCarCallForFloor(f2))
                               return new DesiredFloor(f, desiredFloorHallway, Direction.UP);
                       }
                       for(int f2 = f-1;f2 > 0; f2--) {
                           if (hallCalls.getAnyHallCallOnFloor(f2) ||
                                   carCalls.getAnyCarCallForFloor(f2))
                               return new DesiredFloor(f, desiredFloorHallway, Direction.DOWN);
                       }
                       return new DesiredFloor(f, desiredFloorHallway, Direction.STOP);
                   }
               }
           }
           if (!carFull) {
               for (int f = Elevator.numFloors; f >= currentFloor; f--) {
                   if (!Utility.reachedCommitPoint(f, carLevelPositionMM, driveSpeed, driveDirection)) {
                       boolean frontHallCall = hallCalls.getHallCall(f,  Hallway.FRONT,  Direction.DOWN);
                       boolean backHallCall = hallCalls.getHallCall(f, Hallway.BACK, Direction.DOWN);
                       if(frontHallCall || backHallCall) {
                           Hallway desiredFloorHallway = frontHallCall && backHallCall ? Hallway.BOTH :
                                                         (frontHallCall ? Hallway.FRONT : Hallway.BACK);
                           return new DesiredFloor(f, desiredFloorHallway, Direction.DOWN);
                       }
                   }
               }
           }
           return null;
    }
    
    private static DesiredFloor findDesiredBelow(int carLevelPositionMM, double driveSpeed, Direction driveDirection,
            HallCallArray hallCalls, CarCallArray carCalls, int carWeight) {
        
        boolean carFull = carWeight + Passenger.weight > Elevator.MaxCarCapacity;
        int currentFloor = 1 + (int) (((carLevelPositionMM + 100.0) / 1000.0) / Elevator.DISTANCE_BETWEEN_FLOORS);
        for (int f = currentFloor; f >= 1; f--) {
            if (!Utility.reachedCommitPoint(f, carLevelPositionMM, driveSpeed, driveDirection)) {
                if (!carFull) {
                    boolean frontHallCall = hallCalls.getHallCall(f,  Hallway.FRONT,  Direction.DOWN);
                    boolean backHallCall = hallCalls.getHallCall(f, Hallway.BACK, Direction.DOWN);
                    if(frontHallCall || backHallCall) {
                        Hallway desiredFloorHallway = frontHallCall && backHallCall ? Hallway.BOTH :
                                                      (frontHallCall ? Hallway.FRONT : Hallway.BACK);
                        return new DesiredFloor(f, desiredFloorHallway, Direction.DOWN);
                    }
                }
                
                boolean frontCarCall = carCalls.getCarCall(f, Hallway.FRONT);
                boolean backCarCall = carCalls.getCarCall(f, Hallway.BACK);
                if(frontCarCall || backCarCall) { 
                    Hallway desiredFloorHallway = frontCarCall && backCarCall ? Hallway.BOTH : 
                                                  (frontCarCall ? Hallway.FRONT : Hallway.BACK);
                    for(int f2 = f-1; f2 > 0; f2--) {
                        if (hallCalls.getAnyHallCallOnFloor(f2) ||
                                carCalls.getAnyCarCallForFloor(f2))
                            return new DesiredFloor(f, desiredFloorHallway, Direction.DOWN);
                    }
                    for(int f2 = f+1;f2 <= Elevator.numFloors; f2++) {
                        if (hallCalls.getAnyHallCallOnFloor(f2) ||
                                carCalls.getAnyCarCallForFloor(f2))
                            return new DesiredFloor(f, desiredFloorHallway, Direction.UP);
                    }
                    return new DesiredFloor(f, desiredFloorHallway, Direction.STOP);
                }
            }
        }
        if (!carFull) {
            for (int f = 1; f <= currentFloor; f++) {
                if (!Utility.reachedCommitPoint(f, carLevelPositionMM, driveSpeed, driveDirection)) {
                    boolean frontHallCall = hallCalls.getHallCall(f,  Hallway.FRONT,  Direction.UP);
                    boolean backHallCall = hallCalls.getHallCall(f, Hallway.BACK, Direction.UP);
                    if(frontHallCall || backHallCall) {
                        Hallway desiredFloorHallway = frontHallCall && backHallCall ? Hallway.BOTH :
                                                      (frontHallCall ? Hallway.FRONT : Hallway.BACK);
                        return new DesiredFloor(f, desiredFloorHallway, Direction.UP);
                    }
                }
            }
        }
        return null;
    }
    
    public static class DesiredFloor {
        private int floor;
        private Hallway hallway;
        private Direction direction;
        
        public DesiredFloor(int floor, Hallway hallway, Direction direction) {
            this.floor = floor;
            this.hallway = hallway;
            this.direction = direction;
        }
        
        public int getFloor() {
            return floor;
        }

        public Hallway getHallway() {
            return hallway;
        }
        
        public Direction getDirection() {
            return direction;
        }
    
    }
}
