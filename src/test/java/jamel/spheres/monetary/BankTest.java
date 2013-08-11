package jamel.spheres.monetary;

import static org.junit.Assert.fail;
import mocks.EconomyMock;

import org.junit.Before;
import org.junit.Test;

import economy.Economy;

public class BankTest {
	private static final long STARTING_ASSETS = 1000;
	private Bank sut;

	@Before
	public void setUp() throws Exception {
		Economy world = new EconomyMock();
		sut = world.getBank();
		setAssets(STARTING_ASSETS);
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
	public void testPayDividend() {// TODO: check if this is enough
		long prevCapital = sut.getCapital();
		long prevAssets = sut.getAssets();
		long prevLiabilities = sut.getLiabilities();
		long dividend = sut.calculateDividend();

		sut.payDividend();

		long expectedLiabilities = prevLiabilities + dividend;
		long expectedAssets = prevAssets;
		long expectedCapital = prevCapital - dividend;

		boolean liabilitiesFailed = sut.getLiabilities() != expectedLiabilities;
		boolean assetsFailed = sut.getAssets() != expectedAssets;
		boolean capitalFailed = sut.getCapital() != expectedCapital;
		if (liabilitiesFailed || assetsFailed || capitalFailed) {
			fail();
		}
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
