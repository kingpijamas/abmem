package jamel.spheres.monetary;

import scheduling.cycle.Cycle;

public class BorrowerBankAccount extends BankAccount {
	private static final boolean DEFAULT_PROTECTION = false;

	/** A flag that indicates whether the firm is protected or not. */
	private boolean protection;
	private Debt debt;
	private Borrower owner;

	@Override
	public String toString() {
		String ans = super.toString();
		ans += "\nDebt:" + debt;
		return ans;
	}

	BorrowerBankAccount(Cycle cycle, Bank bank, Borrower owner,
			boolean protection) {
		super(cycle, bank);
		this.protection = protection;
		this.owner = owner;
		this.debt = new Debt();
	}

	BorrowerBankAccount(Cycle cycle, Bank bank, Borrower owner) {
		this(cycle, bank, owner, DEFAULT_PROTECTION);
	}

	@Override
	public void transfer(BankAccount payee, long amount) {
		if (amount < 0) {
			throw new IllegalArgumentException();
		}
		if (getDeposit() < amount) {
			getBank().lend(this, amount - getDeposit());
		}
		super.transfer(payee, amount);
	}

	void add(Loan loan) {
		debt.add(loan);
		credit(loan.getPrincipal());
	}

	void subtractForLoans(long amount) {// XXX: Ours, will have to go soon
		if (amount < 0) {
			throw new IllegalArgumentException("Subtracting a negative amount");
		}
		if (getDeposit() < amount) {
			throw new IllegalArgumentException();
		}
		setDeposit(getDeposit() - amount);
	}

	@Override
	public long getCapital() {
		return super.getCapital() - debt.getTotalDebt();
	}

	public boolean isProtected() {
		return protection;
	}

	public LoanQualities getDebtorStatus() {
		return debt.getLoanQuality();
	}

	public long getTotalDebt() {
		return debt.getTotalDebt();
	}

	public void recover() {
		debt.recover();
	}

	public void payInterest() {
		debt.payInterest();
	}

	public void cancelDebt() {
		debt = new Debt();
	}

	public Borrower getHolder() {
		return owner;
	}
}
