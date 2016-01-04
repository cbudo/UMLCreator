package Parse;

import java.util.Map;

/**
 * Created by budocf on 12/17/2015.
 */
public class ParsedDataStorage implements IDataStorage {

    Map<String, IData> classes = null;
    Map<String, IData> interfaces = null;

    @Override
    public void addMethod(String cName, IMethod method) {

    }

    @Override
    public void addField(String cName, IField field) {

    }
}
