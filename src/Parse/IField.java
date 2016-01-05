package Parse;

/**
 * Created by budocf on 12/17/2015.
 */
public class IField extends IData {
    String type = null;

    public IField(String name, String type, String accessibility) {
        this.name = name;
        this.type = type;
        this.accessibility = accessibility;
    }
}
