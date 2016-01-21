package Visitors.PatternVisitors;

import ParseClasses.MethodRep;
import Visitors.ITraverser;
import Visitors.IVisitor;
import Visitors.VisitType;
import Visitors.Visitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by budocf on 1/20/2016.
 */
public class SingletonVisitor {
    private final IVisitor visitor;

    public SingletonVisitor() {
        visitor = new Visitor();
    }

    private void setupVisitor() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if (m.getAccessibility() == Opcodes.ACC_STATIC) {

            }
        });
    }

}
