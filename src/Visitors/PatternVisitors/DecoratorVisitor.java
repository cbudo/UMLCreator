package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractExtendableClassRep;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.Decorators.SingletonClass;

/**
 * Created by budocf on 1/27/2016.
 */
public class DecoratorVisitor extends AbstractVisitorTemplate {
    public DecoratorVisitor(IDataStorage data) {
        super(data);
    }

    @Override
    public void performSetup() {
        //check if classes that classes extend are decorators if so, make class a decorator
        for (AbstractJavaClassRep r : data.getClasses()) {
            try {
                if (data.getNonSpecificJavaClass(((AbstractExtendableClassRep) r).getExtendedClassName().replace('/', '.')) instanceof SingletonClass) {
                    r.setDecorator(true);
                }
            } catch (Exception ignored) {

            }
        }
        for (AbstractJavaClassRep r : data.getAbstractClasses()) {
            try {
                if (data.getNonSpecificJavaClass(((AbstractExtendableClassRep) r).getExtendedClassName().replace('/', '.')) instanceof SingletonClass) {
                    r.setDecorator(true);
                }
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void performVisits(IDataStorage data) {
        // add decorator or component tag to uml box
        for (AbstractJavaClassRep r : data.getClasses()) {
            if (r instanceof SingletonClass) {
                r.addToDisplayName("\\<\\<decorator\\>\\>");
            }
            if (r.isComponent()) {
                r.addToDisplayName("\\<\\<component\\>\\>");
            }
        }
        for (AbstractJavaClassRep r : data.getAbstractClasses()) {
            if (r instanceof SingletonClass) {
                r.addToDisplayName("\\<\\<decorator\\>\\>");
            }
            if (r.isComponent()) {
                r.addToDisplayName("\\<\\<component\\>\\>");
            }
        }
    }

    @Override
    public void performAnalysis() {
        // pushes up glasses, sniffs
        // don't think we've got anything to do here boss
    }
}
