package cellsociety_team10;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * triggers the entire program
 * 
 * @author Lucy Zhang, Yumin Zhang,Phil Foo
 */

public class Main extends Application {
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 10000 / FRAMES_PER_SECOND;
	private static Stage stage;
	static DataSetup data; 
	private HomeSelection homeSelection;
	
	
	public Stage getStage() {
		return stage;
	}


	public void setStage(Stage stage) {
		Main.stage = stage;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setStage(primaryStage);
		homeSelection = new HomeSelection();
		homeSelection.initHomeScreen();
	}

	public static void main(String[] args) {
		launch(args);
		
	}
}
