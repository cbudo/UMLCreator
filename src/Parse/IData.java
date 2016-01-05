package Parse;

/**
 * Created by budocf on 12/17/2015.
 */
public abstract class IData {
    protected String name = null;
    protected String accessibility = null;

    public void setAccess(String access) {
        this.accessibility = access;
    }

    public void setName(String name) {
        this.name = name;
    }
}
