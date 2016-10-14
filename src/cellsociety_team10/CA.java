package cellsociety_team10;
import java.util.HashMap;
import java.util.Map;
import cellsociety_team10.individualSimulations.CatchingFire;
import cellsociety_team10.individualSimulations.GameOfLife;
import cellsociety_team10.individualSimulations.Schelling;
import cellsociety_team10.individualSimulations.WaTor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;


/**
 * CA class is the master class factored out of main
 * initialize the whole animation of grid
 * trigger everything in this project
 * 
 * @author Phil Foo, Lucy Zhang, Yumin Zhang
 */
public class CA {
	private int frames_per_second;
	private int millisecond_delay;
	private Cell[][] grid;
	private int rows;
	private int cols;
	public static final int CHECK_FALSE = -1;
	private Graphics graphic;
	private int width;
	private int height;
	
	private String nameOfSimulation;
	private Timeline animation = new Timeline();
	private Main main;
	private SceneManager viewManager;
	private String gridShape;
	private String initialType;

	
	private Simulation sim;

	public CA() {
		frames_per_second = 25;
		millisecond_delay = 10000 / frames_per_second;
		this.gridShape = "rectangle";
	}

	public CA(int x, int y, int width, int height, String initialType) {
		this.grid = new Cell[x][y];
		this.rows = y;
		this.cols = x;
		this.width = width;
		this.height = height;
		this.frames_per_second = 25;
		this.millisecond_delay = 10000 / frames_per_second;
		this.graphic = new Graphics(this, rows, cols, "English");
		this.main = new Main();
		this.gridShape = "rectangle";
		this.initialType =  initialType;
	}
	
	public String getGridShape() {
		return gridShape;
	}
	public void setGridShape(String gridShape) {
		this.gridShape = gridShape;
	}
	
	public Timeline getAnimation() {
		return animation;
	}

	public int getFramesPerSecond() {
		return frames_per_second;
	}

	public int getMillisecond_delay() {
		return millisecond_delay;
	}

	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Cell[][] getGrid() {
		return grid;
	}

	public String getNameOfSimulation() {
		return nameOfSimulation;
	}

	public void initialize(int[] initialState) {
		int cnt = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = new Cell(i, j, initialState[cnt]);
				cnt++;
			}
		}
	}

	public void randomInitialize(Map<Integer, Color> colorMapping) {
		int[] randomStatesArray = new int[colorMapping.size()];
		int index = 0;
		for (Integer state : colorMapping.keySet()) {
			randomStatesArray[index] = state;
			index++;
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = new Cell(i, j, randomStatesArray[(int) Math.floor(Math.random() * colorMapping.size())]);
			}
		}
	}

	/*------------------------GUI Stuff---------------------*/

	private void initializeDataFromXML(DataSetup data){
		HashMap<String, String> generalInfo = data.getGeneralInfo();
		rows = Integer.parseInt(generalInfo.get("dimensionx"));
		cols=Integer.parseInt(generalInfo.get("dimensiony"));
		width = Integer.parseInt(generalInfo.get("width"));
		height = Integer.parseInt(generalInfo.get("height"));
		data.setInitialStates(this, generalInfo.get("initialState"));
		initialType = generalInfo.get("initialType");
	}
	
	/*
	 * Method called in HomeSelection.java which is the entry point for the program.
	 * */
	public void initAnimationSimulation(String fileName, DataSetup data) {
		viewManager = new SceneManager(rows, cols, width, height, graphic);
		Group root = viewManager.setupNewScene(main.getStage(), nameOfSimulation, 2.1 * this.getWidth(),
				1.3 * this.getHeight());
		sim = selectSimulation(fileName, this, data);
		viewManager.initStaticGui(root, animation, "English", sim);
		
		if(initialType.equals("random"))
			randomInitialize(sim.getColorMapping());
		
		else if (initialType.equals( "readInitialStatesFromXML"))
			initializeDataFromXML(data);
		
		Color borderColor = Color.BLUEVIOLET; // TODO: need to set elsewhere
		runSimulation(sim, root, this.getWidth(), this.getHeight(), sim.getColorMapping(), true, borderColor, data);
	}

	private Simulation selectSimulation(String name, CA ca, DataSetup data) {
		nameOfSimulation = name;
		Simulation sim;
		if (checkForSimName("wator")) {
			sim = new WaTor(ca);
			graphic.setClickState(1);
		} else if (checkForSimName("schelling")) {
			sim = new Schelling(ca, Float.parseFloat(data.readFileForTag("probability")));
			System.out.println(Float.parseFloat(data.readFileForTag("probability")));
		} else if (checkForSimName("gameoflife")) {
			sim = new GameOfLife(ca);
		} else if (checkForSimName("catchingfire")) {
			sim = new CatchingFire(ca, Float.parseFloat(data.readFileForTag("probability")));
		} else {
			sim = null;
		}
		return sim;
	}
	
	private boolean checkForSimName(String name){
		return  nameOfSimulation.toLowerCase().indexOf(name) != CHECK_FALSE;
	}

	private void runSimulation(Simulation sim, Group root, int width, int height, Map<Integer, Color> stateColors,
			boolean needBorder, Color borderColor, DataSetup data) {
		viewManager.refreshGridView(root, width, height, stateColors, needBorder, borderColor, grid);
		Graph_v2 graph = viewManager.initGraphs(nameOfSimulation);
		KeyFrame frame1 = new KeyFrame(Duration.millis(getMillisecond_delay()), e -> sim.update());
		KeyFrame frame2 = new KeyFrame(Duration.millis(getMillisecond_delay()),
				e -> viewManager.updateView(root, width, height, sim.getColorMapping(), needBorder, borderColor, data, sim, grid,graph));
		KeyFrame[] frames = { frame1, frame2 };
		animation.setCycleCount(Timeline.INDEFINITE);
		for (KeyFrame frame : frames) {
			animation.getKeyFrames().add(frame);
		}
		
		animation.play();
	}
	
	public void addClickListenerToShape(Shape r, int x, int y, int state){
		r.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
            	System.out.println("CLICKED");
                grid[x][y].setState(state);
                r.setFill(sim.getColorMapping().get(state));
                updateClickState(2);
            }
        });
	}
	
	private void updateClickState(int totalStates){
		int currentClickState = graphic.getClickState();
		if (currentClickState>totalStates){
			graphic.setClickState(1);
		}else{
			graphic.setClickState(currentClickState+1);
		}
		System.out.println("Click state: "+graphic.getClickState());
	}


	public String formatStringToFileName(String str) {
		String tempStr = str.replaceAll("\\s+", "");
		return tempStr.substring(0, 1).toLowerCase() + tempStr.substring(1);
	}
}