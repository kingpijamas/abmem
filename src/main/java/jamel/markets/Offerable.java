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

/**
 * A base class for offers on markets.
 * <p>
 * Encapsulates a volume offered and a unit price.
 * <p>
 * Last update: 8-Dec-2010.
 */
public abstract class Offerable {

	private long unitValue;
	/** The offered volume. */
	private int volume;
	private final boolean fractionable;

	/**
	 * Creates a new offer.
	 * 
	 * @param offerer
	 *            the agent author of the offer.
	 * @param volume
	 *            the offered volume.
	 * @param pricePerUnit
	 *            the unit price of the offer.
	 */
	public Offerable(boolean fractionable, int volume, long pricePerUnit) {
		this.volume = volume;
		this.unitValue = pricePerUnit;
		this.fractionable = fractionable;
		if (pricePerUnit < 0) {
			throw new IllegalArgumentException("ACA");
		}
	}

	public int getVolume() {
		return volume;
	}

	public long getUnitValue() {
		return unitValue;
	}

	protected void subtractVolume(double volume) {
		if (volume > this.volume) {
			throw new IllegalArgumentException(
					"The volume to subtract exceeds the total volume of this offer");
		}
		this.volume -= volume;
	}

	protected void addVolume(int volume) {
		this.volume += volume;
	}

	public long getValue() {
		return getVolume() * getUnitValue();
	}

	/**
	 * Consumes the total volume of commodities.
	 */
	public void consumeAll() {
		subtractVolume(getVolume());
	}

	public boolean isFractionable() {
		return fractionable;
	}

	@Override
	public String toString() {
		return "\nVolume: " + volume + "\nUnitValue: " + unitValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fractionable ? 1231 : 1237);
		result = prime * result + (int) (unitValue ^ (unitValue >>> 32));
		result = prime * result + volume;
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
		Offerable other = (Offerable) obj;
		if (fractionable != other.fractionable)
			return false;
		if (unitValue != other.unitValue)
			return false;
		if (volume != other.volume)
			return false;
		return true;
	}
}
