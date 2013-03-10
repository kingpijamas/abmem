package jamel.markets;

import jamel.spheres.monetary.BankAccount;

public interface Offering<T extends Offerable> {
	// #1
	public void prepareOffer();

	// #2
	public T getOffer();

	// #3
	public T supply(BankAccount purchaser, long amount);

}
