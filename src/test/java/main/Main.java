package main;

import jamel.World;
import jamel.agents.firms.ProductiveSector;
import jamel.agents.households.Household;
import jamel.spheres.monetary.Bank;
import jamel.utils.Range;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Period;

import economicCycle.EconomicCycle;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {
		DateTime start = new DateTime(0, 1, 1, 1, 1);
		DateTime end = start.plus(Months.THREE);
		EconomicCycle circuit = new EconomicCycle(start, end, new Period(24, 0, 0, 0));
		ProductiveSector firms = new ProductiveSector(circuit, 1, 0.5, 0.5, 0.5);
		Bank bank = new Bank(circuit, 0.05, 0.1);
		World.init(circuit, firms, bank);
		for (int i = 0; i < 1500; i++) {
			World.getInstance()
					.addHousehold(new Household(circuit, bank, 0.25));
		}
		firms.newFirms(100, 2, new Range(0, 100, 0.5), 15);
		World w = World.getInstance();
		w.init();
		w.getCycle().init(false);
		while (!w.getCycle().ended()) {
			w.getCycle().doPeriod();
		}
	}

}
