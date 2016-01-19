package ParseClasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractJavaClassRep extends AbstractData
{
    private List<String> implementsNames;
    private Map<String, AbstractData> methodsMap;
    private Map<String, AbstractData> fieldsMap;

    public AbstractJavaClassRep(String name, int accessibility)
    {
        this(name, accessibility, null);
    }

    public AbstractJavaClassRep(String name, int accessibility, List<String> implementsNames)
    {
        super(name, accessibility);
        this.implementsNames = implementsNames;
        this.methodsMap = new HashMap<String, AbstractData>();
        this.fieldsMap = new HashMap<String,AbstractData>();
        //addImplementsToStorage();
    }

//    protected void addImplementsToStorage() {
//        for (String s : implementsNames) {
//            ParsedDataStorage.getInstance().addRelation(new Implements(s, this.getName()));
//        }
//    }


    public void addMethod(String methodName, AbstractData methodStructure)
    {
        this.methodsMap.put(methodName, methodStructure);
    }

    public AbstractData getMethod(String methodName)
    {
        if (this.methodsMap.containsKey(methodName))
        {
            return this.methodsMap.get(methodName);
        }
        return null;
    }

    public Map<String, AbstractData> getMethodsMap()
    {
        return this.methodsMap;
    }

    public void addField(String fieldName, AbstractData fieldRep)
    {
        this.fieldsMap.put(fieldName, fieldRep);
    }

    public AbstractData getField(String fieldName)
    {
        if (this.fieldsMap.containsKey(fieldName))
        {
            return this.fieldsMap.get(fieldName);
        }
        return null;
    }

    public Map<String, AbstractData> getFieldsMap()
    {
        return this.fieldsMap;
    }

    public void addImplements(String interfaceName)
    {
        this.implementsNames.add(interfaceName);
    }

    public List<String> getImplementsList()
    {
        return this.implementsNames;
    }

}
