package ParseClasses;

/**
 * Created by efronbs on 1/18/2016.
 */
public class AssociationRelation implements IRelation {
    String from;
    String to;

    public AssociationRelation(String to, String from) {
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
