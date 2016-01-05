package GraphMaker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by efronbs on 1/4/2016.
 */
public class Translator {
    private Map<String, String> accessibilityTranslator = new HashMap<String, String>();

    public Translator() {
        accessibilityTranslator.put("public", "+");
        accessibilityTranslator.put("private", "-");
        accessibilityTranslator.put("protected", "-");
        accessibilityTranslator.put("default", "+");
    }

    public static String translateAccessibility(String accessibility) {
        Translator t = new Translator();
        return t.accessibilityTranslator.get(accessibility);
    }
}
