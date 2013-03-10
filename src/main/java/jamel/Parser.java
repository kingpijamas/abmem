package jamel;

public class Parser {

	public String getParameter(String name) {
		return null;
		// TODO:
	}


	private SimulationParams get(String s) {
		double min = Double
				.parseDouble(getParameter("simulation." + s + ".min"));
		return new SimulationParams(min, min,
				Double.parseDouble(getParameter("simulation." + s + ".max")),
				Double.parseDouble(getParameter("simulation." + s + ".step")));
	}
}
