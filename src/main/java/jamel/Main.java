package jamel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.data.xy.XYSeries;

/**
 * La classe principale de l'applet.<br>
 * Permet la cr�ation de nouvelles simulations.
 * 
 * @author Pascal Seppecher 2008-2009
 * @version 0.1.5.9 On transf�re la r�cup�ration des param�tres �
 *          l'objet <code>Simulation</code>.
 */

@SuppressWarnings("serial")
public class Main {



	private Simulator simulator;
	
	private boolean autoRun;


	private Parser parser;

	private static Main instance;


	private Double getParameter(String s) {
		String param = parser.getParameter(s);
		if (param == null) {
			return null;
		}
		return Double.parseDouble(param);
	}

	private String replace(String string, String label, double number) {
		if (number == (int) number)
			string = string.replace(label, Integer.toString((int) number));
		else
			string = string.replace(label, Double.toString(number));
		return string;
	}

	boolean isAutoRun() {
		return autoRun;
	}

	private void saveHtmlRapport() {
		try {
			final FileWriter writer = new FileWriter(new File(outputDirectory,
					"rapport.html"));
			writer.write("<HTML>");
			writer.write("<HEAD>");
			writer.write("<TITLE>" + title + "</TITLE>");
			writer.write("</HEAD>");
			writer.write("<BODY>");
			writer.write("<H1>" + title + "</H1>");
			writer.write("<P>Simulations: " + simulator.getCount() + "</P>");
			writer.write("<P>From simulator.getAlpha() = " + simulator.getAlpha().min + " to " + simulator.getAlpha().max
					+ ", step = " + simulator.getAlpha().step + "</P>");
			writer.write("<P>From simulator.getBeta() = " + simulator.getBeta().min + " to " + simulator.getBeta().max
					+ ", step = " + simulator.getBeta().step + "</P>");
			writer.write("<TABLE>");
			writer.write("<TR>");
			writer.write(getHtmlCell(DataLabel.medianPrice));
			writer.write(getHtmlCell(DataLabel.medianWage));
			writer.write(getHtmlCell(DataLabel.unemployment));
			writer.write("<TR>");
			writer.write(getHtmlCell(DataLabel.moneyVelocity));
			writer.write(getHtmlCell(DataLabel.profitShare));
			writer.write(getHtmlCell(DataLabel.productionVolume));
			writer.write("<TR>");
			writer.write(getHtmlCell(DataLabel.doubtfulDebtRatio));
			writer.write(getHtmlCell(DataLabel.bankruptcies));
			writer.write(getHtmlCell(DataLabel.duration));
			writer.write("</TABLE>");
			writer.write("</BODY>");
			writer.write("</HTML>");
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private String getHtmlCell(String key) {
		return "<TD><IMG src=\"" + key.replace(" ", "_") + ".png\" title=\""
				+ key + "\">";
	}

	private void saveCharts() {
		for (Map.Entry<String, ChartPanel> entry : chartPanelList.entrySet())
			try {
				ChartUtilities.saveChartAsPNG(new File(outputDirectory, entry
						.getKey().replace(" ", "_") + ".png"), entry.getValue()
						.getChart(), entry.getValue().getWidth(), entry
						.getValue().getHeight());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#init()
	 */
	public void init() {
		Simulator sim = parser.init();
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGUI();
				}
			});
		} catch (Exception e) {
			System.err.println("createGUI didn't successfully complete");
		}
	}

/*	public JamelPeriod getCurrentPeriod() {
		return simulator.getCurrentPeriod();
	}
*/
	public JamelPeriod getPeriod(int period) {
		return simulator.getPeriod(period);
	}

	public JamelPeriod getPeriod(String parameter) {
		return simulator.getPeriod(parameter);
	}

	public void changePeriod() {
		simulator.changePeriod();
	}

	
	public String getRequiredParameter(String key) {
		String value = mockApplet.getParameter(key);
		if (value == null)
			throw new NullPointerException("Parameter \"" + key
					+ "\" not found.");
		println("\t" + key + "(" + value + ")");
		return value;
	}

	public String getOptionalParameter(String key) {
		String value = mockApplet.getParameter(key);
		if (value != null)
			println("\t" + key + "(" + value + ")");
		return value;
	}

	public void println(String message) {
		if (simulation != null) {
			if (messageBuffer != null) {
				simulation.println(messageBuffer.toString());
				messageBuffer = null;
			}
			simulation.println(message);
		} else {
			if (messageBuffer == null)
				messageBuffer = new StringBuffer(message + "<br>\n");
			else
				messageBuffer.append(message + "<br>\n");
		}
	}

	/**
	 * Adds a marker in time chart.
	 * 
	 * @param label
	 *            the label.
	 */
	public void newMarker(String label) {
		simulation.addMarker(label, getCurrentPeriod().getYearValue(),
				getCurrentPeriod().getMonthValue());
	}

	public XYSeries getMultiSimulationSeries()(String key) {
		try {
			return ((XYSeries) simulator.getMultiSimulationSeries()).get(key).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void createNewSimulation() {
		System.out.println("Simulation:" + simulator.getCount());
		System.out.println("simulator.getAlpha():" + simulator.getAlpha().param);
		System.out.println("simulator.getBeta():" + simulator.getBeta().param);
		System.out.println("Gama:" + simulator.getGamma().param);
		simulator.createNewSimulation();
		if (messageBuffer != null) {
			println(messageBuffer.toString());
			messageBuffer = null;
		}
	}
	
	public void nextSimulation(){
		jProgressBar.setValue(simulator.getCount());
		int estimatedRemainingTime=simulator.getEstimatedRemainingTime();
		String estimatedRemainingTimeMessage;
		if (estimatedRemainingTime > 3600) {
			estimatedRemainingTimeMessage = ""
					+ (estimatedRemainingTime / 3600)
					+ " h "
					+ (estimatedRemainingTime - (estimatedRemainingTime / 3600) * 3600)
					/ 60 + " mn";
		} else if (estimatedRemainingTime > 120) {
			estimatedRemainingTimeMessage = "" + (estimatedRemainingTime / 60)
					+ " mn";
		} else {
			estimatedRemainingTimeMessage = "" + estimatedRemainingTime + " s";
		}
		jProgressBar.setString("" + simulator.getTaskPercentage() + " % (Simulation " + simulator.getCount()
				+ " on " + simulator.getMaxSimulations() + " - Estimated remaining time: "
				+ estimatedRemainingTimeMessage + ")");
	}
	public void initMultiSimulation() {
		window = getNewMultiSimulationWindow();
	}
	
	public void multiSimEnded() {
		jProgressBar.setValue(jProgressBar.getMinimum());
		jProgressBar.setString("Completed");
		System.out.println("Multi-simulation successfuly ended.");
		saveCharts();
		saveHtmlRapport();
		return;
	}

	public void makeDir() {
		outputDirectory = new File(title + "-" + (new Date()).getTime());
		outputDirectory.mkdir();
	}

	public boolean isMultiSimulation() {
		//TODO
		return false;
	}

	public static Main getInstance() {
		if(instance==null){
			instance=new Main();
		}
		return instance;
	}

	public void newSimulation() {
		// TODO Auto-generated method stub
		
	}

}
