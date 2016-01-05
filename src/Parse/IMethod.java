package Parse;

import GraphMaker.Translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by budocf on 12/17/2015.
 */
public class IMethod extends IData {
    String returnType = null;
    List<String> parameters;

    public IMethod() {
        this.parameters = new ArrayList<>();
    }

    public IMethod(String name, String returnType, String accessibility, String[] paramTypes) {
        this.name = name;
        this.returnType = returnType;
        this.accessibility = accessibility;
        this.parameters = new ArrayList<>();
        Collections.addAll(parameters, paramTypes);
    }

    public String toString() {
        return Translator.translateAccessibility(accessibility) + " " + this.name + " : " + this.returnType + "\\l";
    }

}
