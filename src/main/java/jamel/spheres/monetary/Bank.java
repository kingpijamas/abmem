/* =========================================================
 * JAMEL : a Java (tm) Agent-based MacroEconomic Main.
 * =========================================================
 *
 * (C) Copyright 2007-2011, Pascal Seppecher.
 * 
 * Project Info <http://p.seppecher.free.fr/jamel/>. 
 *
 * This file is a part of JAMEL (Java Agent-based MacroEconomic Main).
 * 
 * JAMEL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JAMEL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JAMEL. If not, see <http://www.gnu.org/licenses/>.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */

package jamel.spheres.monetary;

import jamel.spheres.monetary.exceptions.BankFailureException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import scheduling.cycle.CycleElement;
import economy.Economy;

/**
 * Represents the single representative bank.
 * <p>
 * Encapsulates a list of accounts.
 * <p>
 * Last update: 19-Jun-2011.
 */
public class Bank extends CycleElement {// VALIDATED: contains the entire Bank
										// (and inner classes) from Jamel
	private BankAccount owner;
	private final Set<BankAccount> accounts = new HashSet<BankAccount>();
	private final Set<BorrowerBankAccount> borrowerAccounts = new HashSet<BorrowerBankAccount>();

	private boolean accommodating;
	private double monthlyInterestRate; // FIXME: it should be done on a per
										// period basis

	private double capitalRatio;
	private double dividendRedistributionRatio;

	private long debts = 0;
	private long deposits = 0;

	/**
	 * Creates a new bank.
	 */
	public Bank(Economy economy, boolean accomodating,
			double annualInterestRate, double capitalRatio,
			double dividendRedistributionRatio) {
		super(economy.getCycle());
		setMonthlyInterestRate(annualInterestRate);
		this.capitalRatio = capitalRatio;
		this.dividendRedistributionRatio = dividendRedistributionRatio;
	}

	void lend(BorrowerBankAccount account, long principal) {// IMPORTANT: YES,
															// the bank has
															// infinite money to
															// lend
		account.add(new Loan(getCycle(), account, principal,
				getMonthlyInterestRate()));
	}

	/**
	 * Computes and returns the total amount of the doubtful debts.
	 * 
	 * @return the sum of doubtful debts
	 */
	public long getDoubtfulDebtTotal() {// VALIDATED
		long total = 0;
		for (BorrowerBankAccount account : borrowerAccounts) {
			if (account.getDebtorStatus() != LoanQualities.GOOD) {
				total += account.getTotalDebt();
			}
		}
		return total;
	}

	/**
	 * Close the bank.<br>
	 * Updates the balance sheet, determines the dividend that will be paid and
	 * updates statistics. Must be called at the end of each period.
	 */
	public void update() {// VALIDATED
		for (BankAccount account : accounts) {
			deposits += account.getDeposit();
		}
		for (BorrowerBankAccount account : borrowerAccounts) {
			deposits += account.getDeposit();
			debts += account.getTotalDebt();
		}
	}

	/**
	 * Recovers the debt.<br>
	 * Traverses the list of accounts, looking for due debts and trying to
	 * recover them. A very important method that simulates the monetary reflux.
	 */
	public void recoverDebts() {// VALIDATED
		for (BorrowerBankAccount account : borrowerAccounts) {
			account.payInterest();
		}
		List<BankAccount> failures = new LinkedList<BankAccount>();// FIXME:
																	// failures
																	// is
																	// unused.
																	// Maybe the
																	// idea was
																	// to remove
																	// them from
																	// the
																	// accountsList
																	// afterwards?
		for (BorrowerBankAccount account : borrowerAccounts) {
			account.recover();
			if (account.getDebtorStatus() == LoanQualities.BAD
					&& !isAccommodating() && !account.isProtected()) {
				account.cancelDebt();
				failures.add(account);
				account.getHolder().declareBankruptcy();
			}
		}
		// Checks the level of capital adequacy. If negative, ends the
		// simulation.
		if (getCapital() < 0) {
			throw new BankFailureException("Bank crashed");
		}
	}

	/**
	 * Returns a flag that indicates if the bank is accommodating or not.
	 * 
	 * @return a boolean.
	 */
	boolean isAccommodating() {// VALIDATED
		return accommodating;
	}

	/**
	 * Returns the usual monthly rate of interest.
	 * 
	 * @return a double that represents the rate.
	 */
	double getMonthlyInterestRate() {// VALIDATED
		return monthlyInterestRate;
	}

	/**
	 * Sets the usual interest rate as an annual rate.
	 * 
	 * @param annualRate
	 *            the rate to set.
	 */
	private void setMonthlyInterestRate(double annualRate) {// VALIDATED
		this.monthlyInterestRate = Math.pow(1 + annualRate, 1.0 / 12) - 1;
	}

	/**
	 * Returns the ratio of the Bank's dividend that will be redistributed (and
	 * thus not kept by the Bank)
	 */
	double getDividendRedistributionRatio() {
		return dividendRedistributionRatio;
	}

	/**
	 * Calculates the dividend for a period. This depends on whether current
	 * capital is higher or lower than the capital target (which is proportional
	 * to the capitalRatio and the assets).
	 * 
	 * If it is higher, the dividend is the surplus capital times the
	 * dividendRedistributionRatio. If not, the dividend is 0.
	 */
	long calculateDividend() {// VALIDATED
		long targetCapital = (long) (capitalRatio * getAssets());
		if (getCapital() > targetCapital) {
			return (long) (dividendRedistributionRatio * (getCapital() - targetCapital));
		}
		return 0;
	}

	public void payDividend() {// VALIDATED
		long dividend = calculateDividend();
		if (dividend > 0) {
			owner.credit(dividend);
		}
	}

	public BankAccount openAccount() {// VALIDATED
		BankAccount acc = new BankAccount(getCycle(), this);
		accounts.add(acc);
		return acc;
	}

	public BorrowerBankAccount openAccount(Borrower holder) {// VALIDATED
		BorrowerBankAccount acc = new BorrowerBankAccount(getCycle(), this,
				holder);
		borrowerAccounts.add(acc);
		return acc;
	}

	/*
	 * TODO: What if the owner also owns, say, several companies, and he ends up
	 * failing?
	 */
	public void setOwner(BankAccount owner) {// VALIDATED
		this.owner = owner;
	}

	long getAssets() {
		return debts;
	}

	long getLiabilities() {
		return deposits;
	}

	/**
	 * Returns the bank capital adequacy.
	 * 
	 * @return a long that represents the bank capital.
	 */
	long getCapital() {// VALIDATED
		return getAssets() - getLiabilities();
	}

	/**
	 * Returns the minimum ratio of capital the Bank expects to keep for itself
	 * on a given period
	 */
	double getCapitalRatio() {
		return capitalRatio;
	}

	@Override
	public String toString() {// TODO: it'd be interesting to cram all the
								// original GUI
								// data here
		String ans = "Accounts(#): " + accounts.size();
		ans += "\nBorrowers(#): " + borrowerAccounts.size();
		ans += "\nDeposits: " + deposits;
		ans += "\nDebts: " + debts + "(doubtful:" + getDoubtfulDebtTotal()
				+ ")";
		ans += "\nCapital adequacy: " + getCapital();
		return ans;
	}
}