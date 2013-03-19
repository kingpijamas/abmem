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

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import scheduling.cycle.Cycle;
import utils.Recordable;
import utils.TransientNumber;
import economicCycle.events.CreditCheques;
import economicCycle.events.PayBankDividend;
import economicCycle.events.PayFirmDividends;
import economicCycle.events.RunDebtRecovery;
import economicCycle.events.RunMarket;
import economicCycle.events.RunProduction;
import economicCycle.events.UpdateBank;
import economicCycle.events.UpdateHouseholds;
import economicCycle.events.UpdateProductiveSector;

/**
 * Represents the macro-economic circuit.
 * <p>
 * Last update: 19-Jun-2011
 */
public class EconomicCycle extends Cycle {

	public EconomicCycle(DateTime start, DateTime end, Period step) {
		super(start, end, step);
	}

	public void init(boolean testing) {
		addEvent(new Recordable.PollRecordables(), getStart(), Days.ONE);
		addEvent(new TransientNumber.RefreshTransients(), getStart(), Days.ONE);
		addEvent(new PayBankDividend(), getStart(), Days.ONE);
		addEvent(new PayFirmDividends(), getStart(), Days.ONE);
		addEvent(new RunMarket(World.getInstance().getLaborMarket()),
				getStart(), Days.ONE);
		addEvent(new RunProduction(), getStart(), Days.ONE);// TODO: not clear
															// if this goes here
		addEvent(new RunMarket(World.getInstance().getGoodsMarket()),
				getStart(), Days.ONE);
		addEvent(new UpdateHouseholds(), getStart(), Days.ONE);
		addEvent(new RunDebtRecovery(), getStart(), Days.ONE);
		addEvent(new UpdateProductiveSector(), getStart(), Days.ONE);
		addEvent(new UpdateBank(), getStart(), Days.ONE);
		addEvent(new CreditCheques(), getStart(), Days.ONE);// TODO: dunno
															// where this
															// goes exactly
	}
}
