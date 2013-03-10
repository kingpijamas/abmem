package jamel;

import java.util.Date;

public class Simulator {
	private SimulationParams alpha;
	private SimulationParams beta;
	private SimulationParams gamma;
	private String title;
	private boolean multi;
	private MultiSimulationData multiSimulationData;
	private MultiSimulationSeries multiSimulationSeries;
	private double maxSimulations;
	private long begin;
	private Main main;
	/**
	 * le compteur de simulation
	 */
	private int count;

	public Simulator(String title, boolean multi, SimulationParams alpha,
			SimulationParams beta, SimulationParams gamma) {
		if (title == null) {
			title = "Untitled";
		} else {
			this.title = title;
		}
		this.multi = multi;
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
	}

	public int getCount() {
		return count;
	}

	public MultiSimulationSeries getMultiSimulationSeries() {
		return multiSimulationSeries;
	}

	public int getTaskPercentage() {
		return (int) (100 * count / maxSimulations);
	}

	public int getEstimatedRemainingTime() {
		long time = (new Date()).getTime() - begin;
		long averageSimulationTime = time / (count + 1);
		return (int) ((averageSimulationTime * (maxSimulations - count)) / 1000);
	}

	public double getMaxSimulations() {
		return maxSimulations;
	}

	public SimulationParams getAlpha() {
		return alpha;
	}

	public SimulationParams getBeta() {
		return beta;
	}

	public SimulationParams getGamma() {
		return gamma;
	}

}
