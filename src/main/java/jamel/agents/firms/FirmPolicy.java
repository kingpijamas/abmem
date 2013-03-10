package jamel.agents.firms;

import jamel.agents.adaptation.AdaptiveObject;
import jamel.agents.adaptation.AdaptiveVariable;
import jamel.utils.JamelRandom;
import economicCycle.Cycle;
import economicCycle.CycleElement;

public class FirmPolicy extends CycleElement implements PolicyMaker,
		AdaptiveObject {
	protected static final int DEFAULT_MATURITY = 36;
	private static final double DEFAULT_PROFITABILITY = 0.25;
	private static final double DEFAULT_PRICING_POLICY = 0.5;
	private static final int DEFAULT_STOCK_TARGET = 5;
	private static final double DEFAULT_MARKUP_TARGET = 0.2;
	private static final double DEFAULT_RESERVE_TARGET = 0.6;
	private static final double DEFAULT_PRICING_FLEXIBILITY = 0.5;

	private Firm firm;

	private AdaptiveVariable pricingPolicy = new AdaptiveVariable(
			DEFAULT_PRICING_POLICY);
	private AdaptiveVariable inventoryStockTarget = new AdaptiveVariable(
			DEFAULT_STOCK_TARGET);
	private AdaptiveVariable markupTarget = new AdaptiveVariable(
			DEFAULT_MARKUP_TARGET);
	private AdaptiveVariable reserveTarget = new AdaptiveVariable(
			DEFAULT_RESERVE_TARGET);
	private AdaptiveVariable priceFlexibility = new AdaptiveVariable(
			DEFAULT_PRICING_FLEXIBILITY);

	public FirmPolicy(Cycle circuit, Firm firm) {
		super(circuit);
		this.firm = firm;
	}

	public void adaptNewcomer(FirmPolicy parentB, FirmPolicy newcomer) {
		double prefA = new JamelRandom().nextExp();
		newcomer.pricingPolicy.adapt(this.pricingPolicy, parentB.pricingPolicy,
				prefA);
		newcomer.inventoryStockTarget.adapt(this.inventoryStockTarget,
				parentB.inventoryStockTarget, prefA);
		newcomer.markupTarget.adapt(this.markupTarget, parentB.markupTarget,
				prefA);
		newcomer.reserveTarget.adapt(this.reserveTarget, parentB.reserveTarget,
				prefA);
		newcomer.priceFlexibility.adapt(this.priceFlexibility,
				parentB.priceFlexibility, prefA);
	}

	public void changeLoser(FirmPolicy parentB, FirmPolicy loser,
			double audacity, double prefA) {
		loser.pricingPolicy.adapt(pricingPolicy, parentB.pricingPolicy,
				audacity, prefA);
		loser.inventoryStockTarget.adapt(inventoryStockTarget,
				parentB.inventoryStockTarget, audacity, prefA);
		loser.markupTarget.adapt(markupTarget, parentB.markupTarget,
				audacity, prefA);
		loser.reserveTarget.adapt(reserveTarget, parentB.reserveTarget,
				audacity, prefA);
		loser.priceFlexibility.adapt(priceFlexibility,
				parentB.priceFlexibility, audacity, prefA);
	}

	public boolean isMature() {
		return getAgeInPeriods() > DEFAULT_MATURITY;
	}

	public double getPriceFlexibility() {
		return priceFlexibility.getValue();
	}

	public long getReserveTarget() {
		return (long) reserveTarget.getValue();
	}

	public long getExpectedMarkup() {
		return (long) markupTarget.getValue();
	}

	public int getStockTarget() {
		return (int) inventoryStockTarget.getValue();
	}

	public double getDemandPricingVsMarkupPricing() {
		return pricingPolicy.getValue();
	}

	public void adaptNewcomer(AdaptiveObject parentB, AdaptiveObject loser) {
		adaptNewcomer((FirmPolicy) parentB, (FirmPolicy) loser);
	}

	public void changeLoser(AdaptiveObject parentB, AdaptiveObject loser,
			double audacity, double prefA) {
		changeLoser((FirmPolicy) parentB, (FirmPolicy) loser, audacity, prefA);
	}

	public double getFitness(int poolSize) {
		if (this.isMature()) {
			if (poolSize < DEFAULT_MATURITY) {// XXX
				return 0;
			}
			return firm.getProfitability();
		}
		return (long) DEFAULT_PROFITABILITY;
	}
}
