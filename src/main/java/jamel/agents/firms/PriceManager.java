package jamel.agents.firms;

import jamel.spheres.productive.DemandsTendencyEstimator;
import jamel.utils.JamelRandom;
import jamel.utils.Tendency;

public class PriceManager {
	private static final long MINPRICE = 1;

	private DemandsTendencyEstimator demandsTendencyEstimator;
	private PolicyMaker policy;

	private long currentPrice;

	public PriceManager(PolicyMaker policy, DemandsTendencyEstimator estimator,
			long startingPrice) {
		this.currentPrice = startingPrice;
		this.policy = policy;
		this.demandsTendencyEstimator = estimator;
	}

	private void setCostPrice(long unitCost) {
		this.currentPrice = (long) ((1 + new JamelRandom().nextFloat()) * unitCost);
	}

	private void reducePrice(float alpha) {
		this.currentPrice = (long) Math.max(MINPRICE, currentPrice
				* (1f - alpha * policy.getPriceFlexibility()));
	}

	private void raisePrice(float alpha) {
		this.currentPrice = (long) (currentPrice * (1f + alpha
				* policy.getPriceFlexibility()));
	}

	private void demandPricing(int currentStock, float alpha1, float alpha2) {
		Tendency demandsTendency = demandsTendencyEstimator.getDemandsTendency(
				currentStock, alpha1, alpha2);
		if (demandsTendency.isIncreasing()) {
			raisePrice(alpha1);
		} else if (!demandsTendency.isConstant()) {
			reducePrice(alpha1);
		}
	}

	/**
	 * Returns the price that satisfies the markup goal.
	 * 
	 * @return a double that represent the price.
	 */
	private double getMarkupPriceGoal(long unitCost) {
		return unitCost * policy.getExpectedMarkup();
	}

	/**
	 * Determines the new price using the markup goal pricing procedure.
	 */
	private void markupPricing(long unitCost, float alpha1, float alpha2) {
		double targetprice = getMarkupPriceGoal(unitCost);
		if (unitCost == targetprice) {
			return;
		}
		if (alpha1 * alpha2 < 1 - this.currentPrice / targetprice) {
			this.raisePrice(alpha1);
			currentPrice = (long) Math.max(currentPrice, targetprice);
			return;
		} else if (alpha1 * alpha2 < this.currentPrice / targetprice - 1) {
			this.reducePrice(alpha1);
			currentPrice = (long) Math.max(currentPrice, targetprice);
			return;
		}
	}

	/**
	 * Updates the unit price.<br>
	 * Chooses between two procedures : the demand-pricing procedure, based on
	 * the inventory normal level, the markup-pricing procedure, based on the
	 * markup normal level.
	 * 
	 * @return a double that represent the new price.
	 */
	public void updatePrice(long unitCost, int currentStock) {
		if (currentPrice == 0) {
			setCostPrice(unitCost);
			return;
		}
		float alpha1 = new JamelRandom().nextFloat();
		float alpha2 = new JamelRandom().nextFloat();
		if (policy.getDemandPricingVsMarkupPricing() > new JamelRandom()
				.nextDouble()) {
			markupPricing(unitCost, alpha1, alpha2);
		} else {
			demandPricing(currentStock, alpha1, alpha2);
		}
	}

}
