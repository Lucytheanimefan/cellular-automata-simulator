package cellsociety_team10;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataOutput {
	private Cell[][] grid;
	
	/**
	 * When the user click save button
	 * the program constructs a DataOutput
	 * it will save the current states in an xml file
	 * 
	 * @author yuminzhang 
	 */
	
	public DataOutput(CA ca, String outputFileName) {
		grid = ca.getGrid();
		writeXML(outputFileName);
	}

	/**
	 * turn the grid into a stirng in order to write to xml
	 * @param grid is the current states of the grid
	 * @return a String version of the grid, separate by a space between each cell
	 */
	public String gridToString(Cell[][] grid){
		String result = "";
		for(Cell[] r : grid){
			for(Cell c: r){
				result = result + c.getState() + " ";
			}
		}
		return result.trim();		
	}
	

	/**
	 * create a new xml in data folder 
	 * write the current states of the grid into xml file created
	 */
	public void writeXML(String fileName){
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Simulation");
			doc.appendChild(rootElement);

			Element currentState = doc.createElement("currentState");
			currentState.appendChild(doc.createTextNode(gridToString(grid)));
			rootElement.appendChild(currentState);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("data/"+fileName));

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe){
			tfe.printStackTrace();
		}

	}


}
