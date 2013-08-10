package economy.events;

import jamel.markets.Market;
import scheduling.schedule.SimulationEvent;

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
