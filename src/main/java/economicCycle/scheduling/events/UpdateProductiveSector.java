package economicCycle.scheduling.events;

import jamel.World;
import economicCycle.scheduling.SimulationEvent;


public class UpdateProductiveSector extends SimulationEvent {

	@Override
	public void execute() {
		World.getInstance().getFirms().update();
	}

}
