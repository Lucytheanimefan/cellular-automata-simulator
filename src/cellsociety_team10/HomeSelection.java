package cellsociety_team10;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.HashMap;

/**
 * This class create the file chooser
 * @author Lucy Zhang
 *
 */
public class HomeSelection {
	private final File INITIAL_DIRECTORY = new File("data");
	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	private String fileName;
	private DataSetup data;

	public HomeSelection() {

	}

	/**
	 * Creates the file directory to choose an xml simulation file to run
	 */
	public void initHomeScreen() {
		createFileDirectory();
	}

	/**
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	private void createFileDirectory() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(INITIAL_DIRECTORY);
		chooser.setTitle("Choose File");
		File temp = chooser.showOpenDialog(new Stage());
		if (temp != null) {
			System.out.println("Here is temp: ");
			System.out.println(temp.toString());
			fileName = temp.toString();
			data = new DataSetup(fileName);
			CA sim = createCAFromXML(fileName);
			sim.initAnimationSimulation(fileName, data);
		}
	}

	private CA createCAFromXML(String fileName) {
		DataSetup data = new DataSetup(fileName);
		HashMap<String, String> generalInfo = data.getGeneralInfo();
		CA newCA = new CA(Integer.parseInt(generalInfo.get("dimensionx")),
				Integer.parseInt(generalInfo.get("dimensiony")), Integer.parseInt(generalInfo.get("width")),
				Integer.parseInt(generalInfo.get("height")), generalInfo.get("initialType"));
		return newCA;
	}

}
