package DataStorage;

import DataStorage.SequenceParsing.SequenceGenerator;
import DataStorage.UMLClassParsing.GraphGenerator;

/**
 * Created by budocf on 1/13/2016.
 */
public class GeneratorFactory {
    public GeneratorFactory() {

    }

    public IGenerator getGenerator(String generatorType) {
        switch (generatorType) {
            case "UML":
                return new GraphGenerator();
            case "Sequence":
                return new SequenceGenerator();
            default:
                return null;
        }
    }
}
