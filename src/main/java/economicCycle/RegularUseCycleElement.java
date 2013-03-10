package economicCycle;

import jamel.spheres.monetary.exceptions.UnexpectedInvocationException;

import org.joda.time.DateTime;

public abstract class RegularUseCycleElement extends CycleElement {

	private DateTime lastUse;

	public RegularUseCycleElement(Cycle circuit) {
		super(circuit);
		this.lastUse = null;
	}

	public void use() {
		if (!isUseable()) {
			throw new UnexpectedInvocationException(
					"Already used in the current period.");
		}
		lastUse = getDate();
	}

	public DateTime getLastUse() {
		return lastUse;
	}

	public boolean isUseable() {
		return lastUse == null || lastUse.compareTo(getDate()) < 0;
	}
}
