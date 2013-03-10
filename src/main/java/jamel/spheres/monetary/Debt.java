package jamel.spheres.monetary;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Debt {
	/** The collection of loans. */
	private Set<Loan> loans;

	public Debt() {
		this.loans = new HashSet<Loan>();
	}

	/**
	 * Pays interest due for each loan.
	 */
	void payInterest() {
		for (Loan loan : loans) {
			loan.payInterest();
		}
	}

	/**
	 * Recovers loans.
	 */
	void recover() {
		List<Loan> toRemove = new LinkedList<Loan>();
		for (Loan loan : loans) {
			loan.payBack();
			if (loan.isRepaid()) {
				toRemove.add(loan);
			}
		}
		loans.removeAll(toRemove);
	}

	/**
	 * Returns the total outstanding debt for this account.
	 * 
	 * @return a long that represents the debt.
	 */
	public long getTotalDebt() {
		long sum = 0;
		for (Loan loan : loans) {
			sum += loan.getPrincipal();
		}
		return sum;
	}

	void add(Loan loan) {
		loans.add(loan);
	}

	/**
	 * Returns the quality of the debt for this account.
	 * 
	 * @return the quality.
	 */
	public LoanQualities getLoanQuality() {
		LoanQualities quality = LoanQualities.GOOD;
		for (Loan loan : loans) {
			if (loan.getQuality() == LoanQualities.BAD) {
				return LoanQualities.BAD;
			} else if (loan.getQuality() == LoanQualities.DOUBTFUL) {
				quality = LoanQualities.DOUBTFUL;
			}
		}
		return quality;
	}

}
