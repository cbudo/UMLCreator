package UMLTests.TestingDummyCode.AdapterTests;

/**
 * Created by efronbs on 2/4/2016.
 */
public class DoubleAdaptor implements SampleTarget {
    private DoubleAdaptee adaptee;

    public DoubleAdaptor(DoubleAdaptee ad) {
        this.adaptee = ad;
    }

    @Override
    public void m1() {
        this.adaptee.doM1();
    }

    @Override
    public void m2() {
        this.adaptee.doM2();
    }

    @Override
    public void m3() {
        this.adaptee.doM3();
    }
}
