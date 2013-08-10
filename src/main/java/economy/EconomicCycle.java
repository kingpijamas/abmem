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

package economy;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import scheduling.cycle.Cycle;
import utils.Recordable;
import utils.TransientNumber;
import economy.events.CreditCheques;
import economy.events.PayBankDividend;
import economy.events.PayFirmDividends;
import economy.events.RunDebtRecovery;
import economy.events.RunMarket;
import economy.events.RunProduction;
import economy.events.UpdateBank;
import economy.events.UpdateHouseholds;
import economy.events.UpdateProductiveSector;

/**
 * Represents the macro-economic circuit.
 * <p>
 * Last update: 19-Jun-2011
 */
public class EconomicCycle extends Cycle {
	private final Economy economy;

	public EconomicCycle(Economy economy, DateTime start, DateTime end,
			Period step) {
		super(start, end, step);
		this.economy = economy;
	}

	public Economy getEconomy() {
		return economy;
	}

	public void init(boolean testing) {
		addEvent(new Recordable.PollRecordables(), getStart(), Days.ONE);
		addEvent(new TransientNumber.RefreshTransients(), getStart(), Days.ONE);
		addEvent(new PayBankDividend(economy), getStart(), Days.ONE);
		addEvent(new PayFirmDividends(economy), getStart(), Days.ONE);
		addEvent(new RunMarket(economy.getLaborMarket()), getStart(), Days.ONE);
		addEvent(new RunProduction(economy), getStart(), Days.ONE);// TODO: not
																	// clear
		// if this goes here
		addEvent(new RunMarket(economy.getGoodsMarket()), getStart(), Days.ONE);
		addEvent(new UpdateHouseholds(economy), getStart(), Days.ONE);
		addEvent(new RunDebtRecovery(economy), getStart(), Days.ONE);
		addEvent(new UpdateProductiveSector(economy), getStart(), Days.ONE);
		addEvent(new UpdateBank(economy), getStart(), Days.ONE);
		addEvent(new CreditCheques(economy), getStart(), Days.ONE);// TODO:
																	// dunno
		// where this
		// goes exactly
	}
}
