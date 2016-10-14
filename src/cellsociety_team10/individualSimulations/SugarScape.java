package cellsociety_team10.individualSimulations;

import java.util.HashSet;

import cellsociety_team10.CA;
import cellsociety_team10.Cell;
import cellsociety_team10.DataSetup;
import cellsociety_team10.Simulation;
import javafx.scene.paint.Color;

public class SugarScape extends Simulation {
	private DataSetup data;
	private final int ANGENT = 1;
	private final int EMPTY = 0;
	private double sugarMetabolism; 
	private double initialSugar;
	//extra state2 stores sugar amount
	

	public SugarScape(CA ca) {
		super(ca);
		data = new DataSetup("data/sugarScape.xml");
		sugarMetabolism = Double.parseDouble(data.readFileForTag("sugarMetabolism"));	
		initialSugar = Double.parseDouble(data.readFileForTag("initialSugar"));	
	}
	
	/*
	 * initialize the amount of sugar in the agent cell
	 */
	private void initSugar(){
		for (Cell[] r : grid){
			for(Cell c: r){
				if(c.getState() == ANGENT)
					c.setExtraState2(initialSugar);
			}
		}
	}

	@Override
	protected void createColorMapping() {
		colorMapping.put(ANGENT, Color.ORANGE); 
		colorMapping.put(EMPTY, Color.GREY); 			
	}
	
	/*
	 * move agent based on the vacancy and sugar level in the neighbors
	 */
	private void moveAngent(Cell c, HashSet<Cell> neighbors){
		Cell maxSugarVacant = null;
		double maxSugar = -1;
		for(Cell i : neighbors){
			if(i.getState() == EMPTY && i.getExtraState2() > maxSugar){
				maxSugar = i.getExtraState2();
				maxSugarVacant = i;				
			}
		}
		
		c.setState(EMPTY);
		if((c.getExtraState2() + maxSugar - sugarMetabolism) > 0)		
			maxSugarVacant.setState(ANGENT);
	}
	
	/*
	 * (non-Javadoc)
	 * @see cellsociety_team10.Simulation#update()
	 * updates the entire grid
	 */
	@Override
	public void update() {
		grid=ca.getGrid();
		Cell[][] temp = copyGrid();		
		for (Cell[] r : grid){
			for(Cell c: r){
				HashSet<Cell> neighbors = checkNeighbor(temp, c , false, true);
				if(c.getState() == ANGENT){
					moveAngent(c, neighbors);
				}
			}
		}		
	}

}
