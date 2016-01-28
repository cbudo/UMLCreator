package ParseClasses;

import DataStorage.ParsedDataStorage;

import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractExtendableClassRep extends AbstractJavaClassRep {
    private String extendedClassName;

    public AbstractExtendableClassRep(String name, int accessibility) {
        this(name, accessibility, null, null);
    }

    public AbstractExtendableClassRep(String name, int accessibility, List<String> implementsNames) {
        this(name, accessibility, implementsNames, null);
    }

    public AbstractExtendableClassRep(String name, int accessibility, String extendedClassName) {
        this(name, accessibility, null, extendedClassName);
    }

    public AbstractExtendableClassRep(String name, int accessibility, List<String> implementsNames, String extendedClassName) {
        super(name, accessibility, implementsNames);
        this.extendedClassName = extendedClassName;
        AbstractJavaClassRep extending = ParsedDataStorage.getInstance().getClass(extendedClassName);
        if (extending != null) {
            if (extending.isDecorator()) {
                this.isDecorator = true;
            }
        }
    }

    @Override
    public void addField(String fieldName, AbstractData fieldRep) {
        String type = ((FieldRep) fieldRep).getType();
        if (this.extendedClassName.equals(type) || this.getImplementsList().contains(type) || this.getInnermostName().equals(type)) {
            //we've got a winner
            this.isDecorator = true;
            //set type to component
            try {
                ParsedDataStorage.getInstance().getClass(((FieldRep) fieldRep).getFullType()).setComponent(true);
            } catch (Exception e) {

            }
        }
        super.addField(fieldName, fieldRep);
    }

    public String getExtendedClassName() {
        return this.extendedClassName;
    }

//    protected void addExtendsRelationsToStorage() {
//        if (extendedClassName != null) {
//            ParsedDataStorage.getInstance().addRelation(new Extends(extendedClassName, getName()));
//        }
//    }
}
