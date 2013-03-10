package jamel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;

public class GUI {
	/**
	 * Une classe pour �couter le bouton <i>new Simulation</i>.
	 */
	public class NewSimulationButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Main.getInstance().newSimulation();
		}
	}

	final String rc = System.getProperty("line.separator");

	private boolean multiSimulation = false;
	/**
	 * la simulation courante
	 */
	private static Simulation simulation;
	private String strInfo = "";
	private String title;

	private JFrame window;
	private JProgressBar jProgressBar;

	private HashMap<String, ChartPanel> chartPanelList;
	private File outputDirectory;
	private static StringBuffer messageBuffer;

	private Container contentPane;
	private MockApplet mockApplet;

	public GUI() {
		JLabel label = new JLabel("<html><center>Jamel<br>(" + title + ")",
				JLabel.CENTER);
		label.setBorder(BorderFactory.createLoweredBevelBorder());
		JButton button = new JButton("New Simulation");
		button.setToolTipText("Launch New Simulation");
		button.addActionListener(new NewSimulationButtonListener());
		contentPane.add(label, BorderLayout.CENTER);
		contentPane.add(button, BorderLayout.SOUTH);
	}

	private JFrame getNewMultiSimulationWindow(Simulator simulator) {
		JFrame newWindow = new JFrame();
		newWindow.setTitle(title);
		newWindow.setBackground(MainWindow.backgroundColor);
		newWindow.setMinimumSize(new Dimension(400, 200));
		newWindow.setPreferredSize(new Dimension(800, 400));
		newWindow.pack();
		newWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
		JPanel jPanel1 = new JPanel(new GridLayout(3, 3, 10, 10));
		jPanel1.setBackground(new Color(238, 238, 238));
		jPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		chartPanelList = new HashMap<String, ChartPanel>();
		chartPanelList.put(
				DataLabel.medianPrice,
				new ChartPanel(getLineChart(simulator, DataLabel.medianPrice,
						"Prices (Value)")));
		chartPanelList.put(
				DataLabel.medianWage,
				new ChartPanel(getLineChart(simulator, DataLabel.medianWage,
						"Wages (Value)")));
		chartPanelList.put(
				DataLabel.unemployment,
				new ChartPanel(getLineChart(simulator, DataLabel.unemployment,
						"Unemployment (Percent)")));
		chartPanelList.put(
				DataLabel.productionVolume,
				new ChartPanel(getLineChart(simulator,
						DataLabel.productionVolume, "Production (Volume)")));
		chartPanelList.put(
				DataLabel.profitShare,
				new ChartPanel(getLineChart(simulator, DataLabel.profitShare,
						"Profit Share (Percent)")));
		chartPanelList.put(
				DataLabel.moneyVelocity,
				new ChartPanel(getLineChart(simulator, DataLabel.moneyVelocity,
						"Velocity of money")));
		chartPanelList.put(
				DataLabel.doubtfulDebtRatio,
				new ChartPanel(getLineChart(simulator,
						DataLabel.doubtfulDebtRatio, "Doubtful Debt Ratio")));
		chartPanelList.put(
				DataLabel.bankruptcies,
				new ChartPanel(getLineChart(simulator, DataLabel.bankruptcies,
						"Bankruptcies")));
		chartPanelList.put(
				DataLabel.duration,
				new ChartPanel(getLineChart(simulator, DataLabel.duration,
						"Simulation Duration (Periods)")));
		jPanel1.add(chartPanelList.get(DataLabel.medianPrice));
		jPanel1.add(chartPanelList.get(DataLabel.medianWage));
		jPanel1.add(chartPanelList.get(DataLabel.unemployment));
		jPanel1.add(chartPanelList.get(DataLabel.moneyVelocity));
		jPanel1.add(chartPanelList.get(DataLabel.profitShare));
		jPanel1.add(chartPanelList.get(DataLabel.productionVolume));
		jPanel1.add(chartPanelList.get(DataLabel.doubtfulDebtRatio));
		jPanel1.add(chartPanelList.get(DataLabel.bankruptcies));
		jPanel1.add(chartPanelList.get(DataLabel.duration));
		JPanel jPanel2 = new JPanel(new GridLayout(3, 3, 10, 10));
		jPanel2.setBackground(new Color(238, 238, 238));
		jPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		chartPanelList.put(
				DataLabel.phillipsCurve,
				new ChartPanel(getScatterChart(simulator,
						DataLabel.phillipsCurve, "Unemployment", "Inflation")));
		jPanel2.add(chartPanelList.get(DataLabel.phillipsCurve));
		JTabbedPane jTabbedPane = new JTabbedPane();
		jTabbedPane.addTab("Pane 1", jPanel1);
		jTabbedPane.addTab("Pane 2", jPanel2);
		newWindow.getContentPane().add(jTabbedPane);
		jProgressBar = new JProgressBar(0, (int) simulator.getMaxSimulations());
		jProgressBar.setStringPainted(true);
		newWindow.getContentPane().add(jProgressBar, BorderLayout.SOUTH);
		newWindow.setVisible(true);
		return newWindow;
	}

	private JFreeChart getLineChart(Simulator simulator, String key,
			String yLabel) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(simulator.getMultiSimulationSeries().get(key + "-Q3"));
		dataset.addSeries(simulator.getMultiSimulationSeries().get(key + "-Q1"));
		dataset.addSeries(simulator.getMultiSimulationSeries().get(
				key + "-median"));
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(simulator.getMultiSimulationSeries().get(
				key + "-maxRegularValue"));
		dataset2.addSeries(simulator.getMultiSimulationSeries().get(
				key + "-minRegularValue"));
		JFreeChart chart = new JFreeChart(null, ChartFactory.createXYLineChart(
				null, null, yLabel, dataset, PlotOrientation.VERTICAL, false,
				false, false).getPlot());
		setChartTheme(chart);
		XYDifferenceRenderer renderer = new XYDifferenceRenderer();
		renderer.setPositivePaint(ChartsList.VERY_TRANSPARENT_RED);
		renderer.setNegativePaint(ChartsList.VERY_TRANSPARENT_RED);
		renderer.setSeriesPaint(0, ChartsList.VERY_LIGHT_RED);
		renderer.setSeriesPaint(1, ChartsList.VERY_LIGHT_RED);
		renderer.setSeriesPaint(2, ChartsList.RED);
		((XYPlot) chart.getPlot()).setRenderer(0, renderer);
		((XYPlot) chart.getPlot()).setDataset(1, dataset2);
		XYDifferenceRenderer renderer2 = new XYDifferenceRenderer();
		renderer2.setPositivePaint(ChartsList.ULTRA_TRANSPARENT_RED);
		renderer2.setNegativePaint(ChartsList.ULTRA_TRANSPARENT_RED);
		renderer2.setSeriesPaint(0, ChartsList.ULTRA_LIGHT_RED);
		renderer2.setSeriesPaint(1, ChartsList.ULTRA_LIGHT_RED);
		((XYPlot) chart.getPlot()).setRenderer(1, renderer2);
		chart.getXYPlot().getDomainAxis()
				.setRange(simulator.getBeta().min, simulator.getBeta().max);
		chart.removeLegend();
		return chart;
	}

	private void setChartTheme(JFreeChart chart) {
		StandardChartTheme chartTheme = new StandardChartTheme(
				"Standard Chart Theme");
		float size = chartTheme.getExtraLargeFont().getSize2D();
		Font titleFont = chartTheme.getExtraLargeFont().deriveFont(size - 4);
		chartTheme.setExtraLargeFont(titleFont);
		Font axisFont = chartTheme.getRegularFont();
		chartTheme.setLargeFont(axisFont);
		chartTheme.setChartBackgroundPaint(new Color(0, 0, 0, 1));
		chartTheme.setPlotBackgroundPaint(Color.WHITE);
		chartTheme.setDomainGridlinePaint(Color.GRAY);
		chartTheme.setRangeGridlinePaint(Color.GRAY);
		chartTheme.apply(chart);
	}

	private JFreeChart getScatterChart(Simulator simulator, String key,
			String xLabel, String yLabel) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries xySeries = (simulator.getMultiSimulationSeries()).get(key);
		dataset.addSeries(xySeries);
		JFreeChart chart = ChartFactory.createScatterPlot("", xLabel, yLabel,
				dataset, PlotOrientation.VERTICAL, false, false, false);
		setChartTheme(chart);
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseLinesVisible(false);
		renderer.setSeriesPaint(0, Color.GRAY);
		renderer.setSeriesFillPaint(0, ChartsList.LIGHT_TRANSPARENT_RED);
		renderer.setUseFillPaint(true);
		plot.setRenderer(renderer);
		return chart;
	}

}
