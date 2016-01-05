package Parse;

import java.util.Collection;

/**
 * Created by budocf on 1/4/2016.
 */
public class AbstractClass extends IClass {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getExtends() {
        return null;
    }

    @Override
    public Collection<String> getImplements() {
        return null;
    }

    @Override
    public String toString() {
        return "this is an abstract class\n";
    }
}
