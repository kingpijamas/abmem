package jamel.utils;

public class Range {
	private static final int NOT_INIT = -1;
	private static final double DEFAULT_VARIATION = 1.0;
	private double min;
	private double max;
	private double variation = DEFAULT_VARIATION;
	private int steps = NOT_INIT;

	public Range(double min, double max) {
		if (max <= min) {
			throw new IllegalArgumentException();
		}
		this.min = min;
		this.max = max;
	}

	public Range(double min, double max, int steps) {
		this(min, max);
		this.steps = steps;
		if (Double.compare(getStepWidth(), Double.MIN_VALUE) == 0) {
			throw new IllegalArgumentException();
		}
	}

	public Range(double min, double max, double variation) {
		this(min, max);
		this.variation = variation;
	}

	public boolean contains(int val) {
		return (this.min <= val) && (val <= this.max);
	}

	public double getMin() {
		return this.min;
	}

	public double getMax() {
		return this.max;
	}

	public int getRemainingSteps(double curr) {
		return steps - getStepOf(curr);
	}

	public int getStepOf(double curr) {
		if (steps == NOT_INIT) {
			throw new UnsupportedOperationException();
		}
		return (int) (curr / getStepWidth());
	}

	public String toString() {
		return ("[" + min + "-" + max + "]");
	}

	public double getMean() {
		return (max + min) / 2.0;
	}

	public double getHalfRange() {
		return length() / 2.0;
	}

	public double getStepWidth() {
		return length() / ((double) steps);
	}

	public double length() {
		return max - min;
	}

	public Range generateDistortedInterval() {
		return new Range((int) (getMin() * (1. - variation)),
				(int) (getMin() * (1. + variation)));
	}

	public double getVariation() {
		return variation;
	}
}
