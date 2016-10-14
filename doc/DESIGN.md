# Cell Society Design

Design Questions
--
- provides the high-level design goals of your project
- explains, in detail, how to add new features to your project
justifies major design choices, including trade-offs (i.e., pros and cons), made in your project
- states any assumptions or decisions made to simplify or resolve ambiguities in your the project's functionality

Introduction
--
We are developing software to create 2D cellular automata simulations. The software is intended to accept different parameters and rules in order to build the animation. These rules include cell behavior relative to the cells around it, color choices, grid size, speed of animation, and more. 

Overview
--  
To flexibly handle the cellular automata animation, the software was divided into several core functionalities: a class to initialize the game, a class representing each cell in the grid, a data setup class that retrieves and organizes the data, a class that responsible for general simulation properties that all cellular automata share, and specific classes tailored to each different cellular automata type that the program must be capable of simulating. There will also be visualization tailored classes to manage the extensions that modify the GUI. 
![](img/uml.png) 
### Classes:
* #### CA: 
	The CA class will be responsible for delegating a lot of the initial responsibilities upon launch of the program, including initiating the DataSetup class to parse through the setup.txt file, retrieving the pertinent information from the Setup class, and then deciding on the correct Simulation to run.

* #### Cell
	The responsibility of the Cell class is to provide an object which can be stored in the n x n grid which the simulation runs on. A "Cell" will fill each grid, and each Cell will have properties such as its coordinates, state (e.g. red/blue or fish/shark), and potentially its neighbors. The Cell class will store information which dictates its behavior relative to its neighbors. For example, the Cell class allows the program to know a fish is present in a particular cell, while a shark is present in the cell next to it.
    
* #### DataSetup

	The DataSetup  class is responsible for parsing through the setup.txt file, and returning the appropriate information back to the CA class. It also will be determining if information in the file is wrong: e.g. missing information, a simulation that doesn't exist, etc. Additionally, it can also be responsible for handling the XML output of the program after a simulation is done running.

	- There is also a DataOutput class that writes all of the data to an xml file and saves it in the directory. 
    
* #### Simulation (abstract class)
	This will be an abstract class which each simulation will extend. There will most likely be a class for each type of simulation, but they will all be subclasses of this Simulation class. This is because all simulations have certain behaviors in common - an update loop, random grid initialization at the beginning, grid reset, and data such as animation length and animation speed. Methods such as the updateLoop will be abstract, and each of the children simulations will have to implement this behavior since it is unique to each simulation. A certain type of Simulation will be called from the CA class to be rendered onto the screen, depending on the result of the DataSetup class.
    
* #### Simulation-specific Classes
	This mostly includes classes which are specific to certain simulations. For example, the WatorWorld simulation will probably have Fish and Shark classes, while the Segregation simulation might have Color classes. These specific classes will be important, particularly in the updateLoop of each simulation, since their behavior will be dependent on the requirements of the simulation. These are essentially rules classes will handle the different types of rules in CA

* #### Visualization Classes
	- SceneManager: A class that manages the whole scene that holds the GUI
	- ShapeCreator: Create the different grid shapes
	- Graph: Creates the live updating graph
	- HomeSelection: Brings up the file directory to choose an xml file to use
	
User Interface
--
The user will mainly interact with our CA program by modifying a setup .txt file, which can contain information such as the type of simulation to run, as well as any pertinent information relating to that simulation. For example, if running the model of segregation, the user might input the necessary level of similarity, initial red/blue starting ratios, etc. among other things. The program can also report erroneous error situations to the user through the console if the user inputs information in the setup.txt file incorrectly. Finally, the rest of the program will mostly just be a visualization of the CA simulation, similar to the grid below:

![] (img/example_grid.png)
Design Details
--
* #### CA
	The CA class would have the following methods:  
	- ```init``` which initializes the entire cellular automata animation by calling ```public``` methods from the other classes to get and parse the data and send it to the simulation classes.   
	- ```createBoard``` which accepts parameters for the size to make the grid.  

* #### Cell
	The Cell class is relatively simple and will simply contain the following instance variables: x coordinate (```int x```), y coordinate (```int y```), and state (```short state```). There will be two Cell constructors, one that sets all of the instance variables to zero and accepts no arguments and the other that sets the three instance variables to values passed into the constructor.

* #### DataSetup  
	The DataSetup class would have a loadTextFile method that reads the specified text file and return its lines. This would use ```DataInputStream```, ```InputStreamReader```, and ```BufferedReader```. 

* #### Simulation abstract class  
	This class would contain the following methods:  
		- ```initGrid``` which would create the grid and initial positions of cells as well as store those cells in a 2D array.  
		-  ```update``` which is the abstract method that handles how a specific cellular automata's rules might update the cells  
		- ```checkNeighbor``` checks the surrounding neighbors of a certain cell. As all cellular automata need to check their so-called neighbors in order to determine their next course of action or state, this method is in the abstract class. By default, it would check the 4 cells directly adjacent to the cell in question. However, parameters will be abled to be passed in in order to tailor the function to fulfill other rules that may not follow the conventional up-down-right-left neighbor check.  
		- ```drawGrid``` This abstract method would redraw the grid according to the updates based on the rules applied to each cell. It would be called within the main game loop performed by ```update```


* #### Simulation-specific cellular automata
	* Schelling:  
		Schelling-specific data to be included in the file: the original number and positions of X and Y agents,the threshold value, which is the lowest percent of similar neighbors that an agent will be satisified with.  
	* Wa-Tor:  
		Wa-Tor specific data to be included in the file: the number and positions of sharks and fish.   
	* Catching fire:  
		Catching-fire specific data to be included in the file: The original state of each cell (empty, tree, or burning). The probability of a tree in a cell catching fire if a tree in a neighboring cell is on fire.
	* Foraging Ants
	* Slime mold
	* Sugar scape


Design Considerations
--
One design consideration that required significant debate was the inclusion of a Neighborhood class. Originally it was concluded that such a class would be unnecessary and excessive, as storing information on all of the cell's neighboring cells could be side stepped by simply checking the the surrounding cells in some kind of game loop. However, after thinking about how to make the software as flexible as possible, we realized that a cell's surrounding cells might not simply be the ones immediately above, below, and to the sides. Rather, the neighboring cells could be in a different shape depending on the criteria. This forced us to reconsider the idea of a neighborhood class. Ultimately, we decided this was still unnecessary an counterproductive as it would be difficult to continuosly update the stored data on neighboring cells. Rather we decided to have an overseer of everything to be aware of a cell's neighbors.  

Another design consideration was to create the abstract simulation class. We knew that all cellular automata share certain functionalities and in order to be prepared for the random curve ball Professor Duvall decided to throw at us, we opted to create the abstract class that we could easily build on for any cellular automata's rules.  

Team Responsibilities
--
All: Construction the Cell, CA, and simulation abstract class as well as discuss overall architecture for specific cellular automata implementations.  
Phil: Schelling simulation class  
Lucy: Wa-Tor simulation class  
Yumin: Catching fire simulation class  