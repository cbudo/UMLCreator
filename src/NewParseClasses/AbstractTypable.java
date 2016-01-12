package NewParseClasses;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractTypable extends AbstractData
{
    private String type;

    public AbstractTypable(String name, int accessibility, String type)
    {
        super(name, accessibility);
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }
}
