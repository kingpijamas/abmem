package main;
import jamel.agents.firms.ProductiveSector;
import jamel.agents.households.Household;
import jamel.spheres.monetary.Bank;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Period;

import economy.Economy;
import utils.Range;

public class EconomyMock extends Economy {
	// Base simulation parameters
	private static final DateTime START = new DateTime(0, 1, 1, 0, 0);
	private static final Period STEP = new Period(24, 0, 0, 0);
	private static final DateTime END = START.plus(Months.THREE);

	// Bank parameters
	private static final boolean ACCOMODATING_BANK = true;
	private static final double ANNUAL_INTEREST_RATE = 0.05;
	private static final double CAPITAL_RATIO = 0.1;
	private static final double DIVIDEND_REDIST_RATIO = 0.5;

	// Market parameters
	private static final int FREQUENCY = 1; // XXX: what is this?
	private static final double AUDACITY = 0.5;
	private static final double WAGE_UPWARD_FLEXIBILITY = 0.5;
	private static final double WAGE_DOWNWARD_FLEXIBILITY = 0.5;

	// Population parameters
	private static final int POPULATION = 1500;
	private static final double SAVING_PROPENSITY = 0.25;

	// Firms' parameters
	private static final int QUANTITY_OF_FIRMS = 100;
	private static final int PRODUCTION_TIME_IN_PERIODS = 2;
	private static final Range DEFAULT_PRODUCTIVITY = new Range(0, 100, 0.5);
	private static final int MACHINES_PER_FIRM = 15;

	public EconomyMock() throws CloneNotSupportedException {
		super(START, END, STEP);
		Bank bank = new Bank(this, ACCOMODATING_BANK, ANNUAL_INTEREST_RATE,
				CAPITAL_RATIO, DIVIDEND_REDIST_RATIO);
		ProductiveSector firms = new ProductiveSector(this, FREQUENCY,
				AUDACITY, WAGE_UPWARD_FLEXIBILITY, WAGE_DOWNWARD_FLEXIBILITY);
		this.set(bank);
		this.set(firms);
		for (int i = 0; i < POPULATION; i++) {
			this.addHousehold(new Household(this, bank, SAVING_PROPENSITY));
		}
		this.init(true);
		firms.newFirms(QUANTITY_OF_FIRMS, PRODUCTION_TIME_IN_PERIODS,
				DEFAULT_PRODUCTIVITY, MACHINES_PER_FIRM);
	}
}
