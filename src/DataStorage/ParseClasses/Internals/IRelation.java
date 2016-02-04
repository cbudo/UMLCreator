package DataStorage.ParseClasses.Internals;

import Visitors.DefaultVisitors.ITraverser;

/**
 * Created by budocf on 1/13/2016.
 */
public interface IRelation extends ITraverser {

    String getTo();

    String getFrom();

    String getArrowName();

}
