package Visitors.DefaultVisitors;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by budocf on 1/19/2016.
 */
public class Visitor implements IVisitor {
    Map<LookupKey, IVisitMethod> keyToVisitMethodMap;

    public Visitor() {
        this.keyToVisitMethodMap = new HashMap<>();
    }

    private void doVisit(VisitType vType, ITraverser t) {
        LookupKey key = new LookupKey(vType, t.getClass());
        IVisitMethod m = this.keyToVisitMethodMap.get(key);
        if (m != null)
            m.execute(t);
    }

    @Override
    public void preVisit(ITraverser t) {
        this.doVisit(VisitType.PreVisit, t);
    }

    @Override
    public void visit(ITraverser t) {
        this.doVisit(VisitType.Visit, t);
    }

    @Override
    public void postVisit(ITraverser t) {
        this.doVisit(VisitType.PostVisit, t);
    }

    @Override
    public void addVisit(VisitType visitType, Class<?> clazz, IVisitMethod m) {
        LookupKey key = new LookupKey(visitType, clazz);
        this.keyToVisitMethodMap.put(key, m);
    }

    @Override
    public void removeVisit(VisitType visitType, Class<?> clazz) {
        LookupKey key = new LookupKey(visitType, clazz);
        this.keyToVisitMethodMap.remove(key);
    }
}
