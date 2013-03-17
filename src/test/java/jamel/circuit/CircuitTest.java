package jamel.circuit;

import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import economicCycle.EconomicCycle;
import economicCycle.scheduling.SimulationEvent;

public class CircuitTest {

	private static final DateTime START = new DateTime(0);
	private static final DateTime END = new DateTime(1000);
	private static final Period STEP = new Period(1);

	private static EconomicCycle sut;

	@Before
	public void setUp() throws Exception {
		sut = new EconomicCycle(START, END, STEP);
	}

	@Test
	public void testDoPeriod() {
		sut.addEvent(new TestDoPeriod(), START);
		sut.doPeriod();
		if (!TestDoPeriod.executed) {
			fail();
		}
	}

	@Test
	public void testGetPeriodsFromStart() {
		int i = 0;
		while (sut.ended()) {
			sut.doPeriod();
			i++;
		}
		if (i != sut.getPeriodsFromStart()) {
			fail();
		}
	}

	static class TestDoPeriod extends SimulationEvent {
		static boolean executed = false;

		@Override
		public void execute() {
			executed = true;
		}
	}

}
