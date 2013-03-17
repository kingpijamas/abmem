package utils;

import java.util.Random;

public class JamelRandom extends Random {

	private static final long serialVersionUID = -6084951831675788045L;

	@Override
	protected int next(int bits) {
		int result = super.next(bits);
		return result;
	}

	public int nextInt(int min, int max) {
		if (max < min) {
			throw new IllegalArgumentException();
		}
		if (max == min) {
			return min;
		}
		return min + this.nextInt(max - min);
	}

	public double nextExp(double lambda) {
		return -(1 / lambda) * Math.log(1 - this.nextDouble());
	}

	public double nextExp() {
		return nextExp(1);
	}
}