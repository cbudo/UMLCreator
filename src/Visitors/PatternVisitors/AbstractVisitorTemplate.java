package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import Visitors.DefaultVisitors.IVisitor;
import Visitors.DefaultVisitors.Visitor;

/**
 * Created by efronbs on 1/27/2016.
 */
public abstract class AbstractVisitorTemplate {
    public final IVisitor visitor;
    public IDataStorage data;

    public AbstractVisitorTemplate(IDataStorage data) {
        this.visitor = new Visitor();
        this.data = data;
    }

    public final void doTheStuff() {
        performSetup();
        performVisits(this.data);
        performAnalysis();
    }

    public abstract void performSetup();

    public abstract void performVisits(IDataStorage data);

    public abstract void performAnalysis();

}
