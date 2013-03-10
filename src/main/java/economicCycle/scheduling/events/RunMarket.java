package economicCycle.scheduling.events;

import jamel.markets.Market;
import economicCycle.scheduling.SimulationEvent;

public class RunMarket extends SimulationEvent {
	@SuppressWarnings("rawtypes")
	private Market m;

	@SuppressWarnings("rawtypes")
	public RunMarket(Market m) {
		this.m = m;
	}

	@Override
	public void execute() {
		System.out.println(m);
		m.run();
	}

	@Override
	public String toString() {
		return "Run" + m.getClass().getSimpleName() + "\n" + super.toString();
	}
}
