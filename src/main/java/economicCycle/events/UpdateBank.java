package economicCycle.events;

import jamel.World;
import scheduling.schedule.SimulationEvent;

public class UpdateBank extends SimulationEvent {

	@Override
	public void execute() {
		System.out.println(World.getInstance().getBank());
		World.getInstance().getBank().update();
	}
}
