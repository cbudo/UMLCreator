package UMLTests.TestingDummyCode.AdapterTests;

/**
 * Created by efronbs on 2/4/2016.
 */
public class FakeAdaptor_NoUsesRelation implements SampleTarget {
    private SimpleAdaptee adaptee;

    public FakeAdaptor_NoUsesRelation(SimpleAdaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void m1() {
        return;
    }

    @Override
    public void m2() {
        return;
    }

    @Override
    public void m3() {
        this.adaptee.doStuff3();
    }

}