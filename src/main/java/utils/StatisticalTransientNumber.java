package utils;

import scheduling.cycle.Cycle;
import economy.EconomicCycle;

public class StatisticalTransientNumber extends TransientNumber {

	private int i = 0;
	private int pollSize;
	private double buffer = 0;

	public StatisticalTransientNumber(int pollSize) {
		super();
		this.pollSize = pollSize;
	}

	public StatisticalTransientNumber(EconomicCycle cycle, int pollSize) {
		super(cycle);
		this.pollSize = pollSize;
	}

	public StatisticalTransientNumber(double baseValue, int lifetimeInPeriods,
			int pollSize) {
		super(baseValue, lifetimeInPeriods);
		this.pollSize = pollSize;
		this.buffer = 0;
		this.i = 0;
	}

	public StatisticalTransientNumber(Cycle cycle, double baseValue,
			int lifetimeInPeriods, int pollSize) {
		this(baseValue, lifetimeInPeriods, pollSize);
		init(cycle);
	}

	@Override
	public void add(double value) {
		if (i == pollSize) {
			super.setValue(buffer);
			i = 0;
		}
		buffer += value;
		i++;
	}

	public void setPollsize(int pollSize) {
		this.pollSize = pollSize;
	}

	public int getPollSize() {
		return pollSize;
	}
}
