package InputHandling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by efronbs on 2/17/2016.
 */
public class FileHandler {
    public static void listFilesForFolder(final File folder, String basePath, Map<String, FileInputStream> classesToParse) throws FileNotFoundException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                FileHandler.listFilesForFolder(fileEntry, basePath, classesToParse);
            } else if (fileEntry.getPath().endsWith(".class")) {
                String fullFPath = fileEntry.getPath().replace("\\", "/");
                FileInputStream iostream = new FileInputStream(fullFPath);
                int startingPoint = fullFPath.indexOf(basePath);
                String name = fullFPath.substring(startingPoint + basePath.length()).replace("/", ".");
                String[] dirs = basePath.split("/");
                String lastDir = dirs[dirs.length - 1];
                name = name.replace(".class", "");
                name = lastDir + name;
                //System.out.println(name);
                classesToParse.put(name, iostream);
            }
        }
    }
}
