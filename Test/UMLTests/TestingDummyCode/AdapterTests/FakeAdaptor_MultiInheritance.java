package UMLTests.TestingDummyCode.AdapterTests;

/**
 * Created by efronbs on 2/4/2016.
 */
class FakeAdaptor_MultiInheritance extends SampleExtends implements SampleTarget {

    private SimpleAdaptor ad;

    public FakeAdaptor_MultiInheritance(SimpleAdaptor ad) {
        super();
        this.ad = ad;
    }

    @Override
    public void m1() {
        this.ad.m1();
    }

    @Override
    public void m2() {
        this.ad.m2();
    }

    @Override
    public void m3() {
        this.ad.m3();
    }

    @Override
    void doNothing() {
        this.ad.m1();
    }
}
