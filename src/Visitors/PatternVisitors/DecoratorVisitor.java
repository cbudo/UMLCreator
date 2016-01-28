package Visitors.PatternVisitors;

import DataStorage.IDataStorage;

/**
 * Created by budocf on 1/27/2016.
 */
public class DecoratorVisitor extends AbstractVisitorTemplate {
    public DecoratorVisitor(IDataStorage data) {
        super(data);
    }

    @Override
    public void performSetup() {

    }

    @Override
    public void performVisits(IDataStorage data) {

    }

    @Override
    public void performAnalysis() {

    }
}
