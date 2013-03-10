package jamel;

import jamel.agents.firms.ProductiveSector;
import jamel.agents.households.Household;
import jamel.markets.goods.GoodsMarket;
import jamel.markets.labor.LaborMarket;
import jamel.spheres.monetary.Bank;
import jamel.spheres.monetary.exceptions.UnexpectedInvocationException;
import jamel.utils.JamelRandom;

import java.util.LinkedList;
import java.util.List;

import economicCycle.Cycle;

public class World {

	private static World instance;
	private Bank bank;
	private Cycle circuit;
	private List<Household> households;
	private ProductiveSector firms;
	private LaborMarket laborMarket;
	private GoodsMarket goodsMarket;

	private World(Cycle circuit, ProductiveSector firms, Bank bank)
			throws CloneNotSupportedException {
		this.circuit = circuit;
		this.laborMarket = new LaborMarket(circuit);
		this.goodsMarket = new GoodsMarket(circuit);
		this.bank = bank;
		this.firms = firms;
		this.households = new LinkedList<Household>();
	}

	public static void init(Cycle circuit, ProductiveSector firms, Bank bank)
			throws CloneNotSupportedException {
		if (instance != null) {
			throw new UnexpectedInvocationException();
		}
		instance = new World(circuit, firms, bank);
	}

	public static World getInstance() {
		return instance;
	}

	public void init() {// FIXME
		for (Household h : households) {
			h.enterMarkets();
		}
		firms.enterMarkets();
		bank.findOwner();
	}

	public void addHousehold(Household h) {
		households.add(h);
	}

	public Bank getBank() {
		return bank;
	}

	public Cycle getCycle() {
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