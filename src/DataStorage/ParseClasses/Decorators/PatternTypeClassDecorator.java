package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.ClassTypes.ClassRep;
import Visitors.DefaultVisitors.IVisitor;

import java.util.List;
import java.util.Map;

/**
 * Created by efronbs on 2/4/2016.
 */
public class PatternTypeClassDecorator extends AbstractJavaClassRep {
    private ClassRep classToDecorate;
    private String patternName;

    public PatternTypeClassDecorator(ClassRep c, String patternName) {
        super(c.getName(), c.getAccessibility());
        this.classToDecorate = c;
        this.patternName = patternName;
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }

    public void setPublicStaticGetInstance(boolean inst) {
        this.classToDecorate.setPublicStaticGetInstance(inst);
    }

    public void setPrivateSingletonInit(boolean inst) {
        this.classToDecorate.setPrivateSingletonInit(inst);
    }

    public void setPrivateSingletonField(boolean inst) {
        this.classToDecorate.setPrivateSingletonField(inst);
    }

    public boolean isSingleton() {
        return this.classToDecorate.isSingleton();
    }

    @Override
    public void addField(String fieldName, AbstractData fieldRep) {
        this.classToDecorate.addField(fieldName, fieldRep);
    }

    public String getExtendedClassName() {
        return this.classToDecorate.getExtendedClassName();
    }

    public String getFillColor() {
        return this.classToDecorate.getFillColor();
    }

    public void setFillColor(String color) {
        this.classToDecorate.setFillColor(color);
    }

    public void addMethod(String methodName, AbstractData methodStructure) {
        this.classToDecorate.addMethod(methodName, methodStructure);
    }

    public AbstractData getMethod(String methodName) {
        return this.classToDecorate.getMethod(methodName);
    }

    public Map<String, AbstractData> getMethodsMap() {
        return this.methodsMap;
    }

    public AbstractData getField(String fieldName) {
        return this.classToDecorate.getField(fieldName);
    }

    public Map<String, AbstractData> getFieldsMap() {
        return this.classToDecorate.getFieldsMap();
    }

    public void addImplements(String interfaceName) {
        this.classToDecorate.addImplements(interfaceName);
    }

    public List<String> getImplementsList() {
        return this.classToDecorate.getImplementsList();
    }

    public void addProfileTag(String profile) {
        this.classToDecorate.addProfileTag(profile);
    }

    public List<String> getProfileTags() {
        return this.classToDecorate.getProfileTags();
    }

    public boolean isDecorator() {
        return true;
    }

    public void setDecorator(boolean decorator) {
        this.classToDecorate.setDecorator(decorator);
    }

    public String getColor() {
        return this.classToDecorate.getColor();
    }

    public void setColor(String c) {
        this.classToDecorate.setColor(c);
    }

    public String getName() {
        return this.classToDecorate.getName();
    }

    public String getInnermostName() {
        return this.classToDecorate.getInnermostName();
    }

    public String getDisplayName() {
        return classToDecorate.getDisplayName() + "\n" + this.patternName;
    }

    public void addToDisplayName(String textToAdd) {
        return;
    }

    public int getAccessibility() {
        return this.classToDecorate.getAccessibility();
    }

    public String getTranslatedAccessibility() {
        return "";
    }

    public boolean isComponent() {
        return this.classToDecorate.isComponent();
    }

    public void setComponent(boolean component) {
        this.classToDecorate.setComponent(component);
    }
}
