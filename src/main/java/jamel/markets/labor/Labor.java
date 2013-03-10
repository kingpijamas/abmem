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

package jamel.markets.labor;

import jamel.markets.Offerable;

/**
 * Represents an offer on a labour market.
 * <p>
 * Last update: 8-Dec-2010.
 */
public class Labor extends Offerable implements Comparable<Labor> {

	private static final int DEFAULT_LABOR_UNITS = 1;

	public Labor(long wage) {
		this(DEFAULT_LABOR_UNITS, wage);
	}

	public Labor(int laborUnits, long wage) {
		super(false, laborUnits, wage);
	}

	/**
	 * Implements the Comparable interface so that offers can easily be sorted.
	 * 
	 * @param otherOffer
	 *            the offer to compare against.
	 * @return <code>-1</code> if the price of the other offer is less than
	 *         this, <code>0</code> if both have the same price and
	 *         <code>1</code> this price is less than the other.
	 */
	public int compareTo(Labor otherOffer) {
		return (int) (getUnitValue() * getVolume() - otherOffer.getUnitValue()
				* otherOffer.getVolume());
	}

	public long getWage() {
		return getUnitValue() * getVolume();
	}
}
