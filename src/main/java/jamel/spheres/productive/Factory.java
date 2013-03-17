package jamel.spheres.productive;

import jamel.agents.firms.PolicyMaker;
import jamel.markets.goods.Goods;
import jamel.markets.labor.Labor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.JamelRandom;
import utils.Tendency;

public class Factory implements Producing<Goods>, DemandsTendencyEstimator {

	private static final double DEFAULT_PRODUCTIONLEVEL = 0.5;
	private static final double MAX_PRODUCTION_LEVEL = 1;
	private static final double DEFAULT_PRODUCTION_FLEXIBILITY = 1;

	private double productionLevel = DEFAULT_PRODUCTIONLEVEL;
	private double productionFlexibility;
	private PolicyMaker policy;
	private Machinery machines;
	private Set<Labor> labor = new HashSet<Labor>();

	public Factory(PolicyMaker policy, List<Machine> machines) {
		this(policy, machines, DEFAULT_PRODUCTION_FLEXIBILITY);
	}

	public Factory(PolicyMaker policy, List<Machine> machines,
			double productionFlexibility) {
		this.machines = new Machinery(machines);
		this.productionFlexibility = productionFlexibility;
		this.policy = policy;
	}

	private void reduceProduction(float ratio) {
		this.productionLevel = Math.max(0, productionLevel - ratio
				* productionFlexibility);
	}

	private void raiseProduction(float ratio) {
		this.productionLevel = Math.min(MAX_PRODUCTION_LEVEL, productionLevel
				+ ratio * productionFlexibility);
	}

	public Tendency getStockTendency(int currentStock, float alpha1,
			float alpha2) {
		double normalStock = policy.getStockTarget()
				* machines.getMaxProduction();
		return new Tendency(alpha1, alpha2, normalStock, currentStock);
	}

	public void planProduction(int currentInventoryVolume) {
		float alpha1 = new JamelRandom().nextFloat();
		float alpha2 = new JamelRandom().nextFloat();
		Tendency tendency = getStockTendency(currentInventoryVolume, alpha1,
				alpha2);
		if (tendency.isConstant()) {
			return;
		}
		if (tendency.isIncreasing()) {
			raiseProduction(alpha1);
		} else {
			reduceProduction(alpha1);
		}
	}

	public int getWorkforceRequirement() {
		return (int) Math.round(machines.getAmountOfMachines()
				* productionLevel);
	}

	public long getCapital() {
		return machines.getUnfinishedGoodsValue();
	}

	public Goods produceGoods() {
		Goods production = new Goods();
		for (Labor l : labor) {
			production = production.add(machines.produceGoods(l));
		}
		return production;
	}

	public void addMachines(List<Machine> machines) {
		this.machines.add(machines);
	}

	public Tendency getDemandsTendency(int currentStock, float alpha1,
			float alpha2) {
		return getStockTendency(currentStock, alpha1, alpha2);
	}

	public long getUnitCost() {
		long totalLaborValue = 0;
		for (Labor l : labor) {
			totalLaborValue += l.getValue();
		}
		//FIXME: if labor is empty, the line below throws a / by zero exception
		return machines.getAverageProductionTime() * totalLaborValue
				/ labor.size();
	}

	public void addLabor(Labor l) {
		labor.add(l);
	}
}
