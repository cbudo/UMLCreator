package Visitors.DefaultVisitors;

import java.io.IOException;

/**
 * Created by budocf on 1/19/2016.
 */
public interface IVisitMethod {
    void execute(ITraverser t) throws IOException;
}
