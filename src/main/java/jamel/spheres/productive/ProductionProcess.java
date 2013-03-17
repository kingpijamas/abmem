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
import scheduling.cycle.Cycle;
import scheduling.cycle.RegularUseElement;
import economicCycle.EconomicCycle;

/**
 * Represents a production process.
 * <p>
 * A production process is incremented by the expenditure of a
 * {@link LaborPower}. When the production process is completed, a new nonempty
 * {@link Goods} object is created.
 * <p>
 * Last update: 19-Jun-2011
 */
public class ProductionProcess extends RegularUseElement {

	final private int productionTime;
	final private double productivity;

	private int progress = 0;
	private long value = 0;

	/**
	 * Creates a new production process.
	 * 
	 * @param machine
	 *            the machine.
	 * @productivity
	 * @productionTime The production cycle time (= the number of successive
	 *                 labour-power expenditures required to complete the
	 *                 process).
	 */
	ProductionProcess(Cycle cycle, int productivity,
			int productionTime) {
		super(cycle);
		this.productivity = productivity;
		this.productionTime = productionTime;
	}

	/**
	 * Returns an number that represents the progress of the production process.
	 * 
	 * @return
	 */
	public int getProgress() {
		return progress;
	}

	public long getValue() {
		return value;
	}

	/**
	 * Increments the production process.<br>
	 * The labour-power is expended(). If the process is completed, a new volume
	 * of commodities is created.<br>
	 * Generates an exception if the process has been already called in the
	 * current period or if the process is already completed or cancelled.
	 * 
	 * @param laborPower
	 *            the labor power to expend.
	 */
	public Goods produce(Labor l) {
		use();
		value += l.getValue();
		l.consumeAll();
		progress++;
		if (progress == productionTime) {
			return new Goods((int) productivity, value);
		}
		return null;
	}

}
