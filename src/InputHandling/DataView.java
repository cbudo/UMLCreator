package InputHandling;

import DataStorage.DataStore.ParsedDataStorage;
import Generation.GeneratorFactory;
import Generation.IGenerator;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Created by efronbs on 2/18/2016.
 */
public class DataView implements IParserViewer {
    private Properties prop;
    private InputStream propertiesInput;
    private GeneratorFactory generatorFactory;
    private IGenerator generator;
    private PhaseHandler phaseHandler;


    public DataView() {
        try {
            openConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        generatorFactory = new GeneratorFactory();
        generator = generatorFactory.getGenerator("UML");
        phaseHandler = new PhaseHandler(getPhases());

    }

    private void openConfigFile() throws IOException {

        try {
            prop = new Properties();
            String propFileName = "config.properties";
            propertiesInput = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (propertiesInput != null) {
                prop.load(propertiesInput);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            //System.out.println("PATH FROM CONFIG: " + prop.getProperty("Input-Folder"));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    private void closeConfigFile() throws IOException {
        if (propertiesInput != null)
            propertiesInput.close();
    }

    private String getInputFolderPath() {
        return prop.getProperty("Input-Folder");
    }

    private String[] getSupplementaryClassesFromProperties() {
        String rawClassString = prop.getProperty("Input-Classes");
        return rawClassString.split(",");
    }

    private String[] getPhases() {
        return prop.getProperty("Phases").split(",");
    }

    private void setupPhases() {
        try {
            phaseHandler.setupClassLoadingPhase(DataView.class.getDeclaredMethod("parseASM"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Analyze() throws IOException {
        runPhases();
        System.out.println(generator.Generate());
    }

    @Override
    public void performSetup() {
        getClassesFromInputFile();
        getSupplementaryClasses();
//        for (String s : ParsedDataStorage.getInstance())
//            System.out.println(s);
        //parseASM();
        setupPhases();
    }

    @Override
    public void getClassesFromInputFile() {
        String path = getInputFolderPath();
        File folder = new File(path);
//        FileHandler.listFilesForFolder(folder, path);
    }

    @Override
    public void getSupplementaryClasses() {
        String[] suppClasses = getSupplementaryClassesFromProperties();
        for (String suppClassName : suppClasses)
            ParsedDataStorage.getInstance().addToDisplayClasses(suppClassName);
    }

    @Override
    public void parseASM() {
        try {
            generator.parse(ParsedDataStorage.getInstance().getDisplayClasses());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runPhases() {
        try {
            phaseHandler.runTheTrap();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        try {
            closeConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPropertiesFile(String filePath) throws IOException{
        prop = new Properties();
        InputStream inputStream = new FileInputStream(filePath);
        prop.load(inputStream);
    }


}

