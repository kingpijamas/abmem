package economicCycle.events;

import jamel.World;
import scheduling.schedule.SimulationEvent;

public class RunDebtRecovery extends SimulationEvent {
	@Override
	public void execute() {
		System.out.println(World.getInstance().getBank());
		World.getInstance().getBank().recoverDebts();
	}
}
