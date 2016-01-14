package DataStorage.SequenceParsing;

import ParseClasses.MethodCall;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by budocf on 1/13/2016.
 */
public interface ISequenceVisitor {
    Set<String> classesVisited = new HashSet<>();

    void visit(MethodCall mc, StringBuilder classes, StringBuilder methods);
}
