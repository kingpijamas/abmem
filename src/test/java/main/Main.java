package main;

import jamel.World;
import jamel.agents.firms.ProductiveSector;
import jamel.agents.households.Household;
import jamel.spheres.monetary.Bank;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Period;

import utils.Range;
import economicCycle.EconomicCycle;

public class Main {
	private static final Period DAY = new Period(24, 0, 0, 0);

	private static final DateTime SIMULATION_START = new DateTime(0, 1, 1, 0, 0);

	private static final double ANNUAL_INTEREST_RATE = 0.05;
	private static final double CAPITAL_RATIO = 0.1;

	private static final int FREQUENCY = 1; // XXX: what is this?
	private static final double AUDACITY = 0.5;
	private static final double WAGE_UPWARD_FLEXIBILITY = 0.5;
	private static final double WAGE_DOWNWARD_FLEXIBILITY = 0.5;

	private static final int POPULATION = 1500;
	private static final double SAVING_PROPENSITY = 0.25;

	private static final int QUANTITY_OF_FIRMS = 100;
	private static final int PRODUCTION_TIME_IN_PERIODS = 2;
	private static final Range DEFAULT_PRODUCTIVITY = new Range(0, 100, 0.5);
	private static final int MACHINES_PER_FIRM = 15;

	public static void main(String[] args) throws CloneNotSupportedException {
		DateTime end = SIMULATION_START.plus(Months.THREE);
		EconomicCycle cycle = new EconomicCycle(SIMULATION_START, end, DAY);
		Bank bank = new Bank(cycle, ANNUAL_INTEREST_RATE, CAPITAL_RATIO);
		ProductiveSector firms = new ProductiveSector(cycle, FREQUENCY,
				AUDACITY, WAGE_UPWARD_FLEXIBILITY, WAGE_DOWNWARD_FLEXIBILITY);
		World.init(cycle, firms, bank);
		World world = World.getInstance();
		for (int i = 0; i < POPULATION; i++) {
			world.addHousehold(new Household(cycle, bank, SAVING_PROPENSITY));
		}
		firms.newFirms(QUANTITY_OF_FIRMS, PRODUCTION_TIME_IN_PERIODS,
				DEFAULT_PRODUCTIVITY, MACHINES_PER_FIRM);
		world.init(true);
		while (!cycle.ended()) {
			world.getCycle().doPeriod();
		}
	}
}
