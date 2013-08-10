/* =========================================================
 * JAMEL : a Java (tm) Agent-based MacroEconomic Main.
 * =========================================================
 *
 * (C) Copyright 2007-2010, Pascal Seppecher.
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

package jamel.markets;


import java.util.HashSet;
import java.util.Set;

import scheduling.cycle.CycleElement;
import utils.StatisticalTransientNumber;
import economy.EconomicCycle;

/**
 * A base class providing common services for markets.
 * <p>
 * In Jamel, there is no auctioneer, and markets are non-clearing markets.
 * <p>
 * Last update: 9-Dec-2010.
 */
public abstract class Market<T extends Offerable, U extends Demanding<T>>
		extends CycleElement {

	private Set<Offering<T>> supply;
	private Set<U> demand;

	private StatisticalTransientNumber volume;
	private StatisticalTransientNumber value;

	/** The average price of the previous period. */
	private double lastPrice;

	public Market(EconomicCycle circuit) {
		super(circuit);
		this.supply = new HashSet<Offering<T>>();
		this.demand = new HashSet<U>();
		this.volume = new StatisticalTransientNumber(getCycle(), 0.0, 1, 0);
		this.value = new StatisticalTransientNumber(getCycle(), 0.0, 1, 0);
	}

	protected void updateValue(long change) {
		value.add(change);
	}

	protected void updateVolume(int change) {
		volume.add(change);
	}

	protected void runDemand() {
		for (U d : demand) {
			supply(d);
		}
	}

	protected abstract void supply(U d);

	protected void runSupply() {
		for (Offering<T> off : supply) {
			off.prepareOffer();
		}
	}

	public boolean offererExists(Offering<T> offerer) {
		return supply.contains(offerer);
	}

	public void run() {
		lastPrice = getAverageValue();
		runSupply();
		runDemand();
	}

	public long getTotalValue() {
		return (long) value.getValue();
	}

	public int getTotalVolume() {
		return (int) volume.getValue();
	}

	public long getAverageValue() {
		if (volume.getValue() == 0) {
			return 0;
		}
		return (long) (value.getValue() / volume.getValue());
	}

	/**
	 * Returns the inflation rate for the period.
	 * 
	 * @return a double that represents the inflation rate.
	 */
	public double getInflation() {
		if (Double.isNaN(lastPrice)) {
			return Double.NaN;
		}
		return (100. * (getAverageValue() - lastPrice)) / lastPrice;
	}

	protected Set<Offering<T>> getOfferers() {
		return supply;
	}

	public void add(Offering<T> offerer) {
		supply.add(offerer);
		value.setPollsize(value.getPollSize() + 1);
		volume.setPollsize(volume.getPollSize() + 1);
	}

	public void remove(Offering<T> offerer) {
		supply.remove(offerer);
	}

	public void add(U d) {
		demand.add(d);
	}

	public void remove(U d) {
		demand.remove(d);
	}

	@Override
	public String toString() {
		String ans = "Total volume: "
				+ Integer.valueOf((int) volume.getValue());
		ans += "\nTotal value: " + Integer.valueOf((int) value.getValue());
		ans += "\nOffering(# of entities): " + supply.size();
		ans += "\nDemanding(# of entities): " + demand.size();
		ans += "\nAverage value: " + getAverageValue();
		ans += "\nInflation rate(last period): " + getInflation();
		return ans;
	}
}