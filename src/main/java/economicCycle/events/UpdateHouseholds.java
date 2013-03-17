package economicCycle.events;

import jamel.World;
import scheduling.schedule.SimulationEvent;

public class UpdateHouseholds extends SimulationEvent{

	@Override
	public void execute() {
		World.getInstance().update();
	}
	
}
