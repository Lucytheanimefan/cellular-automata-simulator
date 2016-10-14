package cellsociety_team10.individualSimulations;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashSet;

import cellsociety_team10.CA;
import cellsociety_team10.Cell;
import cellsociety_team10.Simulation;

public class Schelling extends Simulation{
	private final int AGENT_X = 1;
	private final int AGENT_Y = 2;
	private final int NUM_POSSIBLE_NEIGHBORS = 8;
	private final double similarRatio;
	private ArrayList<Cell> emptyCells;
	
	/*
	 * Takes in a CA object, which gives the simulation access to the grid.
	 * Also initializes similarRatio, which is the ratio needed for an agent to not move.
	 * */
	public Schelling(CA ca, double ratio){
		super(ca);
		this.similarRatio = ratio;
		System.out.println("Schelling ratio: "+ratio);
	}
	
	/*
	 * Abstract void method inherited from the Simulation superclass. Initializes the colorMapping
	 * object with the appropriate agent colors.
	 * */
	protected void createColorMapping(){
		colorMapping.put(EMPTY_CELL, Color.WHITE);
		colorMapping.put(AGENT_X, Color.RED);
		colorMapping.put(AGENT_Y, Color.BLUE);
	}
	
	/*
	 * Abstract void method inherited from the Simulation superclass, running the update loop.
	 * First, creates a temporary copy of the original grid. Loops through all the cells of the grid,
	 * skipping the cell if the cell is empty.
	 * If the cell is not empty, check if its number of similar neighbors fulfills the needed ratio.
	 * If not, move the cell to a random location.
	 * */
	public void update(){
		grid = ca.getGrid();

		Cell[][] temp = copyGrid();
		emptyCells = findEmptyCells(temp);
		for (int i = 0; i < temp.length; i++){
			for (int j = 0; j < temp[0].length; j++){
				if (temp[i][j].getState() == EMPTY_CELL){
					continue;
				}
				
				//Check number of similar neighbors
				HashSet<Cell> neighbors = checkNeighbor(temp, temp[i][j], false, true);
				double numSimilarNeighbors = 0;
				for (Cell c: neighbors){
					if (c.getState() == temp[i][j].getState()){
						//System.out.println(temp[i][j].getState());
						numSimilarNeighbors++;
					}
				}
				
				//Call move cell method
				if ((numSimilarNeighbors / NUM_POSSIBLE_NEIGHBORS) < similarRatio){
					moveCell(temp, grid, temp[i][j]);
				}
			}
		}
	}
	
	/*
	 * Selects a random cell from the list of empty cells, then swaps it with
	 * the cell passed in to the function.
	 * @param {Cell[][]} copyOfGrid - old stored grid
	 * @param {Cell[][]} actualGrid - actual simulation grid, which is to be changed
	 * @param {Cell} c - unhappy Cell which is to be moved
	 * */
	private void moveCell(Cell[][] copyOfGrid, Cell[][] actualGrid, Cell c){
		//Get new cell and its x and y coordinates in the grid
		int randomIndex = (int) Math.floor(Math.random()*emptyCells.size());
		Cell newCell = emptyCells.get(randomIndex);
		int newXLoc = newCell.getX(); int newYLoc = newCell.getY();
		
		//Swap!
		Cell temp = newCell;
		temp.setX(c.getX()); temp.setY(c.getY());
		actualGrid[newXLoc][newYLoc] = c;
		actualGrid[c.getX()][c.getY()] = temp;
		c.setX(newXLoc); c.setY(newYLoc);
		
		//Remove old empty cell from emptyCell list, add c's old Cell to the emptyCell list
		emptyCells.remove(randomIndex);
		emptyCells.add(c);
	} 
	
	/*
	 * Accepts a grid and returns an ArrayList of Cells whose states = EMPTY_CELL.
	 * @param {Cell[][]} grid - grid to be checked for empty Cells
	 * @return {ArrayList<Cell>} emptyCells - list of empty cells
	 * */
	public ArrayList<Cell> findEmptyCells(Cell[][] grid){
		ArrayList<Cell> emptyCells = new ArrayList<Cell>();
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[0].length; j++){
				if (grid[i][j].getState() == EMPTY_CELL){
					emptyCells.add(grid[i][j]);
				}
			}
		}
		return emptyCells;
	}
}
