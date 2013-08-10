package main;

import org.joda.time.DateTime;
import org.joda.time.Period;

import economy.EconomicCycle;

public class EconomicCycleMock extends EconomicCycle {

	public EconomicCycleMock(DateTime start, DateTime end, Period step) {
		super(null, start, end, step);
	}

}
