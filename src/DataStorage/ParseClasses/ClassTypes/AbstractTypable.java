package DataStorage.ParseClasses.ClassTypes;

import DataStorage.ParsedDataStorage;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractTypable extends AbstractData
{
    private String type;
    private String fullType;

    public AbstractTypable(String name, int accessibility, String fullType)
    {
        super(name, accessibility);
        this.fullType = fullType;
        this.type = ParsedDataStorage.getInstance().cleanName(fullType.replace("/", "."));
    }

    public String getType()
    {
        return this.type;
    }

    public String getFullType() {
        return this.fullType;
    }
}
