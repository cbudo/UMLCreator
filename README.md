# UMLCreator
Project for CSSE 374

##Design
[Our UML Design!](./Milestone_1_UML.png)
We are using the visitor and decorator patterns to parse compiled Java code and create UML diagrams.  In the current state we are able to parse Interfaces and classes and methods and fields inside each.
This project uses ASM for bytecode parsing and dot by graphviz for creating the UML diagrams.

[Milestone 2 updates](./Milestone_2_UML.png)
Our design has not changes much since Milestone two, as we were already storing the data needed for this milestone, but not using it yet. However, we have internally reorganized much of our code to make it more scalable. Much of this reorganization is focused on our data storage classes to better share code that does not change between classes. 

##How to Use
In order to use this, you will need to call our application from the command line and provide the class names that you would like shown on the UML diagram.  It will create a mapping of the structure of the code which can be passed into graphviz in order to create the UML document.

##Work
| Chris  |      Benjamin  |
|:----------|:-------------|
| Implement UML v1 |  Create UML v1 | 
| Parsing classes using ASM | Creating graph with Graphviz |
| test cases | toStrings |

|Milestone 2 Work|
|bug fixing and expanding ASM parsing | Bug fixing existing printing methods, implementing uses and association printing|
|refactoring data storage structure |refactoring data storage structure |

