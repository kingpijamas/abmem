package economicCycle.scheduling.events;

import jamel.World;
import economicCycle.scheduling.SimulationEvent;

public class RunDebtRecovery extends SimulationEvent {
	@Override
	public void execute() {
		System.out.println(World.getInstance().getBank());
		World.getInstance().getBank().recoverDebts();
	}
}
