package economicCycle.scheduling.events;

import jamel.World;
import economicCycle.scheduling.SimulationEvent;

public class PayFirmDividends extends SimulationEvent {

	@Override
	public void execute() {
		System.out.println(World.getInstance().getFirms());
		World.getInstance().getFirms().payDividends();
	}

}
