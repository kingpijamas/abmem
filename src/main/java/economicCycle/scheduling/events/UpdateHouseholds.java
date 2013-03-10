package economicCycle.scheduling.events;

import jamel.World;
import economicCycle.scheduling.SimulationEvent;

public class UpdateHouseholds extends SimulationEvent{

	@Override
	public void execute() {
		World.getInstance().update();
	}
	
}
