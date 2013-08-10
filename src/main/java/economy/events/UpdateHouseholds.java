package economy.events;

import economy.Economy;

public class UpdateHouseholds extends EconomicEvent {

	public UpdateHouseholds(Economy economy) {
		super(economy);
	}

	@Override
	public void execute() {
		getEconomy().update();
	}

}
