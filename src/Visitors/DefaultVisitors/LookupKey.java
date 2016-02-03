package Visitors.DefaultVisitors;

/**
 * Created by budocf on 1/19/2016.
 */
public class LookupKey {
    VisitType visitType;
    Class<?> clazz;

    public LookupKey(VisitType visitType, Class<?> clazz) {
        this.visitType = visitType;
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LookupKey other = (LookupKey) obj;
        return visitType == other.visitType && other.clazz.isAssignableFrom(this.clazz);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((visitType == null) ? 0 : visitType.hashCode());
        return result;
    }

}
