package ParseClasses;

/**
 * Created by efronbs on 1/18/2016.
 */
public class UsesRelation implements IRelation {
    private String from;
    private String to;

    public UsesRelation(String to, String from) {
        this.to = to;
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }
}
