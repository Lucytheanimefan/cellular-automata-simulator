package cellsociety_team10.individualSimulations;

import java.util.HashSet;

import cellsociety_team10.CA;
import cellsociety_team10.Cell;
import cellsociety_team10.Simulation;
import javafx.scene.paint.Color;

public class GameOfLife extends Simulation{
	private final int DEATH = 1;
	private final int LIVING = 2;
	private final int LIVING_CONDITION_1 = 2;
	private final int LIVING_CONDITION_2 = 3;
	private final int DEATH_OF_LONELINESS = 2;
	private final int DEATH_OF_OVERPOPULATION = 4;

	

	/*
	 * creates the grid and set up basic info
	 */
	public GameOfLife(CA ca) {
		super(ca);
		//createColorMapping();
	}

	/*
	 * for GUI
	 * @see cellsociety_team10.Simulation#createColorMapping()
	 */
	@Override  
	protected void createColorMapping() {
		colorMapping.put(DEATH, Color.GREY); 
		colorMapping.put(LIVING, Color.WHITE); 
	}
	
	/*
	 * @see cellsociety_team10.Simulation#update()
	 * updates according to the rule for game of life
	 */
	
	@Override
	public void update(){
		System.out.println("GameOfLife");
		//printGrid();
		grid=ca.getGrid();
		Cell[][] temp = copyGrid();
		
		System.out.println("");
		for (Cell[] r : grid){
			for(Cell c: r){
				HashSet<Cell> neighbors = checkNeighbor(temp, c , false, true);
				int numberOflivingNeighbors = 0;
				for(Cell cell : neighbors){
					if(cell.getState() == LIVING)
						numberOflivingNeighbors++;
				}
				
				if(numberOflivingNeighbors < DEATH_OF_LONELINESS || numberOflivingNeighbors > DEATH_OF_OVERPOPULATION){
					c.setState(DEATH);
				}

				else if(numberOflivingNeighbors == LIVING_CONDITION_1 || numberOflivingNeighbors == LIVING_CONDITION_2){
					c.setState(LIVING);
				}			
			}
		}
	}


}
