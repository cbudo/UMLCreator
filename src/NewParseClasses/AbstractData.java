package NewParseClasses;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractData
{
    private String name;
    private int accessibility;
    private Map<String, String> accessibilityTranslator = new HashMap<String, String>();

    public AbstractData(String name, int accessibility)
    {
        this.name = name;
        this.accessibility = accessibility;

        this.accessibilityTranslator.put("public", "+");
        this.accessibilityTranslator.put("private", "-");
        this.accessibilityTranslator.put("protected", "-");
        this.accessibilityTranslator.put("default", "+");
    }

    public String getName()
    {
        return this.name;
    }

    public int getAccessibility()
    {
        return this.accessibility;
    }

    public String getTranslatedAccessibility()
    {
        return this.accessibilityTranslator.get(this.accessibility);
    }

}
