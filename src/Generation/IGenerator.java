package Generation;

import java.io.IOException;
import java.util.List;

/**
 * Created by budocf on 1/13/2016.
 */
public interface IGenerator {
    String getOutputType();
    String Generate();

    void parse(List<String> args) throws IOException;
}
