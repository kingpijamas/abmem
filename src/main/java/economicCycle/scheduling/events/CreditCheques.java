package economicCycle.scheduling.events;

import jamel.spheres.monetary.Cheque;
import economicCycle.scheduling.SimulationEvent;

public class CreditCheques extends SimulationEvent {

	@Override
	public void execute() {
		Cheque.creditCheques();
	}

}
