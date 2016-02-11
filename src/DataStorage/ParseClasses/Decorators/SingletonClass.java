package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;

/**
 * Created by budocf on 2/2/2016.
 */
public class SingletonClass extends PatternTypeClassDecorator {
    private boolean publicStaticGetInstance = false;
    private boolean privateSingletonInit = false;
    private boolean privateSingletonField = false;

    public SingletonClass(AbstractJavaClassRep rep) {
        super(rep);
    }

    public void setPublicStaticGetInstance(boolean inst) {
        this.publicStaticGetInstance = inst;
    }

    public void setPrivateSingletonInit(boolean inst) {
        this.privateSingletonInit = inst;
    }

    public void setPrivateSingletonField(boolean inst) {
        this.privateSingletonField = inst;
    }

    public boolean isSingleton() {
        return privateSingletonField && privateSingletonInit && publicStaticGetInstance;
    }
}
