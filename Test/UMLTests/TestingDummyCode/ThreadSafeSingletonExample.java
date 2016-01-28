package UMLTests.TestingDummyCode;

/**
 * Created by efronbs on 1/28/2016.
 */
public class ThreadSafeSingletonExample
{
    private static ThreadSafeSingletonExample e;

    private ThreadSafeSingletonExample()
    {}

    public static ThreadSafeSingletonExample getInstance()
    {
        if (e == null)
        {
            synchronized (ThreadSafeSingletonExample.class)
            {
                if (e == null)
                {
                    e = new ThreadSafeSingletonExample();
                }
            }
        }

        return e;
    }

}
