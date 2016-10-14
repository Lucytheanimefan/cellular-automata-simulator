package cellsociety_team10;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;

import javafx.scene.paint.Color;

/**
 * This is the abstract simulation class serves as a model for all the
 * simulation contains the methods that are common for all the simulations
 * 
 * @author Phil Foo, Lucy Zhang, Yumin Zhang
 *
 */
public abstract class Simulation {
	protected int EMPTY_CELL = 0;
	protected static CA ca;
	protected Cell[][] grid;
	protected Map<Integer, Color> colorMapping = new HashMap<Integer, Color>();

	public Simulation(CA ca) {
		this.ca = ca;
		grid = ca.getGrid();
		createColorMapping();
	}

	public Simulation() {

	}

	public abstract void update();

	protected abstract void createColorMapping();

	/**
	 * for setting up the color of each cell in grid in the GUI
	 * 
	 * @return the pair of state in cell and corresponding color
	 */
	public Map<Integer, Color> getColorMapping() {
		return colorMapping;
	}

	/**
	 * 
	 * @param state
	 * @return the number of times that state appears in the grid
	 */

	public int getNumOfState(int state) {
		int numOfState = 0;
		for (Cell[] r : grid) {
			for (Cell c : r) {
				if (c.getState() == state) {
					numOfState++;
				}
			}
		}
		return numOfState;
	}

	/**
	 * for debugging purpose only
	 */
	public void printGrid() {
		for (Cell[] row : grid) {
			printRow(row);
		}
	}

	/**
	 * for debugging purpose only
	 */
	public void printNeighbors(HashSet<Cell> neighbors) {
		System.out.print("Neighbor: ");
		for (Cell c : neighbors) {
			System.out.print(c.getX() + "," + c.getY() + "; ");
		}
	}

	private static void printRow(Cell[] row) {
		for (Cell i : row) {
			System.out.print(i.getState());
			System.out.print("\t");
		}
		System.out.println();
	}

	/**
	 * make a copy of the current grid make sure the updates are based on the
	 * previous states of the grid rather than the half updated grid
	 * 
	 * @return a copy of the current grid
	 */
	public Cell[][] copyGrid() {
		Cell[][] copy = new Cell[ca.getRows()][ca.getCols()];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				copy[i][j] = new Cell(grid[i][j].getX(), grid[i][j].getY(), grid[i][j].getState());
			}
		}
		return copy;
	}

	/**
	 * get all the neighbors of the current Cell
	 * 
	 * @param grid
	 * @param cell
	 * @param wrapAround:
	 *            true if for the cell on the border, its neighbor includes the
	 *            cells on the opposite border
	 * @param corners:
	 *            true if the neighbor includes the four corners, i.e. top right
	 * @return a Hashset of all neighbor cells
	 */
	protected HashSet<Cell> checkNeighbor(Cell[][] grid, Cell cell, boolean wrapAround, boolean corners) {
		HashSet<Cell> neighbors = new HashSet<Cell>();
		int gridHeight = grid.length;
		int gridWidth = grid[0].length;

		int xLoc = cell.getX();
		int yLoc = cell.getY();

		// Get directly above
		if (xLoc > 0 && (grid[xLoc - 1][yLoc].getState() != 0)) {
			neighbors.add(grid[xLoc - 1][yLoc]);
		}

		// Get directly below
		if (xLoc < gridHeight - 1 && grid[xLoc + 1][yLoc].getState() != 0) {
			neighbors.add(grid[xLoc + 1][yLoc]);
		}

		// Get directly left
		if (yLoc > 0 && grid[xLoc][yLoc - 1].getState() != 0) {
			neighbors.add(grid[xLoc][yLoc - 1]);
		}

		// Get directly right
		if (yLoc < gridWidth - 1 && (grid[xLoc][yLoc + 1].getState() != 0)) {
			neighbors.add(grid[xLoc][yLoc + 1]);
		}

		if (corners) {
			// Get top left corner
			if (xLoc > 0 && yLoc > 0 && grid[xLoc - 1][yLoc - 1].getState() != 0) {
				neighbors.add(grid[xLoc - 1][yLoc - 1]);
			}

			// Get top right corner
			if (xLoc > 0 && yLoc < gridWidth - 1 && grid[xLoc - 1][yLoc + 1].getState() != 0) {
				neighbors.add(grid[xLoc - 1][yLoc + 1]);
			}

			// Get bottom left corner
			if (xLoc < gridHeight - 1 && yLoc > 0 && grid[xLoc + 1][yLoc - 1].getState() != 0
					&& !ca.getGrid().equals("hexagon")) {
				neighbors.add(grid[xLoc + 1][yLoc - 1]);
			}

			// Get bottom right corner
			if (xLoc < gridHeight - 1 && yLoc < gridWidth - 1 && grid[xLoc + 1][yLoc + 1].getState() != 0
					&& !ca.getGrid().equals("hexagon")) {
				neighbors.add(grid[xLoc + 1][yLoc + 1]);
			}
		}

		if (wrapAround) {
			// Add cell on right side of grid
			if (yLoc == 0 && grid[xLoc][gridWidth - 1].getState() != 0) {
				neighbors.add(grid[xLoc][gridWidth - 1]);
			}

			// Add cell on left side of grid
			if (yLoc == gridWidth - 1 && grid[xLoc][0].getState() != 0) {
				neighbors.add(grid[xLoc][0]);
			}
		}

		return neighbors;
	}

}
