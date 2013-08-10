package jamel.spheres.monetary;

import static org.junit.Assert.fail;
import jamel.spheres.monetary.Bank;
import jamel.spheres.monetary.Borrower;
import main.EconomyMock;

import org.junit.Before;
import org.junit.Test;

import economy.Economy;

public class BankTest {
	private Bank sut;

	@Before
	public void setUp() throws Exception {
		Economy world = new EconomyMock();
		sut = world.getBank();
	}

	@Test
	public void testBank() {
		fail("Not yet implemented");
	}

	@Test
	public void testLend() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDoubtfulDebtTotal() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testRecoverDebts() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsAccommodating() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMonthlyInterestRate() {
		fail("Not yet implemented");
	}

	@Test
	public void testPayDividend() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAssets() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLiabilities() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCapitalAdequacy() {
		if (sut.getCapital() != sut.getAssets() - sut.getLiabilities()) {
			fail();
		}
	}

	@Test
	public void testCalculateDividend() {
		long assets = 1000;
		setAssets(assets);
		long targetCapital = (long) (sut.getCapitalRatio() * sut.getAssets());
		long result = sut.calculateDividend();
		long expected = 0;
		if ((sut.getCapital() > targetCapital)) {
			expected = (long) (sut.getDividendRedistributionRatio() * (sut
					.getCapital() - targetCapital));
		}
		if (result != expected) {
			fail("Expecting " + expected + ". Got " + result + ".");
		}
	}

	@Test
	public void testOpenAccount() {
		fail("Not yet implemented");
	}

	@Test
	public void testOpenBorrowerAccount() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetOwner() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	private void setAssets(long value) {
		sut.lend(sut.openAccount(new Borrower() {
			public void declareBankruptcy() {
			}
		}), value);
	}

}
