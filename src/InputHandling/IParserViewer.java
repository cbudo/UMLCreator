package InputHandling;

import java.io.IOException;

/**
 * Created by efronbs on 2/17/2016.
 */
public interface IParserViewer {
    void Analyze() throws IOException;

    void performSetup();

    void getClassesFromInputFile();

    void getSupplementaryClasses();

    void parseASM();

    void runPhases();

    void exit();

}
