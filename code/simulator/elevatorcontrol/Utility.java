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
import simulator.payloads.CANNetwork;
import simulator.framework.Direction;
import simulator.framework.Elevator;
import simulator.framework.Hallway;
import simulator.framework.Harness;
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
        public final Hallway hallway;

        public DoorClosedArray(Hallway hallway, CANNetwork.CanConnection conn) {
            this.hallway = hallway;
            for (Side s : Side.values()) {
                int index = ReplicationComputer.computeReplicationId(hallway, s);
                ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.DOOR_CLOSED_SENSOR_BASE_CAN_ID + index);
                DoorClosedCanPayloadTranslator t = new DoorClosedCanPayloadTranslator(m, hallway, s);
                conn.registerTimeTriggered(m);
                translatorArray.put(index, t);
            }
        }

        public boolean getBothClosed() {
            return translatorArray.get(ReplicationComputer.computeReplicationId(hallway, Side.LEFT)).getValue() &&
                    translatorArray.get(ReplicationComputer.computeReplicationId(hallway, Side.RIGHT)).getValue();
        }
    }

    public static class DoorOpenedArray {

        HashMap<Integer, DoorOpenedCanPayloadTranslator> translatorArray = new HashMap<Integer, DoorOpenedCanPayloadTranslator>();
        public final Hallway hallway;

        public DoorOpenedArray(Hallway hallway, CANNetwork.CanConnection conn) {
            this.hallway = hallway;
            for (Side s : Side.values()) {
                int index = ReplicationComputer.computeReplicationId(hallway, s);
                ReadableCanMailbox m = CanMailbox.getReadableCanMailbox(MessageDictionary.DOOR_OPEN_SENSOR_BASE_CAN_ID + index);
                DoorOpenedCanPayloadTranslator t = new DoorOpenedCanPayloadTranslator(m, hallway, s);
                conn.registerTimeTriggered(m);
                translatorArray.put(index, t);
            }
        }

        public boolean getBothOpened() {
            return translatorArray.get(ReplicationComputer.computeReplicationId(hallway, Side.LEFT)).getValue() &&
                    translatorArray.get(ReplicationComputer.computeReplicationId(hallway, Side.RIGHT)).getValue();
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
}
