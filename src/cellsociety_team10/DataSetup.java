package cellsociety_team10;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * Reads the input from xml file 
 * 
 * @author Lucy Zhang, Yumin Zhang,Phil Foo
 */


public class DataSetup {
	private static String[] generalInfo = {"title", "initialType","initialState","dimensionx", "dimensiony","width","height","probability"};
	private String dataFilePath;
	
	public String getDataFilePath() {
		return dataFilePath;
	}

	public void setDataFilePath(String dataFilePath) {
		this.dataFilePath = dataFilePath;
	}

	public DataSetup(String dataFilePath){
		this.dataFilePath=dataFilePath;
	}
	
	public DataSetup(){
		
	}
	
	/**
	 * read in Document from xml
	 */
	public Document setUpDocumentToParse() throws ParserConfigurationException, SAXException, IOException{
		File file = new File (dataFilePath);

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		
		return document;
	}
	
	/**
	 * read info by each tag name
	 */
	public String readFileForTag(String tagName){
		try {
			//System.out.println("tagName: " + tagName);
			Document document = setUpDocumentToParse();
			String result = document.getElementsByTagName(tagName).item(0).getTextContent().trim();
			//System.out.println("result: " + result);
			if(result == null){
				result = "0" ; // set up the default value if some tags are missing
			}
			return result;
			
		}
		catch (Exception e) {
			return "tag " + tagName + " is missing";
		}

	}
	
	
	/**
	 * initialize the info for all the simulation into the HashMap
	 */
	public HashMap<String,String> getGeneralInfo(){
		HashMap<String,String> info = new HashMap<String,String>();
		for (int i=0; i<generalInfo.length; i++){
			info.put(generalInfo[i], readFileForTag(generalInfo[i]));
		}
			return info;
	}
	
	
	/**
	 * initialize the states for the grid in CA
	 */
	public void setInitialStates(CA ca, String initialStates){
	    String[] initialStatesString = initialStates.split("\\s+");
	    int[] initialStatesInt = new int[initialStatesString.length];
	    HashMap<String,String> info = getGeneralInfo();
	    int dimensionCheck = Integer.parseInt(info.get("dimensionx")) *Integer.parseInt(info.get("dimensiony"));
	    if(initialStatesString.length != dimensionCheck){
	    	System.out.println("Initial state does not align with dimension");
	    }
	    		
	    for (int i = 0; i < initialStatesString.length; i++)  {
	    	initialStatesInt[i] = Integer.parseInt(initialStatesString[i]);	    			    	
	    }
	    ca.initialize(initialStatesInt);
		
	}

}
