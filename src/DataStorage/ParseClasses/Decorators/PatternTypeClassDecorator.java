package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractExtendableClassRep;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import Visitors.DefaultVisitors.IVisitor;

import java.util.List;
import java.util.Map;

/**
 * Created by efronbs on 2/4/2016.
 */
public abstract class PatternTypeClassDecorator extends AbstractExtendableClassRep {
    private AbstractJavaClassRep classToDecorate;

    public PatternTypeClassDecorator(AbstractJavaClassRep c) {
        super();
        this.classToDecorate = c;
    }

    @Override
    public void accept(IVisitor v) {
        classToDecorate.accept(v);
    }

    @Override
    public void addField(String fieldName, AbstractData fieldRep) {
        this.classToDecorate.addField(fieldName, fieldRep);
    }

    public String getExtendedClassName() {
        return ((AbstractExtendableClassRep) this.classToDecorate).getExtendedClassName();
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
        return this.classToDecorate.getMethodsMap();
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
        return classToDecorate.getDisplayName();
    }

    public void addToDisplayName(String textToAdd) {
        classToDecorate.addToDisplayName(textToAdd);
    }

    public int getAccessibility() {
        return this.classToDecorate.getAccessibility();
    }

    public String getTranslatedAccessibility() {
        return this.classToDecorate.getTranslatedAccessibility();
    }

}
