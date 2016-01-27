package ParseClasses;

import Visitors.ITraverser;
import org.objectweb.asm.Opcodes;

import java.security.KeyRep;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractData implements ITraverser
{
    private String name, displayName;
    private int accessibility;
    private Map<Integer, String> accessibilityTranslator = new HashMap<Integer, String>();

    public AbstractData(String name, int accessibility)
    {
        this.name = name;
        this.displayName = getInnermostName();

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

    public String getInnermostName() {
        return this.name.substring(this.name.lastIndexOf("/") + 1);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void addToDisplayName(String textToAdd) {
        this.displayName += textToAdd;
    }

    public int getAccessibility()
    {
        return this.accessibility;
    }

    public String getTranslatedAccessibility()
    {
        if ((this.getAccessibility() & Opcodes.ACC_PUBLIC) != 0) {
            return "+";
        } else if ((this.getAccessibility() & Opcodes.ACC_PRIVATE) != 0) {
            return "-";
        } else {
            return "#";
        }
    }

}
