package economy.events;

import economy.Economy;

public class UpdateBank extends EconomicEvent {

	public UpdateBank(Economy economy) {
		super(economy);
	}

	@Override
	public void execute() {
		System.out.println(getEconomy().getBank());
		getEconomy().getBank().update();
	}
}
