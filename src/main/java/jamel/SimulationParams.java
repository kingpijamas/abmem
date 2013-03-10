package jamel;

public class SimulationParams {
	public Double param;
	public Double max;
	public Double min;
	public Double step;

	public SimulationParams(Double param, Double min, Double max, Double step) {
		this.param = param;
		this.min = min;
		this.max = max;
		this.step = step;
	}

	public double steps() {
		return (max - min) / (step + 1);
	}

}
