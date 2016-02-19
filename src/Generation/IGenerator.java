package Generation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by budocf on 1/13/2016.
 */
public interface IGenerator {
    String getOutputType();
    String Generate();

    void parse(List<String> args) throws IOException;

    void parseFromStream(Map<String, FileInputStream> filesToParse);
}
