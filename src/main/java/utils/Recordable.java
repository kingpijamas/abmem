package utils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import scheduling.cycle.Cycle;
import scheduling.cycle.CycleElement;

public class Recordable<V extends Number> extends CycleElement {

	private static final List<Recordable<?>> instances = new LinkedList<Recordable<?>>();
	private static final int DEFAULT_MAX_STEPS_PER_RECORD = 30;
	private Deque<V> records;
	private V start; // TODO: cable aca. Se terminaba quedando sin valor por el
						// polleo, lo cual tiraba exception.
	private int maxStepsPerRecord;
	private int step;

	@Override
	public String toString() {
		return records.toString();
	}

	public Recordable(Cycle cycle, V start) {
		this(cycle, DEFAULT_MAX_STEPS_PER_RECORD, start);
	}

	public Recordable(Cycle cycle, int maxStepsPerRecord, V start) {
		super(cycle);
		this.records = new LinkedList<V>();
		this.start = start;
		setValue(start);
		instances.add(this);
		this.step = 0;
		this.maxStepsPerRecord = maxStepsPerRecord;
	}

	public V getValue() {
		return records.getLast();
	}

	public void setValue(V value) {
		records.offer(value);
	}

	public double getAverageValue() {
		return getTotal() / ((double) records.size());
	}

	public double getTotal() {
		double sum = 0;
		for (V rec : records) {
			sum += rec.doubleValue();
		}
		return sum;
	}

	public double getCurrentValue() {
		if (records.isEmpty()) {
			records.offer(start);
			return start.doubleValue();
		}
		return records.getLast().doubleValue();
	}

	public static class PollRecordables extends
			scheduling.schedule.SimulationEvent {
		/**
		 * Dumps the last recorded value, so as to guarantee they have a maximum
		 * age of maxStepsPerRecord
		 */
		@Override
		public void execute() {
			for (Recordable<?> r : instances) {
				if (r.step == r.maxStepsPerRecord) {
					r.step = 0;
					r.records.poll();
				} else {
					r.step++;
				}
			}
		}
	}
}
