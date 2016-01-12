package NewParseClasses;

import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public class MethodRep extends AbstractTypable
{
    private List<String> usesRelations;

    public MethodRep(String name, int accessibility, String type)
    {
        super(name, accessibility, type);
    }

    public void addUsesRelation(String className)
    {
        this.usesRelations.add(className);
    }

    public List<String> getUsesRelations()
    {
        return this.usesRelations;
    }

}
