package ParseClasses;

/**
 * Created by budocf on 1/13/2016.
 */
public class Implements implements IRelation {
    private String implemented;
    private String implementor;

    public Implements(String implemented, String implementor) {
        this.implemented = implemented;
        this.implementor = implementor;
    }

    public String GetImplemented() {
        return implemented;
    }

    public String GetImplementor() {
        return implementor;
    }
}
