package jamel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *	A collection of series.
 */
public class MultiSimulationSeries {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Map<String, XYSeries>> dict; 
	/**
	 * Creates a new collection of series.
	 */
	public MultiSimulationSeries() {
		this.dict = new HashMap<String, Map<String, XYSeries>>();
		this.putNewSeries(DataLabel.bankruptcies);
		this.putNewSeries(DataLabel.doubtfulDebtRatio);
		this.putNewSeries(DataLabel.duration);
		this.putNewSeries(DataLabel.medianPrice);
		this.putNewSeries(DataLabel.medianWage);
		this.putNewSeries(DataLabel.moneyVelocity);
		this.putNewSeries(DataLabel.productionVolume);
		this.putNewSeries(DataLabel.profitShare);
		this.putNewSeries(DataLabel.unemployment);
		this.putNewSeries(DataLabel.inflation);
		this.putNewXYSeries(DataLabel.phillipsCurve);
	}

	private void putNewXYSeries(String key) {
		this.dict.put(key, new HashMap<String, XYSeries>());
		Map<String, XYSeries> m = this.dict.get(key);
		m.put(key, new XYSeries(key));
	}

	private void putNewSeries(String key) {
		this.dict.put(key, new HashMap<String, XYSeries>());
		Map<String, XYSeries> m = this.dict.get(key);
		
		for(InnerMapKeys e: InnerMapKeys.values() ){
			m.put(e.name(), new XYSeries(key+" - "+e.humanName()));
		}
	}

	/**
	 * Adds a new collection of simulation data to the collection of series. 
	 * @param beta the value of the parameter.
	 * @param multiSimulationData the collection of data.
	 */
	public void add(double beta, MultiSimulationData multiSimulationData) {
		for(Map.Entry<String, LinkedList<Double>> entry : multiSimulationData.entrySet()) {
			String key = entry.getKey();
			LinkedList<Double> list = entry.getValue();
			BoxAndWhiskerItem boxAndWhiskerItem = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(list);
			for(InnerMapKeys e: InnerMapKeys.values()){
				this.add(key, e.name(), beta, boxAndWhiskerItem.getMedian().doubleValue());
			}
			this.add(key+"-median",beta,boxAndWhiskerItem.getMedian().doubleValue());
			this.add(key+"-Q1",beta,boxAndWhiskerItem.getQ1().doubleValue());
			this.add(key+"-Q3",beta,boxAndWhiskerItem.getQ3().doubleValue());
			this.add(key+"-maxRegularValue",beta,boxAndWhiskerItem.getMaxRegularValue().doubleValue());
			this.add(key+"-minRegularValue",beta,boxAndWhiskerItem.getMinRegularValue().doubleValue());
		}
	}
	
	private void add(String key, String innerMapKey, double xValue,
			double yValue) {
		if (Double.isInfinite(yValue) && this.dict.get(key) != null) return;
		this.dict.get(key).get(innerMapKey).add(xValue, yValue);
	}

	/**
	 * @param key string
	 * @param xValue double
	 * @param yValue double
	 */
	private void add(String key, double xValue, double yValue) {
		if (Double.isInfinite(yValue)) return; 
		this.get(key).add(xValue,yValue);		
	}

	/**
	 * Returns a given 
	 * @param key
	 * @return
	 */
	public XYSeries get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Utilis� pour la courbe de Phillips et de Beveridge
	 * @param key
	 * @param xLabel
	 * @param yLabel
	 * @param data
	 */
	public void add(String key, String xLabel, String yLabel, HashMap<String, Double> data) {
		final Double xValue = data.get(xLabel);
		final Double yValue = data.get(yLabel);
		if ((xValue != null)&((yValue != null)))
			this.get(key).add(xValue,yValue);		
	}
	
	private enum InnerMapKeys {
		MEDIAN, Q1, Q3, MAX_REGULAR_VALUE, MIN_REGULAR_VALUE;
		
		public String humanName(){
			return this.name().replace("_", " ");
		}
	}

}
