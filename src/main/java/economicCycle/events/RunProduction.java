package economicCycle.events;

import jamel.World;
import scheduling.schedule.SimulationEvent;

public class RunProduction extends SimulationEvent {

	@Override
	public void execute() {
		System.out.println(World.getInstance().getFirms());
		World.getInstance().getFirms().produce();
	}

}
