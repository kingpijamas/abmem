/** 
 * ===================================================
 * JAMEL : a Java Agent-based MacroEconomic Main.
 * ===================================================
 * 
 * (C) Copyright 2007-2010, by Pascal Seppecher.
 * 
 * Project Info:  http://p.seppecher.free.fr/jamel/ 
 */

package jamel.markets.goods;

import jamel.markets.Offerable;
import jamel.spheres.productive.ProductionProcess;

/**
 * Represents a volume of commodities.
 * <p>
 * Encapsulates two numbers that represent a volume of commodities and its
 * value. A nonempty volume of commodities can be created by a
 * {@link ProductionProcess}, at the end of the production cycle. A nonempty
 * volume of commodities can be partially or totally transfered from a Commodity
 * object to another. A nonempty volume of commodities is destroyed in the act
 * of consumption.
 * <p>
 * Last update: 12-Dec-2010
 */
public class Goods extends Offerable {

	private static final long MIN_VALUE = 0;

	/**
	 * Creates an empty volume of commodities.
	 */
	public Goods() {
		super(true, 0, 0);
	}

	/**
	 * Creates a nonempty volume of commodities from a production-process.<br>
	 * The production-process is cancelled. This constructor is used to simulate
	 * the production of new commodities.
	 * 
	 * @param productionProcess
	 *            a ProductionProcess.
	 */
	public Goods(int volume, long value) {
		super(true, volume, value);
	}

	/**
	 * Adds a volume of commodities to the current volume.<br>
	 * The total volume from the source is transfered.
	 * 
	 * @param source
	 *            the source volume.
	 * @param value
	 *            the value added with the transfer.
	 */
	public Goods add(Goods other) {
		if (other.getVolume() == 0) {// TODO
			return this;
		}
		Goods ans = new Goods((int) Math.max(MIN_VALUE,
				getVolume() + other.getVolume()),
				(getValue() + other.getValue())
						/ (getVolume() + other.getVolume()));
		this.consumeAll();
		other.consumeAll();
		return ans;
	}

	public void addGoodsAdding(long unfinishedGoodsValue) {
		addVolume((int) (unfinishedGoodsValue / getUnitValue()));
	}

	@Override
	public long getValue() {
		return getUnitValue() * getVolume();
	}

	public Goods getSuboffer(long totalSubofferValue) {
		if (totalSubofferValue == 0) {// TODO: FIXME
			return new Goods();
		}
		subtractVolume(totalSubofferValue / getUnitValue());
		return new Goods((int) (totalSubofferValue / getUnitValue()),
				totalSubofferValue);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + super.toString();
	}

}