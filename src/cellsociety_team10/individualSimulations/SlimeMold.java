package cellsociety_team10.individualSimulations;

import java.util.HashSet;

import cellsociety_team10.CA;
import cellsociety_team10.Cell;
import cellsociety_team10.DataSetup;
import cellsociety_team10.Simulation;
import javafx.scene.paint.Color;

public class SlimeMold extends Simulation{
	private DataSetup data;
	private double sniffThreshold;
	private double diffusionRate;
	private double evaporationRate;
	private double sniffAmount;
	private final int MOLD = 1;
	private final int EMPTY = 0;
	// extra state2 stores chemical gradient


	public SlimeMold(CA ca) {
		super(ca);
		readInput();
		initSniff();
	}
	
	/*
	 * read in the special parameter for this simulation from xml
	 */
	private void readInput(){
		data = new DataSetup("data/slimeMold.xml");
		sniffThreshold = Double.parseDouble(data.readFileForTag("sniffThreshold"));
		diffusionRate = Double.parseDouble(data.readFileForTag("diffusionRate"));
		evaporationRate = Double.parseDouble(data.readFileForTag("evaporationRate"));
		sniffAmount = Double.parseDouble(data.readFileForTag("sniffAmount"));
	}
	
	/*
	 * intilialize the sniffAmount for the MOLD cells
	 */
	private void initSniff(){
		for (Cell[] r : grid){
			for(Cell c: r){
				if(c.getExtraState() == MOLD)
					c.setExtraState2(sniffAmount);
			}
		}		
	}


	@Override
	protected void createColorMapping() {
		colorMapping.put(MOLD, Color.GREEN); 
		colorMapping.put(EMPTY, Color.GREY); 		
	}
	
	/*
	 * update the chemical in cell based on diffusion and evaporation
	 */
	private void updateChemical(Cell c, HashSet<Cell> neighbors){
		double sumOfDiffuseIn = 0;
		for (Cell i : neighbors){
			sumOfDiffuseIn += i.getExtraState2();
		}
		c.setExtraState2(c.getExtraState2() * (1 - evaporationRate) + diffusionRate * sumOfDiffuseIn);
	}

	/*
	 * move mold based on the chemical
	 * mold moves only if the chemical is greater than the threshold
	 */
	private void moveMold(Cell c, HashSet<Cell> neighbors){
		double maxChemical = -1;
		Cell maxChemCell = null;
		for (Cell i : neighbors){
			if(i.getExtraState2() > maxChemical){
				maxChemical = i.getExtraState2();
				maxChemCell = i;
			}
		}
		if(maxChemical > sniffThreshold){
			c.setState(EMPTY);
			maxChemCell.setState(MOLD);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see cellsociety_team10.Simulation#update()
	 * update the entire grid
	 */
	public void update() {
		grid=ca.getGrid();
		Cell[][] temp = copyGrid();		
		for (Cell[] r : grid){
			for(Cell c: r){
				HashSet<Cell> neighbors = checkNeighbor(temp, c , false, true);
				updateChemical(c, neighbors);
				moveMold(c,neighbors);
			}
		}
		
	}

}
