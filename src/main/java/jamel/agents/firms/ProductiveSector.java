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

package jamel.agents.firms;

import jamel.agents.adaptation.AdaptationManager;
import jamel.markets.goods.GoodsMarket;
import jamel.markets.labor.LaborMarket;
import jamel.markets.labor.Wage;
import jamel.spheres.productive.Machine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import scheduling.cycle.CycleElement;
import utils.Range;
import utils.StatisticalTransientNumber;
import economy.Economy;

/**
 * Represents the firms sector.
 * <p>
 * Encapsulates a collection of {@link Firm} agents.
 * <p>
 * Last update: 19-Jun-2011.
 */
public class ProductiveSector extends CycleElement {
	private static final Range VARIABILITYINTERVAL = new Range(0, 1);// TODO:

	private final Economy economy;
	private final List<Firm> firms = new LinkedList<Firm>();

	private double wageUpwardFlexibility;
	private double wageDownwardFlexibility;
	private AdaptationManager<FirmPolicy> adaptationManager;

	private StatisticalTransientNumber totalDividend;
	private int totalProductivity = 0;// TODO: only for debugging purposes

	// private Recordable<Double> prices; TODO: deleted. Reason: apparently
	// unnecessary

	@Override
	public String toString() {
		String ans = "Firms (#): " + firms.size();
		ans += "\nTotal Dividend: " + totalDividend;
		ans += "\nAverage Dividend: " + getAverageDividend();
		ans += "\nTotal productivity: " + totalProductivity;
		ans += "\nAdaptationManager:\n" + adaptationManager;
		return ans;
	}

	/**
	 * Creates a new firms sector.
	 * 
	 * @param laborMarket
	 *            the labor market.
	 * @param goodsMarket
	 *            the goods market.
	 */
	public ProductiveSector(Economy economy, int frequency, double audacity,
			double wageUpwardFlexibility, double wageDownwardFlexibility) {
		super(economy.getCycle());
		this.economy = economy;
		this.wageUpwardFlexibility = wageUpwardFlexibility;
		this.wageDownwardFlexibility = wageDownwardFlexibility;
		/*
		 * this.prices = new Recordable<Double>(getCircuit(), 0.0); TODO:
		 * Original price=0
		 */
		this.adaptationManager = new AdaptationManager<FirmPolicy>(audacity,
				1/* TODO */, frequency);
		this.totalDividend = new StatisticalTransientNumber(0.0, 1, 0);
	}

	public void init() {
		totalDividend.init(economy.getCycle());
	}

	public LaborMarket getLaborMarket() {
		return economy.getLaborMarket();
	}

	public void newFirms(int quantityOfFirms, int productionTimeInPeriods,
			Range productivity, int quantityOfMachines) {
		for (int i = 0; i < quantityOfFirms; i++) {
			List<Machine> machinery = new ArrayList<Machine>(quantityOfMachines);
			for (int m = 0; m < quantityOfMachines; m++) {
				int aux = (int) (Math.random() * productivity.getVariation()
						* productivity.getMax() + productivity.getMin());
				totalProductivity += aux;
				machinery.add(new Machine(getCycle(), aux,
						productionTimeInPeriods));
			}
			firms.add(new Firm(this, new Wage(getLaborMarket()
					.getAverageValue(), wageUpwardFlexibility,
					wageDownwardFlexibility), machinery, /*
														 * // * (long) // *
														 * prices
														 * .getAverageValue // *
														 * () //
														 */
			getGoodsMarket().getAverageValue()));
		}
		// XXX: unused: productivityVariation
		// int currProd = (int) productivity.getMin();
		// Range auxProd = new Range(productivity.getMin(),
		// productivity.getMax(),
		// quantityOfFirms);// XXX: ours
		// for (int i = 0; i < quantityOfFirms; i++) {
		// List<Machine> machinery = new ArrayList<Machine>(quantityOfMachines);
		// for (int m = 0; m < quantityOfMachines; m++) {
		// machinery.add(new Machine(getCycle(), currProd,
		// productionTimeInPeriods));
		// }
		// firms.add(new Firm(this, new Wage(getLaborMarket()
		// .getAverageValue(), wageUpwardFlexibility,
		// wageDownwardFlexibility), machinery, /*
		// * (long)
		// * prices.getAverageValue
		// * ()
		// */
		// getGoodsMarket().getAverageValue()));
		// currProd += auxProd.getStepWidth();
		// }
		totalDividend
				.setPollsize(totalDividend.getPollSize() + quantityOfFirms);
	}

	// /**
	// * Updates data at the end of a period.<br>
	// * Gets the data from each firm and adds them to the sector data.
	// */
	// private void updateData() {
	// for (Firm f : firms) {
	// // Adds individual data to the aggregate data:
	// totalJobOfferings += f.getJobOffers();
	// totalFirmDeposits += f.getDeposits();
	// totalSalesVolume += f.getSalesVolume();
	//
	// totalProductionVolume += f.getProductionVolume();
	//
	// totalWorkforce += f.getWorkforce();
	// totalVacancies += f.getVacancies();
	// totalInventoriesValue += f.getInventoryValue();
	// totalCapital += f.getCapital();
	//
	// prices.setValue(f.getPrice());
	// pricingMarkup.setValue(f.getMarkupPrice());
	// markupRigidity.setValue(f.getMarkupRigidity());
	// profitability.setValue(f.getProfitability());
	// inventoryStocksTarget
	// .setValue((double) f.getInventoryStockTarget());
	// autofinanceTarget.setValue(f.getReserveTarget());
	// }
	// }

	/**
	 * Closes the sector.<br>
	 * Each household is closed. The sector data are updated.
	 */
	public void update() {
		for (Firm f : firms) {
			f.update();
		}
		List<FirmPolicy> behaviors = new ArrayList<FirmPolicy>(firms.size());
		for (Firm selectedFirm : firms) {
			behaviors.add(selectedFirm.getPolicy());
		}
		this.adaptationManager.adaptLosers(behaviors);
	}

	/**
	 * Pays the dividend.<br>
	 * Each firm is called to pay the dividend.
	 */
	public void payDividends() {
		for (Firm selectedFirm : firms) {
			totalDividend.add(selectedFirm.payDividend());
		}
	}

	/**
	 * Executes the production.<br>
	 * Each firm is called to execute the production.
	 */
	public void produce() {
		for (Firm selectedFirm : firms) {
			selectedFirm.prepareOffer();
		}
	}

	public GoodsMarket getGoodsMarket() {
		return economy.getGoodsMarket();
	}

	public Range getFirmBehaviorVariabilityInterval() {
		return VARIABILITYINTERVAL;
	}

	public void reportDividend(long dividend) {
		totalDividend.add(dividend);
	}

	public long getAverageDividend() {
		return (long) (totalDividend.getValue() / firms.size());
	}

	public void addFirm(Firm firm) {
		firms.add(firm);
	}

	public void enterMarkets() {
		for (Firm f : firms) {
			f.enterMarkets();
		}
	}

	public Economy getEconomy() {
		return economy;
	}
}