package ParseClasses;

import Visitor.ITraverser;

/**
 * Created by budocf on 1/13/2016.
 */
public interface IRelation extends ITraverser {

    String getTo();

    String getFrom();

}
