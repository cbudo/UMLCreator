package Parse;

/**
 * Created by budocf on 12/17/2015.
 */
public interface IDataStorage {
    void addMethod(String cName, IMethod method);

    void addField(String cName, IField field);
}
