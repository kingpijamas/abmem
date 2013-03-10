package jamel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * A dataset for storage of data from multi simulations.
 */
public class MultiSimulationData extends HashMap<String,LinkedList<Double>> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new dataset.
	 */
	public MultiSimulationData() {
		this.put(DataLabel.bankruptcies, new LinkedList<Double>());
		this.put(DataLabel.doubtfulDebtRatio, new LinkedList<Double>());
		this.put(DataLabel.duration, new LinkedList<Double>());
		this.put(DataLabel.medianPrice, new LinkedList<Double>());
		this.put(DataLabel.medianWage, new LinkedList<Double>());
		this.put(DataLabel.moneyVelocity, new LinkedList<Double>());
		this.put(DataLabel.productionVolume, new LinkedList<Double>());
		this.put(DataLabel.profitShare, new LinkedList<Double>());
		this.put(DataLabel.unemployment, new LinkedList<Double>());
		this.put(DataLabel.inflation, new LinkedList<Double>());
	}

	/**
	 * Adds the simulation data to the collection.
	 * @param simulationData the data to add.
	 */
	public void add(HashMap<String, Double> simulationData) {
		for(Map.Entry<String, Double> entry : simulationData.entrySet()) {
		    String key = entry.getKey();
		    Double value = entry.getValue();
		    this.get(key).add(value);
		}
	}
	
}
