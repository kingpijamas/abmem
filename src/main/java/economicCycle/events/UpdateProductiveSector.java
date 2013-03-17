package economicCycle.events;

import jamel.World;
import scheduling.schedule.SimulationEvent;


public class UpdateProductiveSector extends SimulationEvent {

	@Override
	public void execute() {
		World.getInstance().getFirms().update();
	}

}
