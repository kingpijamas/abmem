package jamel.markets.labor;

public class Wage {

	private final double upwardFlexibility;
	private final double downwardFlexibility;

	private static final long MINWAGE = 10;// TODO: magic number
	private long value;

	public Wage(long initialWage, double upwardFlexibility,
			double downwardFlexibility) {
		this.value = Math.max(initialWage, MINWAGE);
		// if(value<MINWAGE){
		// throw new
		// RuntimeException("Temporary: Can't have a wage below the minimum wage");
		// }
		this.upwardFlexibility = upwardFlexibility;
		this.downwardFlexibility = downwardFlexibility;
	}

	public void reduce(float distortion) {
		// if(value<MINWAGE){
		// throw new
		// RuntimeException("Temporary: Can't have a wage below the minimum wage");
		// }
		value -= value * getWageDownwardFlexibility() * distortion;
	}

	private double getWageDownwardFlexibility() {
		return downwardFlexibility;
	}

	public void raise(float distortion) {
		value += value * (1f + getWageUpwardFlexibility() * distortion);
	}

	private double getWageUpwardFlexibility() {
		return upwardFlexibility;
	}

	public long getValue() {
		return value;
	}
}
