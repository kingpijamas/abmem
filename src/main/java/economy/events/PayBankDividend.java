package economy.events;

import economy.Economy;

public class PayBankDividend extends EconomicEvent {

	public PayBankDividend(Economy economy) {
		super(economy);
	}

	@Override
	public void execute() {
		System.out.println(getEconomy().getBank());// TODO: Remove. Here just
													// for debugging purposes
		getEconomy().getBank().payDividend();
	}

}
