package Parse;

import java.util.Map;

/**
 * Created by budocf on 12/17/2015.
 */
public interface IData {
    String name = null;
    String accessability = null;
    Map<String, IMethod> _methods = null;
    Map<String, IField> _fields = null;
}
