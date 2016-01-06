package Parse;

import GraphMaker.Translator;

/**
 * Created by budocf on 12/17/2015.
 */
public class IField extends IData {
    String type = null;

    public IField(String name, String type, int accessibility) {
        this.name = name;
        this.type = type;
        this.accessibility = GetAccess(accessibility);
    }

    public String toString() {
        return Translator.translateAccessibility(accessibility) + " " + this.name + " : " + type + "\\l";
    }
}
