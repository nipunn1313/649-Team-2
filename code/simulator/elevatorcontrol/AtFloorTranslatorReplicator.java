package simulator.elevatorcontrol;

import simulator.elevatormodules.*;
import simulator.framework.*;
import java.util.ArrayList;
import simulator.payloads.CanMailbox.*;

public class AtFloorTranslatorReplicator {
	ArrayList<AtFloorCanPayloadTranslator> mAtFloorArray;
	
	public AtFloorTranslatorReplicator(ArrayList<ReadableCanMailbox> list) {
		mAtFloorArray = new ArrayList<AtFloorCanPayloadTranslator>(10);
		
		mAtFloorArray.add(new AtFloorCanPayloadTranslator(list.get(0),1,Hallway.BOTH));
		mAtFloorArray.add(new AtFloorCanPayloadTranslator(list.get(1),2,Hallway.BACK));
		mAtFloorArray.add(new AtFloorCanPayloadTranslator(list.get(2),3,Hallway.FRONT));
		mAtFloorArray.add(new AtFloorCanPayloadTranslator(list.get(3),4,Hallway.FRONT));
		mAtFloorArray.add(new AtFloorCanPayloadTranslator(list.get(4),5,Hallway.FRONT));
		mAtFloorArray.add(new AtFloorCanPayloadTranslator(list.get(5),6,Hallway.FRONT));
		mAtFloorArray.add(new AtFloorCanPayloadTranslator(list.get(6),7,Hallway.BOTH));
		mAtFloorArray.add(new AtFloorCanPayloadTranslator(list.get(7),8,Hallway.FRONT));
	}
	
	
}
