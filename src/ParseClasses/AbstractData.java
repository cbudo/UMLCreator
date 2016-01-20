package ParseClasses;

import java.security.KeyRep;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractData
{
    private String name;
    private int accessibility;
    private Map<Integer, String> accessibilityTranslator = new HashMap<Integer, String>();

    public AbstractData(String name, int accessibility)
    {
        this.name = name;
        this.accessibility = accessibility;

        this.accessibilityTranslator.put(KeyRep.Type.PUBLIC.ordinal(), "+");
        this.accessibilityTranslator.put(KeyRep.Type.PRIVATE.ordinal(), "-");
        this.accessibilityTranslator.put(KeyRep.Type.SECRET.ordinal(), "-");
        //this.accessibilityTranslator.put("default", "+");
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
