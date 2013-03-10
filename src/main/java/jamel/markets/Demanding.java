package jamel.markets;

public interface Demanding<T extends Offerable> {
	public void getSupplied(Offering<T> offerer);
}
