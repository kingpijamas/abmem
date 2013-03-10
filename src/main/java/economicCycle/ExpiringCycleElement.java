package economicCycle;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;

public abstract class ExpiringCycleElement extends CycleElement {

	private DateTime expiration;
	private ReadablePeriod term;

	private ExpiringCycleElement(Cycle circuit, DateTime expiration,
			ReadablePeriod term) {
		super(circuit);
		this.expiration = expiration;
		this.term = term;
	}

	public ExpiringCycleElement(Cycle circuit, DateTime expiration) {
		this(circuit, expiration, new Period(circuit.getDate(), expiration));
	}

	public ExpiringCycleElement(Cycle circuit, ReadablePeriod term) {
		this(circuit, circuit.getDate().plus(term), term);
	}

	public DateTime getExpiration() {
		return expiration;
	}

	public boolean isExpirationTime() {
		return getDate().equals(expiration);
	}

	protected ReadablePeriod getTerm() {
		return term;
	}

	protected void extendExpiration() {
		expiration = expiration.plus(term);
	}

	protected abstract void expire();
}
