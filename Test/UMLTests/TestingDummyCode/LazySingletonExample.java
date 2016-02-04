package UMLTests.TestingDummyCode;

public class LazySingletonExample
{
    private static LazySingletonExample e;

    private LazySingletonExample()
    {}

    public static LazySingletonExample getInstance()
    {
        if (e == null)
        {
            e = new LazySingletonExample();
        }

        return e;
    }
}
