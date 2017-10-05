import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FileService {
    private HashMap<String, File> files;
    private ArrayList<File> directories;
    private StringBuilder report;

    /**
     * Constructor for none, one or multiple directories. Handles cases with a port arguments as well.
     *
     * @param args    the command line arguments
     * @param portArg true if the first arguments is a port
     */

    public FileService(String[] args, boolean portArg) {
        directories = new ArrayList<>();
        files = new HashMap<>();
        report = new StringBuilder();
        files = new HashMap<>();
        //get valid directories
        getValidDirectories(args, portArg);
        //populate file list
        directories.forEach(this::populateListWithDirectoryFiles);

    }

    /**
     * Constructor if there are no command line arguments.
     *
     * @param projectDir the project directory, which is used as a default one
     */

    public FileService(File projectDir) {
        report = new StringBuilder();
        files = new HashMap<>();
        populateListWithDirectoryFiles(projectDir);
    }

    /**
     * Iterates through the directory from the arguments array and saves only the ones that exist to a arrayList directories.
     *
     * @param args    the command line arguments
     * @param portArg true if the first arguments is a port, used to define the start of the loop that iterates
     */

    private void getValidDirectories(String[] args, boolean portArg) {
        int i = 0;
        //if there is a port argument the traversing should start at index 1
        if (portArg) {
            i = 1;
        }
        while (i < args.length) {
            File dir = new File(args[i]);
            if (dir.exists()) {
                directories.add(dir);
            } else {
                report.append(args[i]).append(" not found.");
            }
            i++;
        }
    }

    /**
     * Populating the HashMap files with all the readable files in a given directory. The method gets the files in
     * subdirectories as well.
     *
     * @param directory that is the root of the files we use to populate the map
     */

    private void populateListWithDirectoryFiles(File directory) {
        File[] list = directory.listFiles();
        if (list != null) {
            for (File currentFile : list) {
                if (currentFile.isDirectory()) {//if the file is directory use a recursion to get the files in it
                    populateListWithDirectoryFiles(currentFile);
                } else {
                    if (currentFile.canRead() && isTxt(currentFile)) {//if the file is readable add it to the map
                        //if there are file with the same name, the map stores the last path
                        files.put(currentFile.getName(), new File(currentFile.getAbsolutePath()));
                    }
                }
            }
        }
    }

    /**
     * Checks if the map contains a file using its name.
     *
     * @param name the name of the file to check
     * @return whether the file is exist in the server
     */

    boolean containsFile(String name) {
        return files.containsKey(name);
    }

    /**
     * A request for a file by its name.
     *
     * @param name of the requested file
     * @return a File object
     */

    File getFile(String name) {
        return files.get(name);
    }

    /**
     * Gets a collection of the names of all the files available in the server.(the keys of the map)
     *
     * @return a collection of the names of the files in the server
     */

    Collection<String> getFilesCollection() {
        return files.keySet();
    }

    /**
     * Gets a reader for a particular file.
     *
     * @param file the file to read
     * @return the reader and nul of there is any problem with it
     */

    BufferedReader getReader(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.err.println(e.getClass().getName() + " exception at getReader().");
        }
        return br;
    }

    /**
     * Message for the the location of the log file.
     *
     * @return a message for the log file
     */

    String getReport() {
        return report.toString();
    }

    /**
     * Checks if a file has the .txt extension.
     *
     * @param file to check
     * @return true if the file is a txt one
     */

    private boolean isTxt(File file) {
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            if (fileName.substring(i + 1).equals("txt"))
                return true;
        }
        return false;
    }
}
