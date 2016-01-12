package NewParseClasses;

import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public class InterfaceRep extends AbstractJavaClassRep
{
    public InterfaceRep(String name, int accessibility)
    {
        super(name, accessibility);
    }

    public InterfaceRep(String name, int accessibility, List<String> interfaces)
    {
        super(name, accessibility, interfaces);
    }
}
