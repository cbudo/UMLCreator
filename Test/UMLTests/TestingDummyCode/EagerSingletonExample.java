package UMLTests.TestingDummyCode;

/**
 * Created by efronbs on 1/28/2016.
 */
public class EagerSingletonExample
{
    private static EagerSingletonExample e = new EagerSingletonExample();

    private EagerSingletonExample()
    {}

    public static EagerSingletonExample getInstance()
    {
        return e;
    }
}

