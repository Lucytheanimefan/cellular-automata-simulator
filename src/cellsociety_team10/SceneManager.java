package cellsociety_team10;

import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import cellsociety_team10.individualSimulations.WaTor;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.util.AbstractList;

/**
 * This class manages all GUI elements in the simulation
 * @author Lucy Zhang, Phil Foo
 *
 */
public class SceneManager {
	public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety_team10.resources/";
	private final int TOP_MARGIN = 10;
	private final int BUTTON_SPACE = 80;
	private final double BUTTONXPOS = 2.5;
	private final int BORDER_SPACE = 2;
	private final int BUTTON_WIDTH_SCALE = 2;
	private final double BUTTON_HEIGHT_SCALE = 1.1;
	private final double BUTTON_HEIGHT_SCALE_ROW2 = 1.2;

	private BorderPane mainPane;
	private int rows, cols, width, height;
	private AbstractList<Shape> shapesOnGrid;
	private Graphics graphicLib;
	private Scene myScene;

	public SceneManager(int rows, int cols, int width, int height, Graphics graphicLib) {
		this.rows = rows;
		this.cols = cols;
		this.width = width;
		this.height = height;
		this.graphicLib = graphicLib;
	}

	/**
	 * Creates a new scene
	 * 
	 * @param stage
	 *            The current stage
	 * @param title
	 *            The title of the new scene
	 * @param width
	 * @param height
	 * @return the Group object
	 */
	public Group setupNewScene(Stage stage, String title, double width, double height) {
		stage.setTitle(title);
		Group root = new Group();
		myScene = new Scene(root, width, height, Color.WHITE);
		stage.setScene(myScene);
		stage.show();
		return root;
	}

	/**
	 * Initializes the static (stuff that doesn't update) parts of the GUI such
	 * as the buttons
	 * 
	 * @param root
	 *            The Group
	 * @param animation
	 *            The main Timeline object
	 * @param language
	 *            English
	 * @param sim
	 *            The Simulation object
	 */
	public void initStaticGui(Group root, Timeline animation, String language, Simulation sim) {
		ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
		double buttonHeight = height - (0.1 * BUTTON_SPACE);
		graphicLib.createPlayButton(animation, 0, buttonHeight, root);
		graphicLib.createPauseButton(animation, width / BUTTONXPOS, buttonHeight, root);
		graphicLib.createStepButton(animation, BUTTON_WIDTH_SCALE * width / BUTTONXPOS, buttonHeight, root);
		graphicLib.createXMLButton(0, buttonHeight * BUTTON_HEIGHT_SCALE, root);
		graphicLib.createChooseShapeGrid(width / BUTTONXPOS, buttonHeight * BUTTON_HEIGHT_SCALE, root);
		graphicLib.createChooseNewSimButton(0, buttonHeight * BUTTON_HEIGHT_SCALE_ROW2, root);
		graphicLib.createRandomInitializeButton(BUTTON_WIDTH_SCALE * width / BUTTONXPOS,
				buttonHeight * BUTTON_HEIGHT_SCALE_ROW2, root, sim);
	}

	/**
	 * Updates the entire GUI based on changes made due to the simulation rules
	 * 
	 * @param root
	 * @param width
	 * @param height
	 * @param stateColors
	 *            The color mapping of integer state to color
	 * @param needBorder
	 *            True if a border is desired, false otherwise
	 * @param borderColor
	 *            The border color
	 * @param data
	 *            A DataSetup object
	 * @param sim
	 *            A Simulation object
	 * @param grid
	 *            The 2D representation of the grid
	 * @param graph
	 *            A graph objecg
	 */
	public void updateView(Group root, int width, int height, Map<Integer, Color> stateColors, boolean needBorder,
			Color borderColor, DataSetup data, Simulation sim, Cell[][] grid, Graph_v2 graph) {
		mainPane.setTop(null);
		mainPane.setBottom(null);
		int dataPoint1;
		int dataPoint2;
		if (sim instanceof WaTor) {
			dataPoint1 = sim.getNumOfState(WaTor.FISH);
			dataPoint2 = sim.getNumOfState(WaTor.SHARK);
		} else {
			dataPoint1 = sim.getNumOfState(1);
			dataPoint2 = sim.getNumOfState(2);
		}
		graph.getNewDataPoints(dataPoint1, dataPoint2);
		if (dataPoint1 > dataPoint2) {
			graph.setMax(dataPoint1);
		} else {
			graph.setMax(dataPoint2);
		}
		mainPane.setLeft(null);
		refreshGridView(root, width, height, stateColors, needBorder, borderColor, grid);
	}

	/**
	 * Updates the grid UI
	 * 
	 * @param root
	 * @param width
	 * @param height
	 * @param stateColors
	 * @param needBorder
	 * @param borderColor
	 * @param grid
	 */
	public void refreshGridView(Group root, int width, int height, Map<Integer, Color> stateColors, boolean needBorder,
			Color borderColor, Cell[][] grid) {
		shapesOnGrid = new ArrayList<Shape>();

		double cellWidth = (needBorder) ? (width / (cols + BORDER_SPACE)) : (width / cols);
		double cellHeight = (needBorder) ? ((height - BUTTON_SPACE) / (rows + BORDER_SPACE))
				: ((height - BUTTON_SPACE) / rows);

		if (needBorder) {
			for (int i = 0; i < rows + BORDER_SPACE; i++) {
				for (int j = 0; j < cols + BORDER_SPACE; j++) {
					if (i == 0 || j == 0 || i == rows + 1 || j == cols + 1) {
						Shape s = graphicLib.createBorderCell(j, i, cellWidth, cellHeight, TOP_MARGIN, Color.BLACK,
								borderColor);
						shapesOnGrid.add(s);
					}
				}
			}
		}
		shapesOnGrid
				.addAll(graphicLib.generateShapeList(grid, stateColors, cellWidth, cellHeight, needBorder, TOP_MARGIN));
		populateMainPane(root);
	}

	private void populateMainPane(Group root) {
		Pane pane = new Pane();
		pane.getChildren().addAll(shapesOnGrid);
		mainPane = graphicLib.createBorderPane(root, width, height);
		BorderPane.setAlignment(root, Pos.CENTER);
		mainPane.setMinHeight(height);
		mainPane.setLeft(pane);
		mainPane.getLeft().setId("grid");
	}

	/**
	 * Initializes the graph
	 * 
	 * @param nameOfSimulation
	 * @return the graph
	 */
	public Graph_v2 initGraphs(String nameOfSimulation) {
		Graph_v2 graph = new Graph_v2();
		graph.createGraph(nameOfSimulation, mainPane);
		return graph;
	}
}
