package jamel.agents.households;

import jamel.markets.Offering;
import jamel.markets.goods.Consuming;
import jamel.markets.goods.Goods;
import jamel.markets.labor.Worker;
import jamel.spheres.monetary.Bank;
import jamel.spheres.monetary.BankAccount;
import scheduling.cycle.CycleElement;
import economy.Economy;

public class Household extends CycleElement implements Consuming<Goods> {
	private Economy economy;

	private static int MAX_MARKET_CRAWLING_INTENTS = 1;

	private double savingPropensity;

	private BankAccount account;
	private Worker worker;

	private long consumptionBudget;
	private Offering<Goods> preferredProvider;

	public Household(Economy economy, Bank bank, double savingPropensity) {
		super(economy.getCycle());
		this.economy = economy;
		this.savingPropensity = savingPropensity;
		this.account = bank.openAccount();
		this.worker = new Worker(economy.getCycle(), account);
	}

	public void init() {
		economy.getGoodsMarket().add(this);
		economy.getLaborMarket().add(worker);
	}

	/*
	 * VALIDATED: comes from the original ConsumptionManager.consume()
	 */
	public void update() {
		long yearlyIncome = worker.getYearlyIncome();
		long savingsTarget = (long) (yearlyIncome * savingPropensity);
		long averageIncome = yearlyIncome / 12; // TODO: check!
		long savings = account.getDeposit() - averageIncome;
		long consumptionTarget;
		if (savings < savingsTarget) {
			consumptionTarget = (long) ((1. - savingPropensity) * averageIncome);
		} else {
			consumptionTarget = averageIncome + (savings - savingsTarget) / 2;
		}
		consumptionBudget = Math.min(account.getDeposit(),
				consumptionTarget);
	}

	public long getConsumptionBudget() {
		return consumptionBudget;
	}

	public void getSupplied(Offering<Goods> offerer) {
		preferredProvider = offerer;
		Goods bought = offerer.supply(account, consumptionBudget);
		consumptionBudget -= bought.getValue();
		bought.consumeAll();
	}

	public Offering<Goods> getPreferredProvider() {
		return preferredProvider;
	}

	public int getMaxMarketCrawlingIntents() {// XXX
		return MAX_MARKET_CRAWLING_INTENTS;
	}

	public BankAccount getBankAccount() {
		return account;
	}

	@Override
	public String toString() {
		String ans = super.toString();
		ans += "\nAccount: " + account.getDeposit();
		ans += "\nUnavailable: " + account.getUnavailableAmount();
		ans += "\nConsumption Budget: " + consumptionBudget;
		ans += "\nPreferredProvider:\n\t" + preferredProvider;
		return ans;
	}

	// TODO: validate all the receiveDividend() methods
}
