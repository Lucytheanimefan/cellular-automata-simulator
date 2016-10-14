package cellsociety_team10.individualSimulations;

import javafx.scene.paint.Color;

import java.util.HashSet;

import cellsociety_team10.CA;
import cellsociety_team10.Cell;
import cellsociety_team10.Simulation;

public class CatchingFire extends Simulation{
	
	private final int EMPTY = 0;
	private final int TREE = 1;
	private final int BURNING = 2;
	private double prob;
	
	public CatchingFire(CA ca, double probility) {
		super(ca);
		this.prob = probility;
		createColorMapping();
	}
	
	public double getProbability(){
		return prob;
	}
	
	public void setProbability(double prob2){
		this.prob = prob2;
	}
		
	
	@Override
	protected void createColorMapping() {
		colorMapping.put(EMPTY, Color.YELLOW); 
		colorMapping.put(TREE, Color.GREEN); 
		colorMapping.put(BURNING, Color.RED); 

	}
	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see cellsociety_team10.Simulation#update()
	 * update the entire grid based on the rule of spreading the fire
	 * if there is a tree next to a burning site, the tree has a chance to burn
	 * the chance is the xml input named prob 
	 */
	@Override
	public void update() {
		System.out.println("CatchingFire");
		grid=ca.getGrid();
		//printGrid();
		Cell[][] temp = copyGrid();
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[0].length; j++){
				if (temp[i][j].getState() == BURNING){
					HashSet<Cell> neighbors = checkNeighbor(temp, temp[i][j], false, false);
					for (Cell cell : neighbors){
						if (cell.getState() == TREE){
							if (Math.random() >= (1-prob)){
								grid[cell.getX()][cell.getY()].setState(BURNING);
							}
						}
					}					
					grid[i][j].setState(EMPTY);
				}
			}
		}
	}

}
