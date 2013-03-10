package jamel.utils;

public class Tendency {
	private static final int INCREASING = 1;
	private static final int DECREASING = -1;
	private static final int CONSTANT = 0;
	int value = CONSTANT;

	public Tendency(float alpha1, float alpha2, double normalValue,
			double currentValue) {
		if (currentValue < normalValue) {
			if (alpha1 * alpha2 < 1 - currentValue / normalValue) {
				this.value = INCREASING;
			}
		} else if (alpha1 * alpha2 < currentValue / normalValue - 1) {// TODO:
																		// what
																		// if
																		// normalStock=0?
			this.value = DECREASING;
		}
	}

	public boolean isIncreasing() {
		return value == INCREASING;
	}

	public boolean isConstant() {
		return value == CONSTANT;
	}
}
