package jamel.circuit;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Before;
import org.junit.Test;

import economicCycle.scheduling.Schedule;
import economicCycle.scheduling.SimulationEvent;

public class SchedulerTest {

	private Schedule sut;

	@Before
	public void setUp() throws Exception {
		TestRepeatingEvent.executions = 0;
		TestRepeatingEventWithPriority.priorities = new LinkedList<TestRepeatingEventWithPriority>();
		this.sut = new Schedule();
	}

	private void testAll(List<TestEvent> orderedList) {
		for (int i = 0; i < orderedList.size(); i++) {
			sut.doNextEvent();
			for (int j = i; j < orderedList.size(); j++) {
				if (orderedList.get(j).executed) {
					if (j != i) {
						fail();
					}
				}
			}
		}
	}

	@Test
	public void testAddSimulationEventDateTime() {
		final int size = 3;
		List<TestEvent> l = new ArrayList<TestEvent>(size);
		for (int i = 0; i < size; i++) {
			l.add(new TestEvent());
			sut.add(l.get(i), new DateTime(i));
		}
		testAll(l);
	}

	@Test
	public void testAddSimulationEventDateTimeInt() {
		final int size = 3;
		List<TestEvent> l = new ArrayList<TestEvent>(size);
		for (int i = 0; i < size; i++) {
			l.add(new TestEvent());
			sut.add(l.get(i), new DateTime(0), size - i);
		}
		Collections.reverse(l);
		testAll(l);
	}

	@Test
	public void testAddSimulationEventDateTimeReadablePeriod() {
		final int size = 3;
		sut.add(new TestRepeatingEvent(), new DateTime(0), Days.ONE);
		for (int i = 0; i < size; i++) {
			sut.doNextEvent();
			if (TestRepeatingEvent.executions < i) {
				fail();
			}
		}
	}

	@Test
	public void testAddSimulationEventDateTimeReadablePeriodInt() {
		final int size = 3;
		final int repetitions = (size / 2);
		sut.add(new TestRepeatingEvent(), new DateTime(0), Days.ONE,
				repetitions);
		for (int i = 0; i < size * 2; i++) {
			sut.doNextEvent();
		}
		if (TestRepeatingEvent.executions != repetitions + 1) {
			fail();
		}
	}

	@Test
	public void testAddSimulationEventDateTimeIntReadablePeriodInt() {
		final int size = 3;
		final int repetitions = 4;
		for (int i = 0; i < size; i++) {
			sut.add(new TestRepeatingEventWithPriority(i), new DateTime(0),
					Days.ONE, repetitions);
		}
		for (int i = 0; i < size * repetitions * 2; i++) {
			sut.doNextEvent();
		}
		for (TestRepeatingEventWithPriority ev : TestRepeatingEventWithPriority.priorities) {
			if (ev.executions != repetitions + 1) {
				fail();
			}
		}
	}

	static class TestEvent extends SimulationEvent {
		boolean executed = false;

		@Override
		public void execute() {
			executed = true;
		}

		@Override
		public String toString() {
			return String.valueOf(executed);
		}
	}

	static class TestRepeatingEvent extends SimulationEvent {
		static int executions = 0;

		@Override
		public void execute() {
			executions++;
		}
	}

	static class TestRepeatingEventWithPriority extends SimulationEvent {
		static List<TestRepeatingEventWithPriority> priorities = new LinkedList<TestRepeatingEventWithPriority>();
		int executions = 0;
		private int priority;
		boolean locked = false;

		public TestRepeatingEventWithPriority(int priority) {
			if (priority < priorities.size()) {
				throw new IllegalArgumentException("Repeating priorities");
			}
			this.priority = priority;
			priorities.add(this.priority, this);
		}

		@Override
		public void execute() {
			if (locked) {
				fail();
			}
			executions++;
		}
	}
}
