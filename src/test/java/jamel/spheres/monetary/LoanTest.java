package jamel.spheres.monetary;

import static org.junit.Assert.fail;
import mocks.BorrowerMock;
import mocks.EconomyMock;

import org.junit.Before;
import org.junit.Test;

import economy.Economy;

public class LoanTest {
	private static final long STARTING_PRINCIPAL = 1000;
	private static final long STARTING_DEPOSIT_GOOD_ACCOUNT = 500;
	private static final double INTEREST_RATE = 0.05;

	private Economy economy;

	private Loan doubtfulLoan;
	private Loan goodLoan;
	private BorrowerBankAccount doubtfulBorrower;
	private BorrowerBankAccount goodBorrower;

	@Before
	public void setUp() throws Exception {
		economy = new EconomyMock();
		doubtfulBorrower = economy.getBank().openAccount(new BorrowerMock());
		goodBorrower = economy.getBank().openAccount(new BorrowerMock());
		goodBorrower.credit(STARTING_DEPOSIT_GOOD_ACCOUNT);

		doubtfulLoan = new Loan(economy.getCycle(), doubtfulBorrower,
				STARTING_PRINCIPAL, INTEREST_RATE);
		goodLoan = new Loan(economy.getCycle(), goodBorrower,
				STARTING_PRINCIPAL, INTEREST_RATE);
	}

	@Test
	public void testExpire() {
		fail("Not yet implemented");
	}

	@Test
	public void testPayBack() {
		fail("Not yet implemented");
	}

	@Test
	public void testPayInterest() {
		testPayInterest(goodBorrower, goodLoan);
		testPayInterest(doubtfulBorrower, doubtfulLoan);
	}

	private void testPayInterest(BorrowerBankAccount borrower, Loan loan) {
		long prevDeposit = borrower.getDeposit();
		long interest = loan.calculateInterest();
		LoanQualities prevQuality = loan.getQuality();
		loan.payInterest();

		long currentDeposit = borrower.getDeposit();
		if (currentDeposit == prevDeposit - interest) {
			if (!loan.getQuality().equals(prevQuality)) {
				fail();
			}
		} else if (prevQuality.compareTo(loan.getQuality()) != 0) {
			fail();
		}
	}

	@Test
	public void testCompareTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPrincipal() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetQuality() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsRepaid() {
		fail("Not yet implemented");
	}

}
