package economy.events;

import economy.Economy;

public class RunDebtRecovery extends EconomicEvent {

	public RunDebtRecovery(Economy economy) {
		super(economy);
	}

	@Override
	public void execute() {
		System.out.println(getEconomy().getBank());
		getEconomy().getBank().recoverDebts();
	}
}
