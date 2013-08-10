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

package jamel.markets.goods;

import jamel.agents.households.Household;
import jamel.markets.Market;
import jamel.markets.Offering;

import java.util.Iterator;

import economy.EconomicCycle;

/**
 * Represents a goods market.
 * <p>
 * Last update: 19-Jun-2011.
 */
public class GoodsMarket extends Market<Goods, Household> {

	public GoodsMarket(EconomicCycle circuit) {
		super(circuit);
	}

	private boolean isSatisfyingOffer(long demandingBudget, Goods offer) {
		return offer.getValue() >= demandingBudget;
	}

	protected void supply(Household h) {
		long budget = h.getConsumptionBudget();
		Offering<Goods> supplier = null;
		if (offererExists(h.getPreferredProvider())
				&& isSatisfyingOffer(budget, h.getPreferredProvider().getOffer())) {
			supplier = h.getPreferredProvider();
		} else {
			supplier = getBestOfferer(budget, h.getMaxMarketCrawlingIntents());
		}
		if (supplier == null) {
			return;// TODO: check
		}
		updateValue(supplier.getOffer().getValue());
		updateVolume(supplier.getOffer().getVolume());
		// if(suppliedGoods==null) TODO
		h.getSupplied(supplier);
		// System.out.println(c);
	}

	public int compare(long demandingBudget, Goods o1, Goods o2) {
		return (int) (Math.abs(o1.getValue() - demandingBudget) - Math.abs(o2
				.getValue() - demandingBudget));
	}

	private Offering<Goods> getBestOfferer(long demandingBudget, int lookingTime) {
		Offering<Goods> best = null;
		Iterator<Offering<Goods>> it = getOfferers().iterator();
		Offering<Goods> current = null;
		while (it.hasNext() && lookingTime > 0) {
			current = it.next();
			if (best == null
					|| compare(demandingBudget, current.getOffer(),
							best.getOffer()) > 0) {
				best = current;
			}
			lookingTime--;
		}
		if (best != null && isSatisfyingOffer(demandingBudget, best.getOffer())) {
			return best;
		}
		return null;
	}
}