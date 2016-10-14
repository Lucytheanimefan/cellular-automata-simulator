package shapes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import java.util.List;
import static java.util.Arrays.asList;

/**
 * @author Phil FOo
 *
 */
public class ShapeCreator {
	private String[] shapeOptions = { "rectangle", "hexagon", "triangle" };

	public ShapeCreator() {

	}

	/**
	 * @return the shape options
	 */
	public String[] getShapeOptions() {
		return shapeOptions;
	}

	/**
	 * Creates rectangular grids
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param cols
	 * @param stroke
	 * @param fill
	 * @param isBorder
	 * @param topMargin
	 * @return the rectangle object
	 */
	public Rectangle createRectangle(double x, double y, double width, double height, int cols, Color stroke,
			Color fill, boolean isBorder, int topMargin) {
		Rectangle r = new Rectangle();
		// Decide where to place the cell based on whether the border is on
		double xLocation = x * width;
		double yLocation = y * height + topMargin;
		if (isBorder) {
			xLocation += width;
			yLocation += height;
		}

		r.setX(xLocation);
		r.setY(yLocation);
		r.setWidth(width);
		r.setHeight(height);
		r.setStroke(stroke);
		r.setFill(fill);
		r.getStyleClass().add("cell");
		return r;
	}

	/**
	 * Creates the hexagonal grid
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param dimension
	 * @param stroke
	 * @param fill
	 * @param isBorder
	 * @param topMargin
	 * @return the hexagon object
	 */
	public Polygon createHexagon(double x, double y, double width, double height, int dimension, Color stroke,
			Color fill, boolean isBorder, int topMargin) {

		boolean toMoveUp = (x % 2 == 0) ? false : true;

		// topLeft X and Y
		double topLeftX = ((x * width) + (width / 2));
		double topLeftY = (y * height) + topMargin;

		// topRight X and Y
		double topRightX = ((x + 1) * width);
		double topRightY = (y * height) + topMargin;

		// left X and Y
		double leftX = (x * width);
		double leftY = ((y * height) + (height / 2)) + topMargin;

		// right X and Y
		double rightX = ((x + 1) * width) + (width / 2);
		double rightY = ((y * height) + (height / 2)) + topMargin;

		// bottom left X and Y
		double bottomLeftX = ((x * width) + (width / 2));
		double bottomLeftY = ((y + 1) * height) + topMargin;

		// bottom right X and Y
		double bottomRightX = ((x + 1) * width);
		double bottomRightY = ((y + 1) * height) + topMargin;

		if (toMoveUp) {
			topLeftY -= (height / 2);
			topRightY -= (height / 2);
			leftY -= (height / 2);
			rightY -= (height / 2);
			bottomLeftY -= (height / 2);
			bottomRightY -= (height / 2);
		}

		if (isBorder) {
			topLeftX += width;
			topLeftY += height;
			topRightX += width;
			topRightY += height;
			bottomRightX += width;
			bottomRightY += height;
			bottomLeftX += width;
			bottomLeftY += height;
			leftX += width;
			leftY += height;
			rightX += width;
			rightY += height;
		}

		if (x == dimension - 1) {
			rightX -= (width / 2);
		}

		if (y == 0 && (x % 2 == 1)) {
			topRightY += (height / 2);
			topLeftY += (height / 2);
		}

		List<Double> points = asList(topLeftX, topLeftY, leftX, leftY, bottomLeftX, bottomLeftY, bottomRightX,
				bottomRightY, rightX, rightY, topRightX, topRightY);
		Polygon h = createPolygon(stroke, fill, points);
		return h;
	}

	/**
	 * Creates the triangular grid
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param numCols
	 * @param stroke
	 * @param fill
	 * @param isBorder
	 * @param topMargin
	 * @return the triangle object
	 */
	public Polygon createTriangle(double x, double y, double width, double height, int numCols, Color stroke,
			Color fill, boolean isBorder, int topMargin) {
		double newWidth = 2 * width;
		boolean toFlip = ((x + y) % 2) == 0 ? false : true;

		double leftX = x * (newWidth / 2);
		double leftY = (y + 1) * height + topMargin;

		double topX = (x + 1) * (newWidth / 2);
		double topY = y * height + topMargin;

		double rightX = (x + 2) * (newWidth / 2);
		double rightY = (y + 1) * height + topMargin;

		if (isBorder) {
			leftX += width;
			topX += width;
			rightX += width;

			leftY += height;
			topY += height;
			rightY += height;
		}

		if (toFlip) {
			leftY -= height;
			topY += height;
			rightY -= height;
		}

		if (x == numCols - 1) {
			rightX -= (newWidth / 2);
		}
		List<Double> points = asList(leftX, leftY, topX, topY, rightX, rightY);
		Polygon p = createPolygon(stroke, fill, points);
		return p;
	}

	private Polygon createPolygon(Color stroke, Color fill, List<Double> points) {
		Polygon p = new Polygon();
		p.getPoints().addAll(points);
		p.setStroke(stroke);
		p.setFill(fill);
		return p;
	}
}
