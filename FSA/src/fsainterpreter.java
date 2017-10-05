import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class fsainterpreter {

    public static void main(String[] args) {
        //args = new String[]{"example-2.fsa", "example-3.fsa"};
        if (args.length > 0) {
            BufferedReader userStream = getUserStream();
            ArrayList<FSA> machines = generateFSAs(args);
            if (machines.size() > 0) {
                if (userStream != null) {
                    try {
                        while ((userStream.read()) != -1) {
                            userStream.reset();
                            System.out.println(compoundResult(machines, userStream));
                            //userStream.skip(1); //this works fine for the compound and breaks for a single fsa
                            userStream.mark(1000);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else System.out.println("No arguments found. Example: java fsainterpreter fsa1.fsa <test.txt ");
    }

    /**
     * Gets the last fsa object of the run through and returns the validation of the input.
     *
     * @param userStream stream of the user input
     * @return the status of the run
     */

    public static String compoundResult(ArrayList<FSA> machines, BufferedReader userStream) throws IOException {
        FSA last = getLastFSA(machines, userStream);
        if (last != null) {
            if (last.getNumber() == machines.size() && last.isAccepting()) {
                return "Accepted";
            }
            userStream.readLine();
            return "Not accepted";
        } else return "Not accepted";

    }

    /**
     * Creates a FSA object from a String reference to a file, containing a transition table of a fsa. Checks for right
     * format of the file and any corruptions in the table as well.
     *
     * @param fsaDescription String reference to a file, containing a transition table of a fsa
     * @return the FSA object
     */
    public static FSA createFSA(String fsaDescription) {
        FSA fsa = null;
        try {
            fsa = new FSA(fsaDescription);
        } catch (WrongFormatException e) {
            System.out.println("Expected: <fsaDescription>.fsa, found: " + fsaDescription);
        } catch (NumberFormatException e) {
            System.out.println("Corrupted description in " + fsaDescription);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return fsa;
    }

    /**
     * Generates all available fsa from the args.
     *
     * @param args the array of arguments passed at the program at the command line
     * @return arrayList of the valid fsas
     */

    public static ArrayList<FSA> generateFSAs(String[] args) {
        ArrayList<FSA> machines = new ArrayList<>();
        for (String arg : args) {
            FSA fsa = createFSA(arg);
            if (fsa != null) {
                machines.add(fsa);
            }
        }
        return machines;
    }

    /**
     * Runs through all the machines consecutively, passing the stream to every fsa.
     *
     * @param userStream stream of the user input
     * @return the last instance of a fsa object
     */

    public static FSA getLastFSA(ArrayList<FSA> machines, BufferedReader userStream) {
        for (FSA fsa : machines) {
            try {
                fsa.runFSA(userStream);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            if (!fsa.isReadyToPass()) {
                return fsa;
            } else {
                fsa.resetReadyToPass();
            }
        }
        return null;
    }

    /**
     * Generates a BufferedReader from the System.in and marks the beginning.
     *
     * @return the user stream
     */

    public static BufferedReader getUserStream() {
        BufferedReader userStream = null;
        try {
            userStream = new BufferedReader(
                    new InputStreamReader(System.in));//new FileInputStream("comTest.txt")))
            userStream.mark(1000);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return userStream;
    }
}
