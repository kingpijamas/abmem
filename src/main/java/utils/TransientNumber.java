package utils;

import java.util.LinkedList;
import java.util.List;

import scheduling.cycle.Cycle;
import scheduling.cycle.CycleElement;
import scheduling.schedule.SimulationEvent;
import economy.EconomicCycle;

public class TransientNumber extends CycleElement {
	private static final List<TransientNumber> instances = new LinkedList<TransientNumber>();
	private static final double DEFAULT_BASE_VALUE = 0;
	private static final int LIFETIME_IN_PERIODS = 1;

	private Double value;
	private final double baseValue;
	private int maxlifetimeInPeriods;
	private int step;

	public TransientNumber(double baseValue, int maxlifetimeInPeriods) {
		this.value = baseValue;
		this.baseValue = baseValue;
		this.maxlifetimeInPeriods = maxlifetimeInPeriods;
		this.step = 0;
	}

	public TransientNumber(Cycle cycle, double baseValue,
			int maxlifetimeInPeriods) {
		this(baseValue, maxlifetimeInPeriods);
		init(cycle);
	}

	public TransientNumber() {
		this(DEFAULT_BASE_VALUE, LIFETIME_IN_PERIODS);
	}

	public TransientNumber(EconomicCycle cycle) {
		this(DEFAULT_BASE_VALUE, LIFETIME_IN_PERIODS);
		init(cycle);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void add(double value) {
		setValue(getValue() + value);
	}

	@Override
	public String toString() {
		return value + "(base:" + baseValue + ")";
	}

	public static class RefreshTransients extends SimulationEvent {
		@Override
		public void execute() {
			for (TransientNumber t : instances) {
				if (t.step > t.maxlifetimeInPeriods) {
					t.step = 0;
					t.value = t.baseValue;
				} else {
					t.step++;
				}
			}
		}
	}
}
