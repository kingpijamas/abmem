package jamel.markets.labor;

import jamel.markets.Offering;
import jamel.spheres.monetary.BankAccount;
import jamel.utils.JamelRandom;
import economicCycle.Cycle;
import economicCycle.RegularUseCycleElement;

public class Worker extends RegularUseCycleElement implements Offering<Labor> {
	private static final int NOT_UNEMPLOYED = -1;
	private static final double DEFAULT_RESISTANCE = 1;
	private static final double DEFAULT_FLEXIBILITY = 1;

	public EmploymentContract contract;// FIXME

	private EmploymentStatus status;
	private int periodsUnemployed;

	private double resistance;
	private double flexibility;

	private long reservationWage;
	private BankAccount account;

	public Worker(Cycle circuit, BankAccount account) {
		super(circuit);
		this.account = account;
		this.periodsUnemployed = 0;
		this.resistance = DEFAULT_RESISTANCE;
		this.flexibility = DEFAULT_FLEXIBILITY;
	}

	public boolean isUnemployed() {
		return contract == null;
	}

	/**
	 * Updates the reservation wage.<br>
	 * The level of the reservation wage depends on the number of periods spent
	 * in a state of unemployment. After a certain time, the unemployed worker
	 * accepts to lower its reservation wage.
	 */
	private void updateReservationWage() {// TODO: "VAlIDATED" since the
											// original one
											// was doing something weird: at
											// some point,
											// periodsUnemployed=random();
											// happened. This
											// implementation does all that the
											// other did with that exception
		if (!isUnemployed()) {
			reservationWage = contract.getWage();
			return;
		}
		periodsUnemployed++;
		if (new JamelRandom().nextDouble() * resistance < periodsUnemployed) {
			reservationWage = (long) (reservationWage * (1d - flexibility
					* new JamelRandom().nextDouble()));
		}
	}

	/**
	 * Receive the notification of the worker hiring.
	 * 
	 * @param contract
	 *            the job contract.
	 */
	public void notifyHiring(EmploymentContract contract) {
		this.contract = contract;
		this.periodsUnemployed = NOT_UNEMPLOYED;
		status = EmploymentStatus.EMPLOYED;
	}

	/**
	 * Receive the notification of the worker layoff.
	 */
	public void notifyFiring() {
		this.contract = null;
		this.periodsUnemployed = 0;
		status = EmploymentStatus.INVOLUNTARILY_UNEMPLOYED;
	}

	public long getWage() {
		if (contract == null) {
			return 0;
		}
		return contract.getWage();
	}

	public void prepareOffer() {
		updateReservationWage();
	}

	public Labor getOffer() {
		// TODO: check this deletion, apparently it's right:// use();
		return new Labor(contract.getWage());
	}

	public EmploymentStatus getEmploymentStatus() {
		return status;
	}

	public Labor supply(BankAccount employer, long amount) {// XXX why is
		// 'amount'
		// even necessary?
		// (other than to comply
		// with the interface)
		if (amount == contract.getWage()) {// XXX
			employer.transfer(account, contract.getWage());
			use();
			return new Labor(contract.getWage());
		}
		throw new IllegalArgumentException(
				"Wage different from the one in contract");
	}
}
