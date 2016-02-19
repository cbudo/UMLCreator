package InputHandling;

import DataStorage.DataStore.ParsedDataStorage;
import Generation.GeneratorFactory;
import Generation.GraphGenerator;
import Generation.IGenerator;

import java.io.*;
import java.util.*;

/**
 * Created by efronbs on 2/18/2016.
 */
public class DataView implements IParserViewer {
    private Properties prop;
    private InputStream propertiesInput;
    private GeneratorFactory generatorFactory;
    private IGenerator generator;
    private PhaseHandler phaseHandler;

    private List<String> supplementaryClasses;
    private Map<String, FileInputStream> classesToLoad;
    private Map<String, PhaseExecution> phasesToMethods;

    public DataView() {
        try {
            openConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        supplementaryClasses = new ArrayList<>();
        generatorFactory = new GeneratorFactory();
        generator = generatorFactory.getGenerator("UML");
        phasesToMethods = new HashMap<String, PhaseExecution>();
        phaseHandler = new PhaseHandler(getPhases(), getPhaseClasses(), phasesToMethods);
        addBasicPhases();
//        runPhases();
//        System.out.println(generator.Generate());
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

    private String[] getPhaseClasses() {
        return prop.getProperty("PhaseClasses").split(",");
    }

    private void addBasicPhases() {
        phasesToMethods.put("Class-Loading", () -> {
            parseASM();
        });
        phasesToMethods.put("DOT-Generation", () -> {
            try {
                GraphGenerator generator = new GraphGenerator();
                System.out.println(generator.Generate());
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getOutputDirectory()+"\\generated_graph.dot")))) {
                    writer.write("temp write");
                } catch (IOException ex) {
                    // handle me
                }
                FileWriter writer2 = new FileWriter(prop.getProperty("Output-Directory") + "\\generated_graph.dot");
                writer2.write(generator.Generate());
                writer2.close();
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getOutputDirectory()+"\\outputGraph.png")))) {
                    writer.write("temp write");
                } catch (IOException ex) {
                    // handle me
                }
                String command = "\"" + prop.getProperty("Dot-Path") + "\" -Tpng \"" + prop.getProperty("Output-Directory") + "\\generated_graph.dot\" -o \"" + prop.getProperty("Output-Directory") + "\\outputGraph.png\"";
                System.out.println("Command: " + command);
                final Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

    }


    public  Iterator<String> getDecorators(){
        return ParsedDataStorage.getInstance().getDecoratorClasses().iterator();
    }
    public Iterator<String> getComponents(){
        return ParsedDataStorage.getInstance().getComponentClasses().iterator();
    }

    @Override
    public String getOutputDirectory() {
        return prop.getProperty("Output-Directory");
    }

    @Override
    public void setClassesToShow(List<String> classesToShow) {
        ParsedDataStorage.getInstance().setToDisplayClasses(classesToShow);
    }

    public Iterator<String> getSingletons(){
        return ParsedDataStorage.getInstance().getSingletonClasses().iterator();
    }

    @Override
    public void Analyze() throws IOException {
        phasesToMethods.get("DOT-Generation").execute();
    }

    @Override
    public void performSetup() {
        getClassesFromInputFile();
        getSupplementaryClasses();
        phaseHandler.generateAllPhaseClasses();

        try {
            generator.parse(supplementaryClasses);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (classesToLoad.size() > 0) {
            generator.parseFromStream(classesToLoad);
        }
        runPhases();
        System.out.println(generator.Generate().replace("$", ""));
    }

    @Override
    public void getClassesFromInputFile() {
        String path = getInputFolderPath();
        classesToLoad = new HashMap<String, FileInputStream>();
        if (path.equals("")) {
            return;
        }
        File folder = new File(path);
        try {
            FileHandler.listFilesForFolder(folder, path, classesToLoad);
        } catch (FileNotFoundException e) {
            System.out.println("error opening file paths from input directory " + e);
        }
    }

    @Override
    public void getSupplementaryClasses() {
        String[] suppClasses = getSupplementaryClassesFromProperties();
        for (String suppClassName : suppClasses) {
            supplementaryClasses.add(suppClassName);
            ParsedDataStorage.getInstance().addToDisplayClasses(suppClassName);
        }
    }

    private String getDotExecutionPath() {
        return prop.getProperty("Dot-Path");
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
        for (PhaseExecution pe : phasesToMethods.values()) {
            pe.execute();
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
    public void setPropertiesFile(String filePath) throws IOException {
        prop = new Properties();
        InputStream inputStream = new FileInputStream(filePath);
        prop.load(inputStream);
    }


}

