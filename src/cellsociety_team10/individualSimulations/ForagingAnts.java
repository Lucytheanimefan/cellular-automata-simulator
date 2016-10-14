package cellsociety_team10.individualSimulations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import cellsociety_team10.CA;
import cellsociety_team10.Cell;
import cellsociety_team10.DataSetup;
import cellsociety_team10.Simulation;
import javafx.scene.paint.Color;

public class ForagingAnts extends Simulation{
	private final int EMPTY = 0;
	private final int ANT = 1;
	private final int FOOD = 2;
	private final int HOME = 3;
	private final int HAS_FOOD = 1; //extra state1 stores has_FOOD
	private final int NO_FOOD = 0;
	// extra state2 stores FOOD pheromones
	// extra state3 stores HOME pheromones
	private DataSetup data;
	private int homeLocationX;
	private int homeLocationY;
	private int foodLocationX;
	private int foodLocationY;
	private Random randomChoose = new Random();
	private double maxHomePheromones;
	private double maxFoodPheromones;
	private final int SUB_AMOUNT  = 2;

	


	public ForagingAnts(CA ca) {
		super(ca);
		readInput();
		grid[homeLocationX][homeLocationY].setExtraState2(Double.MAX_VALUE);
		grid[foodLocationX][foodLocationY].setExtraState3(Double.MAX_VALUE);

	}

	/*
	 * setup the variables that are unique for this simulations
	 * they are all read in from xml file
	 */
	public void readInput(){
		data = new DataSetup("data/foragingAnt.xml");
		homeLocationX = Integer.parseInt(data.readFileForTag("homeLocationX"));
		homeLocationY = Integer.parseInt(data.readFileForTag("homeLocationY"));
		foodLocationX = Integer.parseInt(data.readFileForTag("foodLocationX"));
		foodLocationY = Integer.parseInt(data.readFileForTag("foodLocationY"));
		maxHomePheromones = Double.parseDouble(data.readFileForTag("maxHomePheromones"));
		maxFoodPheromones = Double.parseDouble(data.readFileForTag("maxFoodPheromones"));

	}

	public void setColorMapping(Map<Integer, Color> colorMapping) {
		this.colorMapping = (HashMap<Integer, Color>) colorMapping;
	}



	@Override
	protected void createColorMapping() {
		colorMapping.put(EMPTY, Color.WHITE); 
		colorMapping.put(ANT, Color.YELLOW); 
		colorMapping.put(FOOD, Color.RED); 
		colorMapping.put(HOME, Color.BLUE); 
	}


	private void ant_return_to_home(Cell c){
		Cell moveTo = selectLocation(c, HOME);;
		if(atFood(c)){
			c.setState(FOOD);		
		}
		
		else{
			dropFoodPheromones(c, moveTo);
			moveTo.setState(ANT);
			if(atHome(moveTo)){
				moveTo.setState(NO_FOOD);
			}	
		}
				
	}

	private void find_food_source(Cell c){
		Cell moveTo = selectLocation(c, FOOD);;
		if(atHome(c)){
			c.setState(HOME);		
		}
		
		else{
			dropHomePheromones(c, moveTo);
			moveTo.setState(ANT);
			if(atFood(moveTo)){
				moveTo.setState(HAS_FOOD);
			}	
		}
	}

	/*
	 * choose the location where ant wants to be next at
	 */
	private Cell selectLocation(Cell c, int foodOrHome){
		grid=ca.getGrid();
		Cell[][] temp = copyGrid();
		HashSet<Cell> neighbors = checkNeighbor(temp, c , false, true);
		
		Cell direction = null;
		if(foodOrHome == FOOD){
			direction =  getMaxFoodDirection(neighbors);		
		}
		else if (foodOrHome == HOME){
			direction = getMaxHomeDirection(neighbors);
		}		
		return direction;
	}
	
	/*
	 * among all the neighbors, get the cell with max food pheromones
	 */
	private Cell getMaxFoodDirection(HashSet<Cell> neighbors){
		double maxFood = 0;
		Cell maxFoodCell = null;
		for (Cell c: neighbors){
			if(c.getExtraState2() > maxFood){
				maxFood = c.getExtraState2();
				maxFoodCell = c;
			}
		}	
		if(maxFood == 0){
			int cellNumber = randomChoose.nextInt(neighbors.size());
			int i = 0;
			for(Cell c: neighbors){
				if(i == cellNumber)
					return c;
				i++;
			}
		}		
		return maxFoodCell;
	}
	
	/*
	 * among all the neighbors, get the cell with max home pheromones
	 */
	private Cell getMaxHomeDirection(HashSet<Cell> neighbors){
		double maxHome = 0;
		Cell maxHomeCell = null;
		for (Cell c: neighbors){
			if(c.getExtraState2() > maxHome){
				maxHome = c.getExtraState3();
				maxHomeCell = c;
			}
		}	
		if(maxHome == 0){
			int cellNumber = randomChoose.nextInt(neighbors.size());
			int i = 0;
			for(Cell c: neighbors){
				if(i == cellNumber)
					return c;
				i++;
			}
		}		
		return maxHomeCell;
	}
	
	/*
	 * check if an ant is at home
	 */
	private Boolean atHome(Cell c){
		return (c.getX()== homeLocationX && c.getY() == homeLocationY);
	}
	
	/*
	 * check if an ant is at food source
	 */
	private Boolean atFood(Cell c){
		return (c.getX()== foodLocationX && c.getY() == foodLocationY);
	}

	private void dropHomePheromones(Cell c, Cell maxCell){
		if(atHome(c))
			c.setExtraState3(maxHomePheromones);
		
		else{
			double d = maxCell.getExtraState3() - SUB_AMOUNT - c.getExtraState3();
			if (d > 0)
				c.setExtraState3(d);			
		}
	}


	private void dropFoodPheromones(Cell c, Cell maxCell){
		if(atFood(c))
			c.setExtraState2(maxFoodPheromones);
		
		else{
			double d = maxCell.getExtraState2() - SUB_AMOUNT - c.getExtraState2();
			if (d > 0)
				c.setExtraState2(d);			
		}
	}
	
	
	private void ant_forage(Cell c){
		if (c.getState() == ANT){
			if (c.getExtraState() == HAS_FOOD){
				ant_return_to_home(c);
			}
			else{
				find_food_source(c);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * @see cellsociety_team10.Simulation#update()
	 */
	@Override
	public void update() {
		for (Cell[] r : grid){
			for(Cell c: r){
				ant_forage(c);
			}
		}
	}

}
