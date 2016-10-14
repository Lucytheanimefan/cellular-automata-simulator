package cellsociety_team10;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

/**
 * This class creates the live updating graph
 * @author Lucy Zhang
 *
 */
public class Graph_v2 {
	private LineChart<Number, Number> chart;

	private XYChart.Series<Number, Number> dataSeries;
	private XYChart.Series<Number, Number> dataSeries2;

	private NumberAxis xAxis;

	private Timeline animation;

	private double sequence = 0;
	private int dataPoint1;
	private int dataPoint2;
	private final int MAX_DATA_POINTS = 50;
	private double max;

	public Graph_v2() {
		animation = new Timeline();
		animation.getKeyFrames().add(new KeyFrame(Duration.millis(1000), (ActionEvent actionEvent) -> plotTime()));
		animation.setCycleCount(Animation.INDEFINITE);
	}

	/**
	 * Sets the max variable
	 * 
	 * @param max
	 */
	public void setMax(int max) {
		this.max = 1.1 * max;
	}

	private Parent createContent(String[] seriesNames) {

		xAxis = new NumberAxis(0, MAX_DATA_POINTS + 1, 2);
		final NumberAxis yAxis = new NumberAxis();
		chart = new LineChart<>(xAxis, yAxis);

		// setup chart
		chart.setAnimated(false);
		chart.setHorizontalGridLinesVisible(true);
		chart.setLegendVisible(true);
		chart.setTitle("Animated Line Chart");
		xAxis.setLabel("X Axis");
		xAxis.setForceZeroInRange(false);

		yAxis.setLabel("Y Axis");
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "", null));
		yAxis.setUpperBound(max);

		dataSeries = new XYChart.Series<>();
		dataSeries2 = new XYChart.Series<>();
		setSeriesNames(seriesNames);
		dataSeries.getData().add(new XYChart.Data<Number, Number>(++sequence, 0));
		dataSeries2.getData().add(new XYChart.Data<Number, Number>(++sequence, 0));
		return chart;
	}

	@SuppressWarnings("unchecked")
	private void setSeriesNames(String[] seriesNames) {
		dataSeries.setName(seriesNames[0]);
		dataSeries2.setName(seriesNames[1]);
		chart.getData().addAll(dataSeries, dataSeries2);
	}

	private String[] getSeriesNames(String title) {
		if (title.toLowerCase().indexOf("wator") != CA.CHECK_FALSE) {
			String[] series = { "Fish", "Shark" };
			return series;
		} else if (title.toLowerCase().indexOf("gameoflife") != CA.CHECK_FALSE) {
			String[] series = { "Alive", "Dead" };
			return series;
		} else if (title.toLowerCase().indexOf("catchingfire") != CA.CHECK_FALSE) {
			String[] series = { "Tree", "Burning" };
			return series;
		} else if (title.toLowerCase().indexOf("schelling") != CA.CHECK_FALSE) {
			String[] series = { "Agent X", "Agent Y" };
			return series;
		} else
			return null;
	}

	/**
	 * Sets the new data points used to update the graph
	 * 
	 * @param dataPoint1
	 * @param dataPoint2
	 */
	public void getNewDataPoints(int dataPoint1, int dataPoint2) {
		this.dataPoint1 = dataPoint1;
		this.dataPoint2 = dataPoint2;
	}

	private void plotTime() {
		dataSeries.getData().add(new XYChart.Data<Number, Number>(++sequence, dataPoint1));
		dataSeries2.getData().add(new XYChart.Data<Number, Number>(++sequence, dataPoint2));
		// after 25hours delete old data
		if (sequence > MAX_DATA_POINTS) {
			dataSeries.getData().remove(0);
			dataSeries2.getData().remove(0);
		}

		// every hour after 24 move range 1 hour
		if (sequence > MAX_DATA_POINTS - 1) {
			xAxis.setLowerBound(sequence - MAX_DATA_POINTS - 1);
			xAxis.setUpperBound(sequence + 1);
		}
	}

	private void play() {
		animation.play();
	}

	/**
	 * Creates the graph and sets it in the pane
	 * 
	 * @param title
	 *            The title of the simulation to graph
	 * @param mainPane
	 */
	public void createGraph(String title, BorderPane mainPane) {
		String[] seriesNames = getSeriesNames(title);
		mainPane.setRight(createContent(seriesNames));
		mainPane.getRight().setStyle("-fx-padding:  0 0 0 60");
		play();
	}

}
