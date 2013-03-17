/* =========================================================
 * JAMEL : a Java (tm) Agent-based MacroEconomic Main.
 * =========================================================
 *
 * (C) Copyright 2007-2010, Pascal Seppecher.
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

package jamel.markets.labor;

import jamel.markets.Offering;
import jamel.utils.JamelRandom;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;

import scheduling.cycle.Cycle;
import scheduling.cycle.ExpiringElement;

/**
 * Represents a job contract.
 * <p>
 * Encapsulates the reference to the employer and the employee, the wage and the
 * end of the contract.
 * <p>
 * Last update: 19-Jun-2011.
 */
public class EmploymentContract extends ExpiringElement implements
		Comparable<EmploymentContract> {
	private static final ReadablePeriod MINDURATION = Months.THREE;// TODO:
																	// magic
																	// number,
																	// courtesy
																	// of JP

	private Worker employee;
	private Employer employer;
	private final long wage;

	/**
	 * Creates a new employment contract.
	 * 
	 * @param abstractJobOfferer
	 *            the employer.
	 * @param employee
	 *            the employee.
	 * @param wage
	 *            the wage.
	 * @param term
	 *            the term.
	 */
	public EmploymentContract(Cycle cycle, Employer employer,
			Worker employee, long wage) {
		super(cycle, new Period(Math.abs(new JamelRandom().nextInt()))
				.plus(MINDURATION));
		this.employer = employer;
		if (!employee.isUnemployed()) {
			throw new IllegalArgumentException("Employee is already employed!");
		}
		this.employee = employee;
		this.wage = wage;
	}

	/**
	 * Returns the employee.
	 * 
	 * @return the employee.
	 */
	public Offering<Labor> getEmployee() {
		return employee;
	}

	/**
	 * Returns the employer.
	 * 
	 * @return the employer.
	 */
	public Employer getEmployer() {
		return employer;
	}

	/**
	 * Returns the end of the contract.
	 * 
	 * @return an integer that represents the period.
	 */
	public DateTime getEnd() {
		return getExpiration();
	}

	/**
	 * Returns the wage to be paid to the employee.
	 * 
	 * @return a long integer that represents the wage.
	 */
	public long getWage() {
		return wage;
	}

	public void expire() {
		employer.removeContract(this);
		employee.notifyFiring();
	}

	public int compareTo(EmploymentContract other) {
		return getExpiration().compareTo(other.getExpiration());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((employee == null) ? 0 : employee.hashCode());
		result = prime * result
				+ ((employer == null) ? 0 : employer.hashCode());
		result = prime * result + (int) (wage ^ (wage >>> 32));
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
		EmploymentContract other = (EmploymentContract) obj;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		if (employer == null) {
			if (other.employer != null)
				return false;
		} else if (!employer.equals(other.employer))
			return false;
		if (wage != other.wage)
			return false;
		return true;
	}

}