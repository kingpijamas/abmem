/** 
 * ===================================================
 * JAMEL : a Java Agent-based MacroEconomic Main.
 * ===================================================
 * 
 * (C) Copyright 2007-2010, by Pascal Seppecher.
 * 
 * Project Info:  http://p.seppecher.free.fr/jamel/ 
 */

package jamel.spheres.productive;

import jamel.markets.goods.Goods;
import jamel.markets.labor.Labor;

import java.util.LinkedList;
import java.util.List;

import utils.Range;

/**
 * Represents a factory.
 * <p>
 * A factory encapsulates a machinery (= a collection of {@link Machine} objects).
 * <p>
 * Last update: 7-Dec-2010
 */
/**
 * @author pascal
 * 
 */
public class Machinery {

	/** The list of machines. */
	private List<Machine> machines;
	private Goods production;
	private int totalProductivity;

	private int averageProductionTime;

	private Machine bestMachine;
	private Machine worstMachine;

	/**
	 * Creates a new factory.
	 * 
	 * @param inventory
	 *            the inventory.
	 */
	public Machinery(List<Machine> machines) {
		this.bestMachine = null;
		this.worstMachine = null;
		this.production = new Goods();
		this.totalProductivity = 0;
		this.machines = new LinkedList<Machine>();
		add(machines);
	}

	public void add(List<Machine> machines) {
		int totalTime = machines.size() * getAverageProductionTime();
		for (Machine m : machines) {
			totalTime += m.getProductionTime();
			this.machines.add(m);
			if (bestMachine == null || m.compareTo(bestMachine) > 0) {
				bestMachine = m;
			} else if (worstMachine == null || m.compareTo(worstMachine) < 0) {
				worstMachine = m;
			}
			totalProductivity += m.getProductivity();
		}
		averageProductionTime = totalTime / machines.size();
	}

	/**
	 * Changes the productivity of all machines.
	 * 
	 * @param productivity
	 *            the change ratio.
	 */
	public void changeProductivity(double ratio) {
		for (Machine machine : machines) {
			totalProductivity -= machine.getProductivity();
			machine.changeProductivity(ratio);
			totalProductivity += machine.getProductivity();
		}
	}

	/**
	 * Returns the average monthly volume that the factory produces when working
	 * at its full capacity.
	 * 
	 * @return an integer that represents the average monthly production.
	 */
	public int getMaxProduction() {
		return totalProductivity;
	}

	/**
	 * Returns the value of the production of the period.
	 * 
	 * @return a double that represents the production value.
	 */
	public double getProductionValue() {
		return production.getValue();
	}

	/**
	 * Returns the volume of the production of the period.
	 * 
	 * @return an integer that represents the production volume.
	 */
	public int getProductionVolume() {
		return production.getVolume();
	}

	/**
	 * Returns the average theoretical productivity of the factory.
	 * 
	 * @return a double that represents the average theoretical productivity.
	 */
	public double getAverageProductivity() {
		return totalProductivity / (double) getAmountOfMachines();
	}

	/**
	 * Returns the interval of the productivities between the worst and the best
	 * machines in the factory.
	 * 
	 * @return the interval of the productivities.
	 */
	public Range getProductivityInterval() {
		return new Range(worstMachine.getProductivity(),
				bestMachine.getProductivity());
	}

	/**
	 * Return the value of unfinished goods stock within the factory.
	 * 
	 * @return the value of the unfinished goods.
	 */
	public long getUnfinishedGoodsValue() {
		long value = 0;
		for (Machine m : machines) {
			value += m.getUnfinishedGoodsValue();
		}
		return value;
	}

	/**
	 * Production function of the factory.<br>
	 * Summons each employee and makes him work on a machine.
	 * 
	 * @param employees
	 *            the payroll.
	 */
	public Goods produceGoods(Labor l) {
		Machine machine = getFirstUnusedMachine();
		if (machine == null) {
			return new Goods();
		}
		Goods product = machine.produceGoods(l);
		if (product == null) {
			return new Goods();
		}
		return product;
	}

	private Machine getFirstUnusedMachine() {
		for (Machine m : machines) {
			if (m.isUseable()) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Sets the production cycle time.<br>
	 * The production cycle time of each machine within the factory is modified.
	 * 
	 * @param prodTime
	 *            the production cycle time to set.
	 */
	public void setProductionTime(int prodTime) {
		for (Machine m : machines) {
			m.setProductionTime(prodTime);
		}
	}

	/**
	 * Sets the production cycle time.<br>
	 * The production cycle time of each machine within the factory is modified.
	 * 
	 * @param prodTime
	 *            the production cycle time to set.
	 */
	public int getAverageProductionTime() {
		return averageProductionTime;
	}

	/**
	 * Tools up the factory with new machines.
	 * 
	 * @param machines
	 *            the number of the new machines.
	 * @param productivityMin
	 *            the minimum productivity of the new machines.
	 * @param productivitymax
	 *            the maximum productivity of the new machines.
	 * @param productionTime
	 *            the production cycle time.
	 */
	/*
	 * TODO: move or delete!!! public void addMachines(int amountOfMachines,
	 * Interval productivityInterval, int productionTime) { for (int i = 0; i <
	 * amountOfMachines; i++) { machines.add(new Machine(getCircuit(), (int)
	 * (productivityInterval .getMin() + (i * (productivityInterval.length())) /
	 * (amountOfMachines - 1)), productionTime));// TODO:si // agregas una //
	 * sola maquina, // da /0! } }
	 */

	public int getAmountOfMachines() {
		return machines.size();
	}

}