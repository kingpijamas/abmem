package jamel.agents.firms;

import jamel.markets.Offering;
import jamel.markets.goods.Goods;
import jamel.markets.labor.Employer;
import jamel.markets.labor.Wage;
import jamel.spheres.monetary.BankAccount;
import jamel.spheres.monetary.Borrower;
import jamel.spheres.monetary.BorrowerBankAccount;
import jamel.spheres.productive.Factory;
import jamel.spheres.productive.Machine;

import java.util.List;

import org.joda.time.Years;

import scheduling.cycle.CycleElement;
import economy.events.FirmComeback;

public class Firm extends CycleElement implements Offering<Goods>, Borrower {
	// TODO: when a Firm dies, it should be either removed from
	// ProductiveSector.firms, OR at least marked as inactive
	private BankAccount owner;
	private BorrowerBankAccount account;
	private FirmPolicy policy;
	private Employer workforce;
	private Factory production;
	private PriceManager priceManager;
	private Goods inventory;
	private ProductiveSector sector;

	public Firm(ProductiveSector sector, Wage offeredWage,
			List<Machine> machines, long startingPrice) {
		this(sector, sector.getEconomy().getRandomHousehold().getBankAccount(),
				offeredWage, machines, startingPrice);
	}

	public Firm(ProductiveSector sector, BankAccount owner, Wage offeredWage,
			List<Machine> machines, long startingPrice) {
		super(sector.getCycle());
		this.owner = owner;
		this.sector = sector;
		this.policy = new FirmPolicy(sector.getCycle(), this);
		this.production = new Factory(policy, machines);
		this.account = sector.getEconomy().getBank().openAccount(this);
		this.workforce = new Employer(sector.getCycle(), production, account,
				offeredWage);
		this.priceManager = new PriceManager(policy, production, startingPrice);
		this.inventory = new Goods();
		enterMarkets();
	}

	public void enterMarkets() {
		sector.getLaborMarket().add(workforce);
		sector.getGoodsMarket().add(this);
	}

	void setPolicy(FirmPolicy policy) {
		this.policy = policy;
	}

	void update() {
		production.planProduction(inventory.getVolume());
		priceManager.updatePrice(production.getUnitCost(),
				inventory.getVolume());
	}

	/**
	 * Renvoie la profitabilit� moyenne de l'objet, rapport de la somme des
	 * dividendes vers�s depuis la naissance de l'objet et du nombre de p�riodes
	 * v�cues.
	 * 
	 * @return la profitabilit� de l'objet
	 */
	public double getProfitability() {// FIXME: according to the new pdf, should
										// be MY own dividend, not the
										// average one
		return (long) sector.getAverageDividend();
	}

	public long getCapital() {
		return account.getCapital() + production.getCapital()
				+ inventory.getValue();
	}

	public void prepareOffer() {
		inventory = inventory.add(production.produceGoods());
	}

	public Goods getOffer() {
		return inventory;
	}

	public Goods supply(BankAccount consumer, long amount) {
		consumer.transfer(this.account, amount);
		return getOffer().getSuboffer(amount);
	}

	public long payDividend() {// FIXME: dividends have to wait to be paid
								// (since they are paid via cheque now, instead
								// of crediting them), so
								// they aren't technically what they are
								// supposed to be
		long dividend = calculateDividend();
		// try {
		account.transfer(owner, dividend);// TODO: ours
		// } catch (IllegalArgumentException e) {
		// XXX
		// }
		// owner.deposit(dividend); before
		return dividend;
	}

	/**
	 * Si l'entreprise accumule les pertes, on ne distribue pas de dividendes.
	 * Si le niveau des benefices accumules sont insuffisants, on ne distribue
	 * pas de dividendes. Sinon, les dividendes a distribuer sont l'exces des
	 * benefices accumules sur leur niveau desire.
	 * 
	 * @return
	 */
	private long calculateDividend() {
		long retainedEarningsEffective = getCapital();
		if (retainedEarningsEffective <= 0) {
			return 0;
		}
		long retainedEarningsTarget = (long) ((account.getTotalDebt() + retainedEarningsEffective) * policy
				.getReserveTarget());
		if (retainedEarningsEffective <= retainedEarningsTarget) {
			return 0;
		}
		return (retainedEarningsEffective - retainedEarningsTarget) / 2;
	}

	public void declareBankruptcy() {// TODO: verify if this is equal to
										// FirmsSector.failure(etc etc) in Jamel
		sector.getLaborMarket().remove(workforce);
		sector.getGoodsMarket().remove(this);
		getCycle().addEvent(new FirmComeback(sector, this),
				getDate().plus(Years.ONE));
	}

	FirmPolicy getPolicy() {
		return policy;
	}
}
