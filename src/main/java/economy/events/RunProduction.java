package economy.events;

import economy.Economy;

public class RunProduction extends EconomicEvent {

	public RunProduction(Economy economy) {
		super(economy);
	}

	@Override
	public void execute() {
		System.out.println(getEconomy().getFirms());
		getEconomy().getFirms().produce();
	}

}
