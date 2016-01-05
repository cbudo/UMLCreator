# UMLCreator
Project for CSSE 374

##Design
[Our UML Design!](./Milestone_1_UML.png)
We are using the visitor and decorator patterns to parse compiled Java code and create UML diagrams.  In the current state we are able to parse Interfaces and classes and methods and fields inside each.
This project uses ASM for bytecode parsing and dot by graphviz for creating the UML diagrams.

##How to Use
In order to use this, you will need to call our application from the command line and provide the class names that you would like shown on the UML diagram.  It will create a mapping of the structure of the code which can be passed into graphviz in order to create the UML document.

##Work
| Chris  |      Benjamin  |
|:----------|:-------------|
| Implement UML v1 |  Create UML v1 | 
| Parsing classes using ASM | Creating graph with Graphviz |
| test cases | toStrings |

