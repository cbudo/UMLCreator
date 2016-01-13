package ParseClasses;

/**
 * Created by budocf on 1/13/2016.
 */
public class Extends implements IRelation {
    private String superClass;
    private String subClass;

    public Extends(String superclass, String subclass) {
        this.subClass = subclass;
        this.superClass = superclass;
    }

    public String GetSuper() {
        return superClass;
    }

    public String GetSub() {
        return subClass;
    }
}
