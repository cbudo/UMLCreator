package DataStorage.ParseClasses.ClassTypes;

import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.Internals.FieldRep;

import java.util.*;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractJavaClassRep extends AbstractData {
    protected Map<String, AbstractData> methodsMap;
    protected Map<String, AbstractData> fieldsMap;
    private List<String> profileTags;
    private List<String> implementsNames;
    private String color;
    private String fillColor;
    private String patternGroup;

    public AbstractJavaClassRep(String name, int accessibility) {
        this(name, accessibility, null);
    }

    public AbstractJavaClassRep(String name, int accessibility, List<String> implementsNames) {
        super(name, accessibility);
        this.implementsNames = implementsNames;
        this.methodsMap = new HashMap<>();
        this.fieldsMap = new HashMap<>();
        this.profileTags = new ArrayList<>();
        this.color = "black";
        this.fillColor = "white";
        this.patternGroup = "standard";
    }

    public AbstractJavaClassRep() {
        super();
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

    public String getPatternGroup() {
        return patternGroup;
    }

    public void setPatternGroup(String pg) {
        patternGroup = pg;
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
            //make field a component
            try {
                String currentName = getName();
                ParsedDataStorage.getInstance().setDecorator(currentName);
                ParsedDataStorage.getInstance().setComponent(((FieldRep) fieldRep).getFullType());
            } catch (Exception ignored) {

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
        if (this.implementsNames == null) return Collections.emptyList();
        return this.implementsNames;
    }

    public void addProfileTag(String profile) {
        this.profileTags.add(profile);
    }

    public List<String> getProfileTags() {
        return this.profileTags;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String c) {
        this.color = c;
    }
}
