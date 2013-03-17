package jamel.spheres.monetary;

import jamel.spheres.monetary.exceptions.UnexpectedInvocationException;

import org.joda.time.DateTime;
import org.joda.time.ReadablePeriod;
import org.joda.time.Years;

import scheduling.cycle.Cycle;
import scheduling.cycle.ExpiringElement;

/**
 * Represents a loan.
 */
public class Loan extends ExpiringElement implements Comparable<Loan> {

	private static final ReadablePeriod DEFAULT_TERM = Years.ONE;

	/**
	 * The period of the last call to payInterest() (to detect and avoid two
	 * interest payments for the same period).
	 */
	private DateTime lastInterestPayment;
	/** The interest rate. */
	private double interestRate;
	/** The principal. */
	private long principal;
	/** The quality. */
	private LoanQualities quality;
	private BorrowerBankAccount account;

	/**
	 * Creates a new loan.<br>
	 * Respects the principle "Credit makes deposit".
	 * 
	 * @param principal
	 *            the principal.
	 * @param quality
	 *            the quality.
	 * @param interestRate
	 *            the rate.
	 * @param term
	 *            the term.
	 */
	Loan(Cycle cycle, BorrowerBankAccount acc, long principal,
			LoanQualities quality, double interestRate, ReadablePeriod term) {
		super(cycle, term);
		if (principal <= 0) {
			throw new IllegalArgumentException(
					"The principal must be strictly positive.");
		}
		this.principal = principal;
		this.quality = quality;
		this.interestRate = interestRate;
		this.lastInterestPayment = null;
		this.account = acc;
	}

	Loan(Cycle cycle, BorrowerBankAccount acc, long principal,
			double interestRate) {
		this(cycle, acc, principal, LoanQualities.GOOD, interestRate,
				DEFAULT_TERM);
	}

	/**
	 * Downgrades the loan quality.
	 */
	private void downgrade() {
		if (account.isProtected()) {
			return; // Le compte est "prot�g�", on ne d�grade pas la
					// cr�ance.
		}
		if (quality == LoanQualities.DOUBTFUL) {
			if (!account.getBank().isAccommodating()) {
				quality = LoanQualities.BAD; // La banque est accommodante, on
												// ne
												// d�grade pas la
				// cr�ance.
			}
		} else if (quality == LoanQualities.GOOD) {
			quality = LoanQualities.DOUBTFUL;
			interestRate *= 2;
		}
		return;
	}

	/**
	 * Pays back the loan.
	 */
	void payBack() {// TODO
		long repayment = 0;
		if (quality.equals(LoanQualities.BAD)) {
			throw new IllegalArgumentException("Unexpected bad loan");
		}
		if (isExpirationTime()) {
			repayment = Math.min(account.getAvailableAmount(), principal);
			expire();
		} else {
			if (quality == LoanQualities.GOOD) {
				return;
			} else if (quality == LoanQualities.DOUBTFUL) {
				repayment = Math.min(account.getAvailableAmount(), principal);
			}
		}
		account.subtractForLoans(repayment);
		this.principal -= repayment;
	}

	@Override
	protected void expire() {
		extendExpiration();// XXX: (just style; works well)
		downgrade();
	}

	/**
	 * Pays interest due.
	 */
	void payInterest() {
		if (lastInterestPayment != null
				&& lastInterestPayment.compareTo(getDate()) >= 0) {
			throw new UnexpectedInvocationException("Interest already paid.");
		}
		long payment = (long) (principal * interestRate);
		if (account.getAvailableAmount() < payment) {
			long newDebt = payment - account.getAvailableAmount();
			principal += newDebt;
			account.credit(newDebt);
		}
		if (payment != 0) {
			if (payment < 0) {
				throw new IllegalArgumentException("Negative interest.");
			}
			// System.out.println(account);
			// System.out.println("Subtracting interest of " + payment);
			account.subtractForLoans(payment);
		}
		lastInterestPayment = getDate();
	}

	/**
	 * Implements the Comparable interface so that loans can easily be sorted.
	 * 
	 * @param other
	 *            the loan to compare against.
	 * 
	 */
	public int compareTo(Loan other) {
		if (quality == other.quality) {
			return getExpiration().compareTo(other.getExpiration());
		}
		switch (quality) {
		case BAD:
			return -1;
		case GOOD:
			return 1;
		case DOUBTFUL:
			if (other.quality.equals(LoanQualities.BAD)) {
				return 1;
			}
		}
		return -1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		long temp;
		temp = Double.doubleToLongBits(interestRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((lastInterestPayment == null) ? 0 : lastInterestPayment
						.hashCode());
		result = prime * result + (int) (principal ^ (principal >>> 32));
		result = prime * result + ((quality == null) ? 0 : quality.hashCode());
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
		Loan other = (Loan) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (Double.doubleToLongBits(interestRate) != Double
				.doubleToLongBits(other.interestRate))
			return false;
		if (lastInterestPayment == null) {
			if (other.lastInterestPayment != null)
				return false;
		} else if (!lastInterestPayment.equals(other.lastInterestPayment))
			return false;
		if (principal != other.principal)
			return false;
		if (quality != other.quality)
			return false;
		return true;
	}

	public long getPrincipal() {
		return principal;
	}

	public LoanQualities getQuality() {
		return quality;
	}

	public boolean isRepaid() {
		return getPrincipal() == 0;
	}
}
