package InputHandling;

import DataStorage.DataStore.ParsedDataStorage;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by efronbs on 2/17/2016.
 */
public class FileHandler {
    public static void listFilesForFolder(final File folder, String basePath, List<InputStream> classesToParse) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                FileHandler.listFilesForFolder(fileEntry, basePath, classesToParse);
            } else {
                ParsedDataStorage.getInstance().addToDisplayClasses(parseForPackage(fileEntry.getPath(), basePath));
            }
        }
    }

    private static String parseForPackage(String fullFPath, String basePath) {
        fullFPath = fullFPath.replace("\\", "/");
        int pathLoc = fullFPath.indexOf(basePath);
        String packageName = fullFPath.substring(pathLoc + basePath.length());
        packageName = packageName.substring(1);
        packageName = packageName.replace("/", ".");
        packageName = packageName.replace(".java", "");
        String[] packageHeader = basePath.split("/");
        packageName = packageHeader[packageHeader.length - 1] + "." + packageName;
        return packageName;
    }
}
