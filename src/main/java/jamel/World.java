package jamel;

import jamel.agents.firms.ProductiveSector;
import jamel.agents.households.Household;
import jamel.markets.goods.GoodsMarket;
import jamel.markets.labor.LaborMarket;
import jamel.spheres.monetary.Bank;
import jamel.spheres.monetary.exceptions.UnexpectedInvocationException;

import java.util.LinkedList;
import java.util.List;

import utils.JamelRandom;
import economicCycle.EconomicCycle;

public class World {
	private static World instance;
	private Bank bank;
	private EconomicCycle circuit;
	private List<Household> households;
	private ProductiveSector firms;
	private LaborMarket laborMarket;
	private GoodsMarket goodsMarket;

	private World(EconomicCycle circuit, ProductiveSector firms, Bank bank)
			throws CloneNotSupportedException {
		this.circuit = circuit;
		this.laborMarket = new LaborMarket(circuit);
		this.goodsMarket = new GoodsMarket(circuit);
		this.bank = bank;
		this.firms = firms;
		this.households = new LinkedList<Household>();
	}

	public static void init(EconomicCycle circuit, ProductiveSector firms,
			Bank bank) throws CloneNotSupportedException {
		if (instance != null) {
			throw new UnexpectedInvocationException();
		}
		instance = new World(circuit, firms, bank);
	}

	public static World getInstance() {
		return instance;
	}

	public void init(boolean testing) {// FIXME
		for (Household h : households) {
			h.enterMarkets();
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

	public EconomicCycle getCycle() {
		return circuit;
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
