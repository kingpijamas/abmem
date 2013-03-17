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

import scheduling.cycle.Cycle;
import scheduling.cycle.CycleElement;

/**
 * Represents an account.
 * <p>
 * Encapsulates a monetary deposit and a list of loans.
 * <p>
 * Last update: 19-Jun-2011.
 */
public class BankAccount extends CycleElement {

	/** The deposit. */
	private long deposit;
	private long unusableDeposit;
	private Bank bank;

	/**
	 * Creates a new account.
	 * 
	 * @param creditManager
	 *            the bank credit manager.
	 * @param holder
	 *            the account holder.
	 * @param protection
	 */
	BankAccount(Cycle cycle, Bank bank) {
		super(cycle);
		this.bank = bank;
		this.deposit = 0;
		this.unusableDeposit = 0;
	}

	Bank getBank() {
		return bank;
	}

	/**
	 * Returns the available amount.
	 * 
	 * @return a long integer.
	 */
	public long getAvailableAmount() {
		return deposit;
	}

	/**
	 * Returns a new cheque from this account for the given amount.
	 * 
	 * @param amount
	 *            the amount to transfer.
	 * @param payee
	 *            the payee.
	 * @return a cheque.
	 */
	public void transfer(BankAccount payee, long amount) {
		if (amount < 0) {
			throw new IllegalArgumentException();
		}
		if (deposit < amount) {
			throw new IllegalArgumentException("Not enough funds");
		}
		deposit -= amount;
		unusableDeposit += amount;
		new Cheque(getCycle(), this, payee, amount);
	}

	void credit(long amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Crediting a negative amount");
		}
		deposit += amount;
	}

	void subtract(long amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Subtracting a negative amount");
		}
		if (unusableDeposit < amount) {
			throw new IllegalArgumentException();
		}
		unusableDeposit -= amount;
	}

	protected void setDeposit(long value) {
		this.deposit = value;
	}

	public long getCapital() {
		return getAvailableAmount();
	}

	public long getUnavailableAmount() {
		return unusableDeposit;
	}

	@Override
	public String toString() {
		String ans = super.toString();
		// ans += "\nHolder: " + holder;
		ans += "\nDeposit: " + deposit;
		ans += "\nUnusable: " + unusableDeposit;
		return ans;
	}
}
