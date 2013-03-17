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

import jamel.spheres.monetary.exceptions.InvalidChequeException;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.ReadablePeriod;

import scheduling.cycle.Cycle;
import scheduling.cycle.ExpiringElement;

/**
 * A base class for cheques.
 * <p>
 * Last update: 19-Jun-2011
 */
public class Cheque extends ExpiringElement {

	private static final List<Cheque> instances = new LinkedList<Cheque>();
	/*
	 * TODO : Magic number gracefully provided by JP
	 */
	public static final ReadablePeriod DEFAULT_TERM = Days.ONE;

	/** The amount. */
	private final long amount;
	private final BankAccount payer;
	private final BankAccount payee;

	/**
	 * Creates a new cheque for the given amount.
	 * 
	 * @param amount
	 *            the amount to transfer.
	 * @param payee
	 *            the payee.
	 */
	Cheque(Cycle cycle, BankAccount payer, BankAccount payee, long amount,
			ReadablePeriod duration) {
		super(cycle, duration);
		this.amount = amount;
		this.payer = payer;
		this.payee = payee;
		instances.add(this);
		// System.out.println(super.toString());
		// System.out.println(payer.toString());
		// System.out.println("Deposit:" + payer.getAvailableAmount());
		// System.out.println("UnusableDeposit:" +
		// payer.getUnavailableAmount());
		// System.out.println("---");
	}

	Cheque(Cycle cycle, BankAccount payer, BankAccount payee, long amount) {
		this(cycle, payer, payee, amount, DEFAULT_TERM);
	}

	/**
	 * Transfer the cheque to the payee account.
	 * 
	 * @param payeeAccount
	 *            the payee account.
	 */
	@Override
	public void expire() {// TODO: cheques never expired?
		if (!isValid()) {
			throw new InvalidChequeException();
		}
		payer.subtract(amount);
		payee.credit(amount);
	}

	/**
	 * Returns a flag that indicates whether or nor the cheque is valid.
	 * 
	 * @return a boolean.
	 */
	public boolean isValid() {
		return isExpirationTime();
	}

	public long getAmount() {
		return amount;
	}

	public static void creditCheques() {
		List<Cheque> credited = new LinkedList<Cheque>();
		for (Cheque c : instances) {
			if (c.isExpirationTime()) {
				c.expire();
				credited.add(c);
			}
		}
		for (Cheque c : credited) {
			instances.remove(c);
		}
	}
}
