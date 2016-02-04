package DataStorage.ParseClasses.ClassTypes;

import Visitors.DefaultVisitors.ITraverser;
import org.objectweb.asm.Opcodes;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractData implements ITraverser
{
    private String name, displayName;
    private int accessibility;
    private boolean isComponent;

    public AbstractData(String name, int accessibility)
    {
        this.name = name;
        this.displayName = getInnermostName() + "\\n";

        this.accessibility = accessibility;

        isComponent = false;
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
        this.displayName += textToAdd + "\\n";
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

    public boolean isComponent() {
        return isComponent;
    }

    public void setComponent(boolean component) {
        isComponent = component;
    }


}