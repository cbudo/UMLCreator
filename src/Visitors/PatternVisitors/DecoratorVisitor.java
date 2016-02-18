package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractExtendableClassRep;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.Decorators.ComponentDecorator;
import DataStorage.ParseClasses.Decorators.DecoratorDecorator;

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
        for (AbstractJavaClassRep r : data.getAbstractClasses()) {
            try {
                if (data.getNonSpecificJavaClass(((AbstractExtendableClassRep) r).getExtendedClassName().replace('/', '.')) instanceof DecoratorDecorator) {
                    String currentName = r.getName();
                    ParsedDataStorage.getInstance().setDecorator(currentName);
                }
            } catch (Exception ignored) {

            }
        }
        for (AbstractJavaClassRep r : data.getClasses()) {
            try {
                if (data.getNonSpecificJavaClass(((AbstractExtendableClassRep) r).getExtendedClassName().replace('/', '.')) instanceof DecoratorDecorator) {
                    String currentName = r.getName();
                    ParsedDataStorage.getInstance().setDecorator(currentName);
                }
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void performVisits(IDataStorage data) {
        // add decorator or component tag to uml box
        for (AbstractJavaClassRep r : data.getClasses()) {
            if (r instanceof DecoratorDecorator) {
                r.addToDisplayName("\\<\\<decorator\\>\\>");
            }
            if (r instanceof ComponentDecorator) {
                r.addToDisplayName("\\<\\<component\\>\\>");
            }
        }
        for (AbstractJavaClassRep r : data.getAbstractClasses()) {
            if (r instanceof DecoratorDecorator) {
                r.addToDisplayName("\\<\\<decorator\\>\\>");
            }
            if (r instanceof ComponentDecorator) {
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
