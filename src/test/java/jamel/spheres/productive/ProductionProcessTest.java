/**
 * 
 */
package jamel.spheres.productive;

import static org.junit.Assert.fail;
import jamel.markets.goods.Goods;
import jamel.markets.labor.Labor;
import jamel.spheres.productive.ProductionProcess;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import economicCycle.EconomicCycle;
import economicCycle.scheduling.SimulationEvent;

/**
 * @author jp
 * 
 */
public class ProductionProcessTest {

	private static final int PRODUCTIVITY = 1;
	private static final int PRODUCTIONTIME = 2;

	private static final DateTime START = new DateTime(0);
	private static final Period STEP = Days.ONE.toPeriod();
	private static final DateTime END = START.plus(STEP
			.multipliedBy(PRODUCTIONTIME));
	private static final int WAGE = 1000;

	private static ProductionProcess sut;

	@Before
	public void setUp() throws Exception {
		sut = new ProductionProcess(new EconomicCycle(START, END, STEP), PRODUCTIVITY,
				PRODUCTIONTIME);
	}

	/**
	 * Test method for
	 * {@link jamel.spheres.productive.ProductionProcess#produce(jamel.markets.labor.Labor)}
	 * .
	 */
	@Test
	public void testProduce() {
		EconomicCycle c = sut.getCycle();
		c.addEvent(new Produce(), START, c.getLastPriority(), 1,
				PRODUCTIONTIME - 1);

		int periods = 0;
		while (!c.ended()) {
			c.doPeriod();
			periods = c.getPeriodsFromStart();
			if (periods > PRODUCTIONTIME
					&& (Produce.product.getValue() != WAGE * periods || Produce.product
							.getVolume() != PRODUCTIVITY)) {
				fail();
			}
		}
	}

	static class Produce extends SimulationEvent {
		static Goods product;

		@Override
		public void execute() {
			System.out.println();
			Goods p = sut.produce(new Labor(WAGE));
			System.out.println(p);
			if (p != null) {
				if (product == null) {
					product = p;
				} else {
					product = product.add(p);
				}
			}
		}
	}
}
