package jamel.agents.households;

import jamel.World;
import jamel.markets.Offering;
import jamel.markets.goods.Consuming;
import jamel.markets.goods.Goods;
import jamel.markets.labor.Worker;
import jamel.spheres.monetary.Bank;
import jamel.spheres.monetary.BankAccount;
import jamel.utils.StatisticalTransientNumber;
import economicCycle.Cycle;
import economicCycle.CycleElement;

public class Household extends CycleElement implements Consuming<Goods> {
	private static int MAX_MARKET_CRAWLING_INTENTS = 1;

	private double savingPropensity;

	private BankAccount account;
	private Worker worker;

	private long consumptionTarget;
	private Offering<Goods> preferredProvider;

	private StatisticalTransientNumber income;// TODO

	public Household(Cycle circuit, Bank bank, double savingPropensity) {
		super(circuit);
		this.savingPropensity = savingPropensity;
		this.account = bank.openAccount();
		this.worker = new Worker(circuit, account);
		this.income = new StatisticalTransientNumber(circuit, 1);
	}

	public void enterMarkets() {
		World.getInstance().getGoodsMarket().add(this);
		World.getInstance().getLaborMarket().add(worker);
	}

	public void update() {
		long savingsTarget = (long) (worker.getWage() * savingPropensity);
		income.add(worker.getWage());
		long averageIncome = World.getInstance().getLaborMarket()
				.getAverageValue();
		long savings = account.getAvailableAmount() - averageIncome;
		if (savings < savingsTarget) {
			consumptionTarget = (long) ((1. - savingPropensity) * averageIncome);
		} else {
			consumptionTarget = averageIncome + (savings - savingsTarget) / 2;
		}
	}

	public void getSupplied(Offering<Goods> offerer) {
		preferredProvider = offerer;
		offerer.supply(account, getConsumptionBudget()).consumeAll();
	}

	public Offering<Goods> getPreferredProvider() {
		return preferredProvider;
	}

	public int getMaxMarketCrawlingIntents() {
		return MAX_MARKET_CRAWLING_INTENTS;
	}

	public long getConsumptionBudget() {
		if (Math.min(account.getAvailableAmount(), consumptionTarget) < 0) {
			throw new IllegalArgumentException("ACA");
		}
		return Math.min(account.getAvailableAmount(), consumptionTarget);
	}

	public BankAccount getBankAccount() {
		return account;
	}

	@Override
	public String toString() {
		String ans = super.toString();
		ans += "\nAccount: " + account.getAvailableAmount();
		ans += "\nUnavailable: " + account.getUnavailableAmount();
		ans += "\nConsumption Target: " + consumptionTarget;
		ans += "\nPreferredProvider:\n\t" + preferredProvider;
		return ans;
	}
	
	//TODO: validate all the receiveDividend() methods
}
