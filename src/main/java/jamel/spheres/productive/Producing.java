package jamel.spheres.productive;

import jamel.markets.goods.Goods;
import jamel.markets.labor.Labor;

public interface Producing<T extends Goods> {
	public T produceGoods();

	public int getWorkforceRequirement();

	public void planProduction(int currentInventoryVolume);

	public long getUnitCost();

	public void addLabor(Labor l);
}
