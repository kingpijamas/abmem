package jamel.agents.firms;

public interface PolicyMaker {
	public double getDemandPricingVsMarkupPricing();

	public double getPriceFlexibility();

	public long getReserveTarget();

	public long getExpectedMarkup();

	public int getStockTarget();
}
