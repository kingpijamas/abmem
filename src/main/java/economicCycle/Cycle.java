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

package economicCycle;

import jamel.World;
import jamel.utils.Recordable;
import jamel.utils.TransientNumber;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;

import economicCycle.scheduling.Schedule;
import economicCycle.scheduling.SimulationEvent;
import economicCycle.scheduling.events.CreditCheques;
import economicCycle.scheduling.events.PayBankDividend;
import economicCycle.scheduling.events.PayFirmDividends;
import economicCycle.scheduling.events.RunDebtRecovery;
import economicCycle.scheduling.events.RunMarket;
import economicCycle.scheduling.events.RunProduction;
import economicCycle.scheduling.events.UpdateBank;
import economicCycle.scheduling.events.UpdateHouseholds;
import economicCycle.scheduling.events.UpdateProductiveSector;

/**
 * Represents the macro-economic circuit.
 * <p>
 * Last update: 19-Jun-2011
 */
public class Cycle {

	private DateTime start;
	private DateTime end;
	private Schedule events;
	private int currentStep;
	private Period step;

	/**
	 * Only for tests!
	 * 
	 * @param start
	 * @param end
	 * @param step
	 * @param testing
	 */
	public Cycle(DateTime start, DateTime end, Period step) {
		this.events = new Schedule();
		this.start = start;
		this.step = step;
		this.end = end;
	}

	public void init(boolean testing) {
		if (!testing) {
			events.add(new Recordable.PollRecordables(), start, Days.ONE);
			events.add(new TransientNumber.RefreshTransients(), start, Days.ONE);
			events.add(new PayBankDividend(), start.plus(step), Days.ONE);
			events.add(new PayFirmDividends(), start.plus(step), Days.ONE);// this
																			// is
																			// done
																			// to
																			// let
																			// firms
																			// not
																			// pay
																			// their
																			// first
																			// dividend,
																			// since
																			// dividends
																			// are
																			// payed
																			// before
																			// actual
																			// money
																			// is
																			// obtained
																			// in
																			// this
																			// model
																			// (and
																			// thus,
																			// it
																			// crashes)
			events.add(new RunMarket(World.getInstance().getLaborMarket()),
					start, Days.ONE);
			events.add(new RunProduction(), start, Days.ONE);
			events.add(new RunMarket(World.getInstance().getGoodsMarket()),
					start, Days.ONE);
			events.add(new UpdateHouseholds(), start, Days.ONE);
			events.add(new RunDebtRecovery(), start, Days.ONE);
			events.add(new UpdateProductiveSector(), start, Days.ONE);
			events.add(new UpdateBank(), start, Days.ONE);
			events.add(new CreditCheques(), start, Days.ONE);// TODO: dunno
																// where this
																// goes exactly
		}
	}

	/**
	 * Executes a period a the circuit.<br>
	 * A period is defined as the time between two consecutive payments of
	 * income.
	 */
	public void doPeriod() {
		if (!ended()) {
			if (events.doNextEvent()) {
				System.out.println("\n\n---------------\n");
				currentStep++;
				System.out.println();
				System.out.println("\t\t<PERIOD: "
						+ start.plus(step.multipliedBy(currentStep)) + "("
						+ (getPeriodsFromStart()) + ")>\n\n");
			}
		}
	}

	public boolean ended() {
		return getDate().equals(end);
	}

	public int getPeriodsFromStart() {
		return currentStep;
	}

	public void addEvent(SimulationEvent event, DateTime date, int priority,
			ReadablePeriod repeatFrequency, int repetitions) {
		events.add(event, date, priority, repeatFrequency, repetitions);
	}

	public void addEvent(SimulationEvent se, DateTime start,
			int periodRepeatFrequency) {
		events.add(se, start, step.multipliedBy(periodRepeatFrequency));
	}

	public void addEvent(SimulationEvent event, DateTime date, int priority,
			int repeatEverySteps, int repetitions) {
		addEvent(event, date, priority, step.multipliedBy(repeatEverySteps),
				repetitions);
	}

	public void addEvent(SimulationEvent event, DateTime date) {
		events.add(event, date);
	}

	public DateTime getDate() {
		/*
		 * System.out.println("Date is:" +
		 * start.plus(step.multipliedBy(currentStep)) + "\n");
		 */
		return start.plus(step.multipliedBy(currentStep));
	}

	public Period getStep() {
		return step;
	}

	public int getLastPriority() {
		return events.getLastPriority();
	}

	public String toString() {
		return "Start: " + start + " Step: " + step + " current: "
				+ currentStep + " end: " + end;
	}

}
