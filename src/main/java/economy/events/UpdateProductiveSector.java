package economy.events;

import economy.Economy;

public class UpdateProductiveSector extends EconomicEvent {

	public UpdateProductiveSector(Economy economy) {
		super(economy);
	}

	@Override
	public void execute() {
		getEconomy().getFirms().update();
	}

}
