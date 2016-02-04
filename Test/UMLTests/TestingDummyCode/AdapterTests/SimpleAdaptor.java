package UMLTests.TestingDummyCode.AdapterTests;

/**
 * Created by efronbs on 2/4/2016.
 */
public class SimpleAdaptor implements SampleTarget {
    private SimpleAdaptee adaptee;

    public SimpleAdaptor(SimpleAdaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void m1() {
        this.adaptee.doStuff1();
    }

    @Override
    public void m2() {
        this.adaptee.doStuff2();
    }

    @Override
    public void m3() {
        this.adaptee.doStuff3();
    }

}
