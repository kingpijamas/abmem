package economicCycle.events;

import jamel.spheres.monetary.Cheque;
import scheduling.schedule.SimulationEvent;

public class CreditCheques extends SimulationEvent {

	@Override
	public void execute() {
		Cheque.creditCheques();
	}

}
