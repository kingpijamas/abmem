package economy.events;

import economy.Economy;

public class PayFirmDividends extends EconomicEvent {

	public PayFirmDividends(Economy economy) {
		super(economy);
	}

	@Override
	public void execute() {
		System.out.println(getEconomy().getFirms());
		getEconomy().getFirms().payDividends();
	}

}
