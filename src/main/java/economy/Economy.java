package economy;

import jamel.agents.firms.ProductiveSector;
import jamel.agents.households.Household;
import jamel.markets.goods.GoodsMarket;
import jamel.markets.labor.LaborMarket;
import jamel.spheres.monetary.Bank;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

import utils.JamelRandom;

public class Economy {
	private EconomicCycle cycle;
	private Bank bank;
	private List<Household> households = new LinkedList<Household>();
	private ProductiveSector firms;
	private LaborMarket laborMarket;
	private GoodsMarket goodsMarket;

	public Economy(DateTime start, DateTime end, Period step)
			throws CloneNotSupportedException {
		this.cycle = new EconomicCycle(this, start, end, step);
	}

	public void set(Bank bank) {
		this.bank = bank;
	}

	public void set(ProductiveSector firms) {
		this.firms = firms;
	}

	public void init(boolean testing) {// FIXME
		this.laborMarket = new LaborMarket(cycle);
		this.goodsMarket = new GoodsMarket(cycle);

		for (Household h : households) {
			h.init();
		}
		firms.enterMarkets();
		bank.setOwner(getRandomHousehold().getBankAccount());
		getCycle().init(testing);
	}

	public void addHousehold(Household h) {
		households.add(h);
	}

	public Bank getBank() {
		return bank;
	}

	public EconomicCycle getCycle() {// XXX
		return cycle;
	}

	public Household getRandomHousehold() {
		if (households.isEmpty()) {
			return null;
		}
		return households.get(new JamelRandom().nextInt(0, households.size()));
	}

	public ProductiveSector getFirms() {
		return firms;
	}

	public LaborMarket getLaborMarket() {
		return laborMarket;
	}

	public GoodsMarket getGoodsMarket() {
		return goodsMarket;
	}

	public void update() {
		for (Household h : households) {
			h.update();
		}
	}

}
