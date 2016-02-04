package DataStorage.ParseClasses.Internals;

import Visitors.DefaultVisitors.IVisitor;

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

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IRelation)) return false;

        IRelation that = (IRelation) o;

        if (getFrom() != null && getFrom().equals(that.getFrom())) {
            if (getTo() != null && getTo().equals(that.getTo())) {
                return true;
            }
        }
        return false;

        //return getFrom() != null ? getFrom().equals(that.getFrom()) : that.getFrom() == null && (getTo() != null ? getTo().equals(that.getTo()) : that.getTo() == null);

    }

    @Override
    public int hashCode() {
        int result = getFrom() != null ? getFrom().hashCode() : 0;
        result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
        return result;
    }
}
