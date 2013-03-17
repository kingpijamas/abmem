package utils;

import scheduling.cycle.Cycle;
import economicCycle.EconomicCycle;

public class StatisticalTransientNumber extends TransientNumber {

	private int i;
	private int pollSize;
	private double buffer;

	public StatisticalTransientNumber(EconomicCycle circuit, int pollSize) {
		super(circuit);
		this.pollSize = pollSize;
		this.buffer = 0;
		this.i = 0;
	}

	public StatisticalTransientNumber(Cycle cycle, double baseValue,
			int lifetimeInPeriods, int pollSize) {
		super(cycle, baseValue, lifetimeInPeriods);
		this.pollSize = pollSize;
		this.buffer = 0;
		this.i = 0;
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
