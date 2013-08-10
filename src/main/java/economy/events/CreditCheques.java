package economy.events;

import economy.Economy;
import jamel.spheres.monetary.Cheque;

public class CreditCheques extends EconomicEvent {

	public CreditCheques(Economy economy) {
		super(economy);
	}

	@Override
	public void execute() {
		Cheque.creditCheques();
	}

}
