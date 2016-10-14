// This entire file is part of my masterpiece.
// Lucy Zhang

package cellsociety_team10.individualSimulations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cellsociety_team10.CA;
import cellsociety_team10.Cell;
import cellsociety_team10.DataSetup;
import cellsociety_team10.Simulation;
import javafx.scene.paint.Color;

/**
 * 
 * The purpose of the WaTor class is to run the WaTor simulation. I believe it
 * is well designed as I carefully abstracted highly specific functionalities
 * and created methods based on those. As a result, I use a number of highly
 * concise and human readable methods to execute all of the WaTor rules. Even
 * without knowing the exact criteria for the simulation, it is easy to get a
 * general idea of how the simulation is supposed to behanve by reading the
 * code.
 * 
 * @author Lucy Zhang
 *
 */
public class WaTor extends Simulation {
	private DataSetup data;
	private static int breedFishSurvivals;
	private static int breedSharkSurvivals;
	private static int sharkEnergy;

	public final int OCEAN = 1;
	public final static int SHARK = 2;
	public final static int FISH = 3;

	public WaTor(CA ca) {
		super(ca);
		data = new DataSetup("data/waTor.xml");
		breedFishSurvivals = Integer.parseInt(data.readFileForTag("numSurvivalsToBreedFish"));
		breedSharkSurvivals = Integer.parseInt(data.readFileForTag("numSurvivalsToBreedShark"));
		sharkEnergy = Integer.parseInt(data.readFileForTag("sharkEnergy"));

	}

	/**
	 * Sets the color mapping
	 * 
	 * @param colorMapping
	 */
	public void setColorMapping(Map<Integer, Color> colorMapping) {
		this.colorMapping = (HashMap<Integer, Color>) colorMapping;
	}

	/*
	 * Implementing abstract methods below
	 */

	protected void createColorMapping() {
		colorMapping.put(1, Color.BLUE); // ocean or kelp
		colorMapping.put(2, Color.YELLOW); // shark
		colorMapping.put(3, Color.GREEN); // fish
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cellsociety_team10.Simulation#update() Updates the grid based on
	 * WaTor rules
	 */
	public void update() {
		grid = ca.getGrid();
		Cell[][] temp = copyGrid();
		for (Cell[] r : grid) {
			for (Cell c : r) {
				HashSet<Cell> neighbors = checkNeighbor(temp, c, false, false);
				if (c.getState() == SHARK) {
					sharkMoves(neighbors, c);
				} else if (c.getState() == FISH) {
					fishMoves(neighbors, c);
				}
			}
		}

	}

	private void sharkMoves(Set<Cell> neighbors, Cell c) {
		if (fishExist((HashSet<Cell>) neighbors)) {
			eatRandomFish((HashSet<Cell>) neighbors, grid);
		} else {
			sharkStarve(c);
		}
		if (canBreed(c.getExtraState(), SHARK)) {
			breed((HashSet<Cell>) neighbors, c, grid);
			c.setExtraState(0);
		}
		commonSeaCreatureMoves(c, (HashSet<Cell>) neighbors, grid);
	}

	private void fishMoves(Set<Cell> neighbors, Cell c) {
		if (canBreed(c.getExtraState(), FISH)) {
			breed(neighbors, c, grid);
		}
		commonSeaCreatureMoves(c, neighbors, grid);
	}

	private void sharkStarve(Cell c) {
		if (c.getExtraState2() >= sharkEnergy) {
			c.setState(OCEAN); // starved and died
		} else {
			c.setExtraState2(c.getExtraState2() + 1);
		}
		setCellOfActualGrid(c, grid);
	}

	private void commonSeaCreatureMoves(Cell c, Set<Cell> neighbors, Cell[][] actualGrid) {
		incrementSurvivalTurns(c);
		moveToEmptyAdjCell(c, (HashSet<Cell>) neighbors, actualGrid);
	}

	private void incrementSurvivalTurns(Cell c) {
		c.setExtraState(c.getExtraState() + 1);

	}

	private boolean canBreed(double turnsSurvived, int seaCreatureType) {
		return (((seaCreatureType == SHARK) && (turnsSurvived == breedSharkSurvivals))
				|| ((seaCreatureType == FISH) && (turnsSurvived == breedFishSurvivals)));

	}

	private void breed(Set<Cell> neighbors, Cell currentCell, Cell[][] actualGrid) {
		if (emptyCellsExist((HashSet<Cell>) neighbors)) {
			ArrayList<Cell> emptyCells = getStateCells((HashSet<Cell>) neighbors, OCEAN);
			Cell emptyCell = getRandomCell(emptyCells);
			emptyCell.setState(currentCell.getState());
			emptyCell.setExtraState(0);
			setCellOfActualGrid(emptyCell, actualGrid);
			currentCell.setExtraState(0); // reset turns to 0
			setCellOfActualGrid(currentCell, actualGrid);
		}
	}

	private void setCellOfActualGrid(Cell tempCell, Cell[][] actualGrid) {
		int x = tempCell.getX();
		int y = tempCell.getY();
		actualGrid[x][y] = tempCell;
	}

	private boolean emptyCellsExist(HashSet<Cell> neighbors) {
		return (getStateCells(neighbors, OCEAN).size() > 0);
	}

	private boolean fishExist(HashSet<Cell> neighbors) {
		return (getStateCells(neighbors, FISH).size() > 0);
	}

	private void eatRandomFish(HashSet<Cell> neighbors, Cell[][] actualGrid) {
		ArrayList<Cell> edibleFishes = getStateCells(neighbors, FISH);
		Cell edibleFish = getRandomCell(edibleFishes);
		eatFish(edibleFish, actualGrid);
	}

	private void eatFish(Cell eatenFish, Cell[][] actualGrid) {
		eatenFish.setState(OCEAN);
		setCellOfActualGrid(eatenFish, actualGrid);
	}

	private void moveToEmptyAdjCell(Cell currentCell, HashSet<Cell> neighbors, Cell[][] actualGrid) {
		ArrayList<Cell> emptyCells = getStateCells(neighbors, OCEAN);
		if (emptyCells.size() > 0) {
			Cell emptyCell = getRandomCell(emptyCells);
			emptyCell.setState(currentCell.getState());
			emptyCell.setExtraState(currentCell.getExtraState());
			emptyCell.setExtraState2(currentCell.getExtraState2());
			setCellOfActualGrid(emptyCell, actualGrid);
			currentCell.setState(OCEAN);
			setCellOfActualGrid(currentCell, actualGrid);
		}
	}

	private ArrayList<Cell> getStateCells(HashSet<Cell> neighbors, int state) {
		ArrayList<Cell> stateCells = new ArrayList<Cell>();
		for (Cell c : neighbors) {
			if (c.getState() == state) {
				stateCells.add(c);
			}
		}
		return stateCells;
	}

	private Cell getRandomCell(ArrayList<Cell> cells) {
		if (cells.size() != 0) {
			Random rand = new Random();
			int pos = rand.nextInt(cells.size());
			return cells.get(pos);
		}
		return null;
	}

}
