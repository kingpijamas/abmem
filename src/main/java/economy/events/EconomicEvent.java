package economy.events;

import scheduling.schedule.SimulationEvent;
import economy.Economy;

public abstract class EconomicEvent extends SimulationEvent {
	private final Economy economy;

	public EconomicEvent(Economy economy) {
		this.economy = economy;
	}

	protected Economy getEconomy() {
		return economy;
	}
}
