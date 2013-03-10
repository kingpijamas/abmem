package economicCycle;

import org.joda.time.DateTime;

public abstract class CycleElement {
	private int birthPeriod;
	private Cycle circuit;

	public CycleElement(Cycle circuit) {
		this.circuit = circuit;
		this.birthPeriod = circuit.getPeriodsFromStart();
	}

	protected int getBirth() {
		return birthPeriod;
	}

	public int getAgeInPeriods() {
		return getCycle().getPeriodsFromStart() - getBirth();
	}

	protected DateTime getDate() {
		return getCycle().getDate();
	}

	public Cycle getCycle() {
		return circuit;
	}
}
