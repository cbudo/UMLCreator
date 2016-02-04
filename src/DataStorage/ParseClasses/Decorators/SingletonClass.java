package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.ClassTypes.AbstractExtendableClassRep;
import DataStorage.ParseClasses.ClassTypes.ClassRep;
import Visitors.DefaultVisitors.IVisitor;

import java.util.List;

/**
 * Created by budocf on 2/2/2016.
 */
public class SingletonClass extends AbstractExtendableClassRep {
    ClassRep decorated;
    public SingletonClass(ClassRep rep) {
        super(rep.getName(),rep.getAccessibility());
        this.decorated = rep;
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }
}
