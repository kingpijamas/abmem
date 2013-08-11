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

package jamel.spheres.monetary;

public enum LoanQualities {
	BAD(0), DOUBTFUL(1), GOOD(2);

	private int orderNumber;

	private LoanQualities(int order) {
		this.orderNumber = order;
	}

	static int compare(LoanQualities lq1, LoanQualities lq2) {
		return lq1.orderNumber - lq2.orderNumber;
	}
}
