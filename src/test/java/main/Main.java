package main;
import economy.Economy;

public class Main {
	public static void main(String[] args) throws CloneNotSupportedException {
		Economy world = new EconomyMock();
		while (!world.getCycle().ended()) {
			world.getCycle().doPeriod();
		}
	}
}
