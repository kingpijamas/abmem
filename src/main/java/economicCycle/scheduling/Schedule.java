package economicCycle.scheduling;

import java.util.PriorityQueue;

import org.joda.time.DateTime;
import org.joda.time.ReadablePeriod;

public class Schedule {
	private static final int REPEATCONSTANTLY = -1;

	private int index;
	private PriorityQueue<ScheduleNode> pq;

	public Schedule() {
		pq = new PriorityQueue<ScheduleNode>();
	}

	/**
	 * Creates a new event and adds it to the schedule.
	 */
	public void add(SimulationEvent event, DateTime date) {
		add(event, date, getLastPriority());
	}

	/**
	 * Creates a new event and adds it to the schedule.
	 */
	public void add(SimulationEvent event, DateTime date, double priority) {
		add(event, date, priority, null, 1);
	}

	/**
	 * Creates a new event and adds it to the schedule.
	 */
	public void add(SimulationEvent event, DateTime date, double priority,
			ReadablePeriod repeatFrequency, int repetitions) {
		pq.add(new ScheduleNode(event, date, priority, repeatFrequency,
				repetitions));
	}

	public void add(SimulationEvent se, DateTime start,
			ReadablePeriod repeatEvery) {
		add(se, start, repeatEvery, REPEATCONSTANTLY);
	}

	/**
	 * IMPORTANT: Note that repetitions are just that: repetitions.
	 * 
	 * @param event
	 * @param start
	 * @param repeatFrequency
	 * @param repetitions
	 *            : amount of times that the event is expected to be REPEATED
	 *            (that is, if repetitions = 0, the event will be run ONCE. If
	 *            repetitions = 1, the event will be run TWICE)
	 */
	public void add(SimulationEvent event, DateTime start,
			ReadablePeriod repeatFrequency, int repetitions) {
		add(event, start, getLastPriority(), repeatFrequency, repetitions);
	}

	/**
	 * Executes the next event. Returns the index of the event that was
	 * processed.
	 */
	public boolean doNextEvent() {
		System.out.println("--------------\n");
		if (pq.isEmpty()) {
			return true;
		}
		ScheduleNode sn = pq.poll();
		if (sn.repeatFrequency != null) {
			if (sn.repetitions == REPEATCONSTANTLY) {
				pq.offer(new ScheduleNode(sn.event, sn.date
						.plus(sn.repeatFrequency), sn.priority,
						sn.repeatFrequency, REPEATCONSTANTLY));
			} else if (sn.repetitions > 0) {
				pq.offer(new ScheduleNode(sn.event, sn.date
						.plus(sn.repeatFrequency), sn.priority,
						sn.repeatFrequency, sn.repetitions - 1));
			}
		}
		// System.out.println(pq);
		System.out.println(sn);
		sn.event.execute();
		if (++index == pq.size()) {
			index = 0;
			return true;
		}
		return false;
	}

	public int getLastPriority() {
		return pq.size();
	}

	@Override
	public String toString() {
		return pq.toString();
	}

	private static class ScheduleNode implements Comparable<ScheduleNode> {
		DateTime date;
		SimulationEvent event;
		double priority;
		ReadablePeriod repeatFrequency;
		int repetitions;

		ScheduleNode(SimulationEvent event, DateTime date, double priority,
				ReadablePeriod repeatFrequency, int repetitions) {
			this.date = date;
			this.event = event;
			this.priority = priority;
			this.repeatFrequency = repeatFrequency;
			this.repetitions = repetitions;
		}

		public int compareTo(ScheduleNode o) {
			int comp = date.compareTo(o.date);
			if (comp != 0) {
				return comp;
			}
			return Double.compare(priority, o.priority);
		}

		@Override
		public String toString() {
			return "Priority: "
					+ priority
					+ "\nEvent: "
					+ event
					+ "\nfreq: "
					+ repeatFrequency
					+ "\nrepeat: "
					+ ((repetitions == REPEATCONSTANTLY) ? "CONSTANTLY"
							: repetitions);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((date == null) ? 0 : date.hashCode());
			result = prime * result + ((event == null) ? 0 : event.hashCode());
			long temp;
			temp = Double.doubleToLongBits(priority);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime
					* result
					+ ((repeatFrequency == null) ? 0 : repeatFrequency
							.hashCode());
			result = prime * result + repetitions;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ScheduleNode other = (ScheduleNode) obj;
			if (date == null) {
				if (other.date != null)
					return false;
			} else if (!date.equals(other.date))
				return false;
			if (event == null) {
				if (other.event != null)
					return false;
			} else if (!event.equals(other.event))
				return false;
			if (Double.doubleToLongBits(priority) != Double
					.doubleToLongBits(other.priority))
				return false;
			if (repeatFrequency == null) {
				if (other.repeatFrequency != null)
					return false;
			} else if (!repeatFrequency.equals(other.repeatFrequency))
				return false;
			if (repetitions != other.repetitions)
				return false;
			return true;
		}
	}

}
