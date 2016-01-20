package Visitor;

/**
 * Created by budocf on 1/19/2016.
 */
public interface IVisitor {
    void preVisit(ITraverser t);

    void visit(ITraverser t);

    void postVisit(ITraverser t);

    void addVisit(VisitType visitType, Class<?> clazz, IVisitMethod m);

    void removeVisit(VisitType visitType, Class<?> clazz);
}
