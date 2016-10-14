Name
--
Yumin Zhang, NetID: yz263
Refactoring
--
- what was the design issue(s) in the code you chose to refactor
I choose to modfidy the CA class which contains too much info at the CA class, because CA contains everything factored out of Main class. We have already factored out most of the GUI stuff into Graphics. Now we are thinking factoring out the initSimulation method to the sceneManager class.

- why you think the new version of the code is better
Because CA class used to have 200+ lines with many different types of things. By factoring out some functions, the structure and function of CA class becomes clearer. 

- a link to one or more commits you made that refactored the problem code
bec05ea6defa7837aa4ec5b29c427eb3c65b6bd0  

Lucy Zhang, lz107  
--
- I refactored a large amount of the UI elements in the graph. There were many functions that called functions in their arguments. There were also many specific if statements that were checking for verbose criteria that could be factored out into new functions. For example, in the Graph class, using the CodePro tools, I found a signficant amount of repetition of an ```if``` statement used to check when to add more data to the live updating graph. Some other refactored areas.  

- The code is much more human readable and checking criteria does not require going through every ```if``` statement, but rather simply modifying one method.  

- [Refactored some of Graph functions](https://git.cs.duke.edu/CompSci308_2016Fall/cellsociety_team10/commit/71b62bc6190345e5339d49ed145327a4bef0cdab)