package InputHandling;

import DataStorage.DataStore.ParsedDataStorage;
import Generation.GeneratorFactory;
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
    private Map<String, PhaseExecution> beginPhases;
    private Map<String, PhaseExecution> endPhases;
    private boolean initialized;

    public DataView() {
        initialized = false;
        supplementaryClasses = new ArrayList<>();
        generatorFactory = new GeneratorFactory();
        generator = generatorFactory.getGenerator("UML");
        phasesToMethods = new HashMap<String, PhaseExecution>();
        beginPhases = new HashMap<String, PhaseExecution>();
        endPhases = new HashMap<String, PhaseExecution>();
//        runPhases();
//        System.out.println(generator.Generate());
    }

    public void openConfigFile(String filePath) throws IOException {

        try {
            prop = new Properties();
            InputStream inputStream = new FileInputStream(filePath);
            prop.load(inputStream);

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
        beginPhases.put("Class-Loading", () -> {
            parseASM();
        });
        endPhases.put("DOT-Generation", () -> {
            try {
                //GraphGenerator generator = new GraphGenerator();

                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getOutputDirectory() + "\\generated_graph.gv")))) {
                    // writer.write("temp write");
                    writer.write(generator.Generate());
                } catch (IOException ex) {
                    // handle me
                }
//                FileWriter writer2 = new FileWriter(new File(prop.getProperty("Output-Directory") + "/generated_graph.dot"));
//                FileOutputStream writer2 = new FileOutputStream(prop.getProperty("Output-Directory") + "/generated_graph.dot");
//                writer2.write(generator.Generate().getBytes());
//                writer2.close();
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getOutputDirectory()+"\\outputGraph.png")))) {
                    writer.write("temp write");
                } catch (IOException ex) {
                    // handle me
                }
                String command = "\"" + prop.getProperty("Dot-Path") + "\" -Tpng \"" + prop.getProperty("Output-Directory") + "\\generated_graph.gv\" -o \"" + prop.getProperty("Output-Directory") + "\\outputGraph.png\"";
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

    @Override
    public void removeClassFromDisplay(String clazzName) {
        ParsedDataStorage.getInstance().removeFromDisplayClasses(clazzName);
    }

    @Override
    public void addClassToDisplay(String clazzName) {
        ParsedDataStorage.getInstance().addToDisplayClasses(clazzName);
    }

    public Iterator<String> getSingletons(){
        return ParsedDataStorage.getInstance().getSingletonClasses().iterator();
    }

    @Override
    public void Analyze() throws IOException {
        if(!initialized){
            performSetup();
            initialized = true;
        }
        endPhases.get("DOT-Generation").execute();
    }

    @Override
    public void performSetup() {
        getClassesFromInputFile();
        getSupplementaryClasses();
        phaseHandler = new PhaseHandler(getPhases(), getPhaseClasses(), phasesToMethods);

        phaseHandler.generateAllPhaseClasses();
        addBasicPhases();
        try {
            generator.parse(supplementaryClasses);
            System.out.println("Display classes: " + ParsedDataStorage.getInstance().getDisplayClasses().size());
            System.out.println("suplementary classes: " + supplementaryClasses.size());
            System.out.println("to load classes: " + classesToLoad.size());
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
        for (PhaseExecution pe : beginPhases.values()) {
            pe.execute();
        }
        for (PhaseExecution pe : phasesToMethods.values()) {
            pe.execute();
        }
        for (PhaseExecution pe : endPhases.values()) {
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

