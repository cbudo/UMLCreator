package ParseClasses;

import DataStorage.ParsedDataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractJavaClassRep extends AbstractData {
    protected Map<String, AbstractData> methodsMap;
    protected Map<String, AbstractData> fieldsMap;
    protected boolean isDecorator;
    private List<String> profileTags;
    private List<String> implementsNames;
    private String color;
    private String fillColor;

    public AbstractJavaClassRep(String name, int accessibility) {
        this(name, accessibility, null);
    }

    public AbstractJavaClassRep(String name, int accessibility, List<String> implementsNames) {
        super(name, accessibility);
        this.implementsNames = implementsNames;
        this.methodsMap = new HashMap<String, AbstractData>();
        this.fieldsMap = new HashMap<String, AbstractData>();
        this.profileTags = new ArrayList<String>();
        this.isDecorator = false;
        this.color = "black";
        this.fillColor = "white";
    }

//    protected void addImplementsToStorage() {
//        for (String s : implementsNames) {
//            ParsedDataStorage.getInstance().addRelation(new Implements(s, this.getName()));
//        }
//    }

    public String getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(String color) {
        this.fillColor = color;
    }

    public void addMethod(String methodName, AbstractData methodStructure) {
        this.methodsMap.put(methodName, methodStructure);
    }

    public AbstractData getMethod(String methodName) {
        if (this.methodsMap.containsKey(methodName)) {
            return this.methodsMap.get(methodName);
        }
        return null;
    }

    public Map<String, AbstractData> getMethodsMap() {
        return this.methodsMap;
    }

    public void addField(String fieldName, AbstractData fieldRep) {
        String type = ((FieldRep) fieldRep).getType();
        if (getImplementsList().contains(type) || this.getInnermostName().equals(type)) {
            setDecorator(true);
            //make field a component
            try {
                ParsedDataStorage.getInstance().getClass(((FieldRep) fieldRep).getFullType()).setComponent(true);
            } catch (Exception e) {

            }
        }
        this.fieldsMap.put(fieldName, fieldRep);
    }

    public AbstractData getField(String fieldName) {
        if (this.fieldsMap.containsKey(fieldName)) {
            return this.fieldsMap.get(fieldName);
        }
        return null;
    }

    public Map<String, AbstractData> getFieldsMap() {
        return this.fieldsMap;
    }

    public void addImplements(String interfaceName) {
        this.implementsNames.add(interfaceName);
    }

    public List<String> getImplementsList() {
        return this.implementsNames;
    }

    public void addProfileTag(String profile) {
        this.profileTags.add(profile);
    }

    public List<String> getProfileTags() {
        return this.profileTags;
    }

    public boolean isDecorator() {
        return isDecorator;
    }

    public void setDecorator(boolean decorator) {
        isDecorator = decorator;
        setFillColor("green");
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String c) {
        this.color = c;
    }
}
