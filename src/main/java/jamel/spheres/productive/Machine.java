/* =========================================================
 * JAMEL : a Java (tm) Agent-based MacroEconomic Main.
 * =========================================================
 *
 * (C) Copyright 2007-2011, Pascal Seppecher.
 * 
 * Project Info <http://p.seppecher.free.fr/jamel/>. 
 *
 * This file is a part of JAMEL (Java Agent-based MacroEconomic Main).
 * 
 * JAMEL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JAMEL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JAMEL. If not, see <http://www.gnu.org/licenses/>.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */

package jamel.spheres.productive;

import jamel.markets.goods.Goods;
import jamel.markets.labor.Labor;
import economicCycle.Cycle;
import economicCycle.RegularUseCycleElement;

/**
 * Represents a machine.
 * <p>
 * A Machine is an object which products commodities by an iterative process of
 * labour-power expenditure. Each time a {@link LaborPower} is provided to the
 * machine, the associated {@link ProductionProcess} is incremented. At the end
 * of the production cycle, the product is available as a new nonempty
 * {@link Goods} object and it can be provided to the {@link Factory}.
 * <p>
 * Last update: 19-Jun-2011
 */
public class Machine extends RegularUseCycleElement implements
		Comparable<Machine> {

	/** The production process. */
	private ProductionProcess process;
	/** The production cycle time. */
	private int productionTime;
	private int productivity;

	/**
	 * Creates a new machine with the given productivity.
	 * 
	 * @param factory
	 *            the factory.
	 * @param productivity
	 *            the productivity.
	 * @param productionTime
	 *            the production cycle time.
	 */
	public Machine(Cycle circuit, int productivity, int productionTime) {
		super(circuit);
		this.productivity = productivity;
		this.productionTime = productionTime;
		this.process = new ProductionProcess(getCycle(), productivity,
				productionTime);
	}

	@Override
	public String toString() {
		String ans = "\n" + getClass().getName();
		ans += "\nProductivity=" + productivity;
		ans += "\nProductionTime=" + productionTime;
		return ans;
	}

	/**
	 * Changes the productivity of the machine.
	 * 
	 * @param alpha
	 *            the change ratio.
	 */
	public void changeProductivity(double ratio) {
		productivity *= ratio;
	}

	/**
	 * Returns the value of the current production process (= the sum of wages
	 * payed).
	 * 
	 * @return the value of the current production process.
	 */
	// TODO: this method was used when firms go bankrupt, why isn't it used
	// anymore??
	public long getUnfinishedGoodsValue() {
		return process.getValue();
	}

	/**
	 * Returns the production cycle time.
	 * 
	 * @return an integer that represents the production cycle time.
	 */
	public int getProductionTime() {
		return productionTime;
	}

	/**
	 * The productivity (= the volume of commodities that the machine can
	 * product on average in one period).
	 */
	public int getProductivity() {
		return productivity;
	}

	/**
	 * Ends a production cycle.<br>
	 * The new product from the completed production process is sent to the
	 * factory. The production process is canceled.
	 * 
	 * @param product
	 *            the new product.
	 */
	public Goods produceGoods(Labor l) {
		use();
		Goods product = process.produce(l);
		if (product != null) {
			process = new ProductionProcess(getCycle(), getProductivity(),
					getProductionTime());
		}
		return product;
	}

	/**
	 * Sets the production cycle time.
	 * 
	 * @param time
	 *            the production cycle time.
	 */
	public void setProductionTime(int time) {
		productionTime = time;
	}

	/**
	 * Implements the Comparable interface so that machines can easily be
	 * sorted.
	 * 
	 * @param otherMachine
	 *            the machine to compare against.
	 */
	public int compareTo(Machine otherMachine) {
		if (getProductivity() != otherMachine.getProductivity()) {
			return getProductivity() - otherMachine.getProductivity();
		}
		if (process.getProgress() != otherMachine.process.getProgress()) {
			return process.getProgress() - otherMachine.process.getProgress();
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result + productionTime;
		result = prime * result + productivity;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Machine other = (Machine) obj;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		if (productionTime != other.productionTime)
			return false;
		if (productivity != other.productivity)
			return false;
		return true;
	}

}