package jamel.markets.goods;

import jamel.markets.Demanding;
import jamel.markets.Offerable;
import jamel.markets.Offering;

public interface Consuming<T extends Offerable> extends Demanding<T> {

	public long getConsumptionBudget();

	public Offering<T> getPreferredProvider();

	public int getMaxMarketCrawlingIntents();
}
