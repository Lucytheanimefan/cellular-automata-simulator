package cellsociety_team10;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import shapes.*;
import javafx.scene.shape.Shape;

/**
 * @author Lucy Zhang, with modifications by Phil Foo
 *
 */
public class Graphics {
	public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety_team10.resources/";
	private ResourceBundle myResources;
	public static final double BUTTONXPOS = 2.5;
	private int rows, cols;
	private CA ca;
	private int clickState;

	private ShapeCreator shape;

	public Graphics() {

	}

	public Graphics(CA ca, int rows, int cols, String language) {
		this.ca = ca;
		this.rows = rows;
		this.cols = cols;
		this.myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
		clickState = 0;
		shape = new ShapeCreator();
	}

	/**
	 * Gets the click state, which is the integer representation of the cell
	 * state that was clicked
	 * 
	 * @return the click state
	 */
	public int getClickState() {
		return clickState;
	}

	/**
	 * Sets the click state
	 * 
	 * @param clickState
	 */
	public void setClickState(int clickState) {
		this.clickState = clickState;
	}

	/**
	 * Creates a BorderPane object
	 * 
	 * @param root
	 * @param width
	 * @param height
	 * @return
	 */
	public BorderPane createBorderPane(Group root, int width, int height) {
		BorderPane border = new BorderPane();
		border.setPrefWidth(width * 1.95);
		border.setPrefHeight(height);
		root.getChildren().add(border);
		return border;
	}

	private Button createButton(String label, double x, double y, Group root) {
		Button button = new Button(label);
		button.setLayoutX(x);
		button.setLayoutY(y);
		button.toFront();
		// ca.getMainPane().setBottom(button);
		root.getChildren().add(button);

		return button;
	}

	/**
	 * Generates a list of Shape objects used to populate the GUI
	 * 
	 * @param grid
	 *            The 2D array representation of the grid
	 * @param stateColors
	 *            The color mapping that maps color to integer representation
	 * @param cellWidth
	 *            The width of one cell
	 * @param cellHeight
	 *            The height of one cell
	 * @param isBorder
	 *            A boolean that is true if there is a border around the grid,
	 *            false otherwise
	 * @param topMargin
	 *            The height of the top margin
	 * @return A list of Shape objects
	 */
	public AbstractList<Shape> generateShapeList(Cell[][] grid, Map<Integer, Color> stateColors, double cellWidth,
			double cellHeight, boolean isBorder, int topMargin) {
		Color color;
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		ShapeCreator sc = new ShapeCreator();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				color = stateColors.get(grid[i][j].getState());
				Shape s = createShape(sc, j, i, ca.getGridShape(), cellWidth, cellHeight, isBorder, topMargin, color);
				shapes.add(s);
				ca.addClickListenerToShape(s, i, j, clickState);
			}
		}
		return shapes;
	}

	private Shape createShape(ShapeCreator sc, int j, int i, String shape, double cellWidth, double cellHeight,
			boolean isBorder, int topMargin, Color color) {
		Shape cellShape;
		if (shape.equals("rectangle")) {
			cellShape = sc.createRectangle(j, i, cellWidth, cellHeight, cols, Color.BLACK, color, isBorder, topMargin);
		} else if (shape.equals("triangle")) {
			cellShape = sc.createTriangle(j, i, cellWidth, cellHeight, cols, Color.BLACK, color, isBorder, topMargin);
		} else if (shape.equals("hexagon")) {
			cellShape = sc.createHexagon(j, i, cellWidth, cellHeight, cols, Color.BLACK, color, isBorder, topMargin);
		} else {
			cellShape = null;
		}
		return cellShape;
	}

	/**
	 * Creates a rectangular cell for the border that surround the grid
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param width
	 * @param height
	 * @param topMargin
	 * @param stroke
	 *            The outline color of the cell
	 * @param fill
	 *            The fill color of the cell
	 * @return THe rectangle cell
	 */
	public Rectangle createBorderCell(double x, double y, double width, double height, double topMargin, Color stroke,
			Color fill) {
		Rectangle r = new Rectangle();
		r.setX(x * width);
		r.setY(y * height + topMargin);
		r.setWidth(width);
		r.setHeight(height);
		setShapeSettings(r, stroke, fill, "cell");
		return r;
	}

	/**
	 * Sets the settings of a shape
	 * 
	 * @param shape
	 *            The shape whose settings need to be set
	 * @param stroke
	 *            The outline color
	 * @param fill
	 *            The fill color
	 * @param className
	 *            A class name
	 */
	public void setShapeSettings(Shape shape, Color stroke, Color fill, String className) {
		shape.setStroke(stroke);
		shape.setFill(fill);
		shape.getStyleClass().add(className);
	}

	/**
	 * Creates the button to change grid cell shapes
	 * 
	 * @param x
	 * @param y
	 * @param root
	 * @return the button
	 */
	public Button createChooseShapeGrid(double x, double y, Group root) {
		Button button = createButton("Change to random grid shape", x, y, root);
		button.setOnAction((ActionEvent e) -> {
			getRandomShape();
		});
		return button;
	}

	private void getRandomShape() {
		Random rand = new Random();
		String[] shapes = shape.getShapeOptions();
		int randomNum = rand.nextInt((shapes.length));
		String myShape = shapes[randomNum];
		if (!ca.getGridShape().equals(myShape)) {
			ca.setGridShape(myShape);
		} else {
			getRandomShape();
		}
	}

	/**
	 * Creates the choose new simulation button
	 * 
	 * @param x
	 * @param y
	 * @param root
	 * @return the button
	 */
	public Button createChooseNewSimButton(double x, double y, Group root) {
		Button button = createButton("Choose new simulation", x, y, root);
		button.setOnAction((ActionEvent e) -> {
			HomeSelection select = new HomeSelection();
			ca.getAnimation().getKeyFrames().clear();
			select.initHomeScreen();
		});
		return button;
	}

	/**
	 * Creates the button that saves the simulation information to an xml file
	 * 
	 * @param x
	 * @param y
	 * @param root
	 * @return the button
	 */
	public Button createXMLButton(double x, double y, Group root) {
		String fileName = ca.getNameOfSimulation();
		Button button = createButton("Save data to XML file", x, y, root);
		button.setOnAction((ActionEvent e) -> {
			DataOutput output = new DataOutput(ca, fileName + "_out");
		});
		return button;
	}

	/**
	 * Creates the play simulation button
	 * 
	 * @param animation
	 *            A Timeline object
	 * @param x
	 * @param y
	 * @param root
	 * @return the button
	 */
	public Button createPlayButton(Timeline animation, double x, double y, Group root) {

		Button button = createButton(myResources.getString("play"), x, y, root);
		button.setOnAction((ActionEvent e) -> {
			animation.play();
		});
		return button;
	}

	/**
	 * Creates the pause simulation button
	 * 
	 * @param animation
	 *            A Timeline object
	 * @param x
	 * @param y
	 * @param root
	 * @return the button
	 */
	public Button createPauseButton(Timeline animation, double x, double y, Group root) {

		Button button = createButton(myResources.getString("pause"), x, y, root); // file
		button.setOnAction((ActionEvent e) -> {
			animation.pause();
		});
		return button;
	}

	/**
	 * Creates the random initialize button
	 * 
	 * @param x
	 * @param y
	 * @param root
	 * @param sim
	 *            The Simulation object
	 * @return the button
	 */
	public Button createRandomInitializeButton(double x, double y, Group root, Simulation sim) {
		Button button = createButton("Random initialize", x, y, root); // file
		button.setOnAction((ActionEvent e) -> {
			ca.randomInitialize(sim.getColorMapping());
		});
		return button;
	}

	/**
	 * Creates the step through button
	 * 
	 * @param animation
	 *            A Timeline object
	 * @param x
	 * @param y
	 * @param root
	 * @return the button
	 */
	public Button createStepButton(Timeline animation, double x, double y, Group root) {
		Button button = createButton(myResources.getString("stepThrough"), x, y, root); // file
		button.setOnAction((ActionEvent e) -> {
			animation.play();
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							animation.pause();
						}
					});
				}
			}, 20 * ca.getFramesPerSecond());

		});
		return button;
	}

}
