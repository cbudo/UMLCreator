package InputHandling;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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

    void setPropertiesFile(String filePath) throws IOException;
    Iterator<String> getDecorators();
    Iterator<String> getSingletons();
    Iterator<String> getComponents();

    String getOutputDirectory();

    void setClassesToShow(List<String> classesToShow);
}
