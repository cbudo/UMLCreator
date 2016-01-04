package Parse;

import java.util.Map;

/**
 * Created by budocf on 12/17/2015.
 */
public interface IMethod extends IData {
    String name = null;
    String returnType = null;
    String accessability = null;
    Map<String, String> paramNameToType = null;
}
