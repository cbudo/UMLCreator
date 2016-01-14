package DataStorage;

import DataStorage.SequenceParsing.SequenceVisitor;
import ParseClasses.MethodCall;

/**
 * Created by budocf on 1/13/2016.
 */
public class SequenceGenerator implements IGenerator {
    @Override
    public String Generate() {
        IDataStorage data = ParsedDataStorage.getInstance();
        SequenceVisitor methodVisitor = new SequenceVisitor();
        StringBuilder classes = new StringBuilder();
        StringBuilder methods = new StringBuilder();
        for (MethodCall mc : data.getMethods()) {
            mc.acceptSequenceClass(methodVisitor, classes, methods);
        }
        return classes.toString() + "\n" + methods.toString();
    }
}
