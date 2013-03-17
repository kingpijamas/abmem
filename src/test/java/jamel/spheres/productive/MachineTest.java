package jamel.spheres.productive;

import static org.junit.Assert.fail;
import jamel.markets.goods.Goods;
import jamel.markets.labor.Labor;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import scheduling.cycle.Cycle;
import scheduling.schedule.SimulationEvent;
import economicCycle.EconomicCycle;

public class MachineTest {

	private static final int PRODUCTIVITY = 1;
	private static final int PRODUCTIONTIME = 2;

	private static final int PRODUCTIONCYCLES = 10;

	private static final DateTime START = new DateTime(0);
	private static final Period STEP = Days.ONE.toPeriod();
	private static final DateTime END = START.plus(STEP
			.multipliedBy(PRODUCTIONTIME * PRODUCTIONCYCLES));
	private static final int WAGE = 1000;

	private static Machine sut;

	@Before
	public void setUp() throws Exception {
		sut = new Machine(new EconomicCycle(START, END, STEP), PRODUCTIVITY,
				PRODUCTIONTIME);
		Produce.product = null;
	}

	@Test
	public void testProduceGoods() {
		Cycle c = sut.getCycle();
		c.addEvent(new Produce(), START, c.getLastPriority(), 1,
				(PRODUCTIONTIME * PRODUCTIONCYCLES) - 1);
		int periods = 0;
		while (!c.ended()) {
			c.doPeriod();
			periods = c.getPeriodsFromStart();
			if (periods > PRODUCTIONTIME
					&& periods % PRODUCTIONTIME == 0
					&& (Produce.product.getValue() != WAGE * periods || Produce.product
							.getVolume() != PRODUCTIVITY
							* (periods / PRODUCTIONTIME))) {
				fail();
			}
		}
	}

	@Test
	public void testGetUnfinishedGoodsValue() {
		Cycle c = sut.getCycle();
		c.addEvent(new Produce(), START, c.getLastPriority(), 1,
				(PRODUCTIONTIME * PRODUCTIONCYCLES) - 1);
		int prev = 0;
		int periods = 0;
		while (!c.ended()) {
			c.doPeriod();
			prev = periods;
			periods = c.getPeriodsFromStart();
			if (prev != periods
					&& periods % PRODUCTIONTIME != 0
					&& sut.getUnfinishedGoodsValue() != (periods % PRODUCTIONTIME)
							* WAGE) {
				fail();
			}
		}
	}

	static class Produce extends SimulationEvent {
		static Goods product;

		@Override
		public void execute() {
			System.out.println();
			Goods p = sut.produceGoods(new Labor(WAGE));
			if (p != null) {
				if (product == null) {
					product = p;
				} else {
					product = product.add(p);
				}
			}
			System.out.println(product);
		}
	}

}
