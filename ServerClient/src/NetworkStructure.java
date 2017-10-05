import java.io.*;
import java.net.InetAddress;
import java.util.Collection;

/**
 * Based on example code from cs1006 tcp/ by saleem, Jan 2015.
 * https://studres.cs.st-andrews.ac.uk/2015_2016/CS1006/Examples/tcp/TcpTextChat.java
 */

abstract class NetworkStructure {
    final static String _disconnect = "disconnect";//the current connection
    //server states:
    final static String _busy = "busy";
    final static String _idle = "idle";
    private final static String _exit = "exit";//exit program
    private final static String _list = "list";//list of available files
    static State _state;

    //streams
    static BufferedReader _in;
    static PrintWriter _out;
    static String logDiary;
    //port
    static int port = Variables.port;
    static String classSetUp;
    private static BufferedReader _stdIn;
    //log
    private static FileWriter log;

    /**
     * Wrap up printing method to report errors.
     *
     * @param s the error message
     */

    static void error(String s) {
        System.err.println("-!- " + s);
    }

    /**
     * Wrap up printing method to report program messages.
     *
     * @param s the program message
     */

    static void report(String s) {
        System.out.println("-*- " + s);
    }

    /**
     * Wrap up printing method.
     *
     * @param s the message to print
     */

    static void print(String s) {
        System.out.println(s);
    }

    /**
     * Checks if the passed string is equal to "exit".
     *
     * @param s the string to check
     * @return true if the checked string is equal to "exit"
     */

    static boolean checkQuit(String s) {
        return s != null && s.compareTo(_exit) == 0;
    }

    /**
     * Checks if the passed string is equal to "list".
     *
     * @param s the string to check
     * @return true if the checked string is equal to "list"
     */

    static boolean checkList(String s) {
        return s != null && s.compareTo(_list) == 0;
    }

    /**
     * Checks if the passed string is equal to "disconnect".
     *
     * @param s the string to check
     * @return true if the checked string is equal to "disconnect"
     */

    static boolean checkDisconnect(String s) {
        return s != null && s.compareTo(_disconnect) == 0;
    }

    /**
     * A wrap up method for reading the user input.
     *
     * @return a single line of user input
     */

    static String readKeyboard() {
        String line = null;
        try {
            if (_stdIn.ready()) {
                line = _stdIn.readLine();
                makeRecord(RecordType.keyboard, line);
            }
        } catch (IOException e) {
            error("readKeyboard(): problem reading! " + e.getClass().getName());
            System.exit(-1);
        }
        return line;
    }

    /**
     * A wrap up method for reading the user input, but it waits for a response !=null.
     *
     * @return the non-null user input
     */

    static String readKeyboardNonNull() {
        String line = null;
        try {
            line = _stdIn.readLine();
            makeRecord(RecordType.keyboard, line);

        } catch (IOException e) {
            error("readKeyboard(): problem reading! " + e.getClass().getName());
            System.exit(-1);
        }
        return line;
    }

    /**
     * A wrap up method for reading the network stream.
     *
     * @return a single line of network input
     */

    static String recvLine() {
        if (_in == null)
            return null;
        String line = null;
        try {
            if (_in.ready()) {
                line = _in.readLine();
                makeRecord(RecordType.receiveNetwork, line);
            }
        } catch (IOException e) {
            error("io problem with recvLine(): " + e.getClass().getName());
            System.exit(-1);
        }
        return line;
    }

    /**
     * A wrap up method for sending strings to the network.
     *
     * @param line to sent to the network
     */

    static void sentLine(String line) {
        if (_out == null)
            return;
        try {
            _out.println(line);
            _out.flush();
            makeRecord(RecordType.sendNetwork, line);
        } catch (Exception e) {
            error("Problem with sentLine(): " + e.getClass().getName());
        }
    }

    /**
     * Sends the collection as a whole.
     *
     * @param files the collection
     */

    static void sentCollection(Collection<String> files) {
        if (_out == null)
            return;
        try {

            for (String file : files) {
                _out.println(file);
                makeRecord(RecordType.sendNetwork, file);
            }
            _out.flush();
        } catch (Exception e) {
            error("Problem with sentLine(): " + e.getClass().getName());
        }
    }

    /**
     * Sends the content of a file as a whole. Flushes the stream at the end.
     *
     * @param fileReader the file reader - content of the file
     */

    static void sentFile(BufferedReader fileReader) {
        if (_out == null)
            return;
        String line;
        try {
            while ((line = fileReader.readLine()) != null) {
                _out.println(line);
                makeRecord(RecordType.sendNetwork, line);
            }
            fileReader.close();
            _out.flush();

        } catch (Exception e) {
            error("Problem with sentLine(): " + e.getClass().getName());
        }
    }

    /**
     * A startup method to greet and list the details of the current client/server. Also assign the input stream of the
     * user to a local BufferedReader.
     */

    static void startup() {
        //greetings
        String me = System.getProperty("user.name");
        report("greetings " + me);
        InetAddress h;
        String s = "(unknown)";
        try {
            h = InetAddress.getLocalHost();
            s = h.getCanonicalHostName();
            report("host: " + InetAddress.getByName(s));
        } catch (java.net.UnknownHostException e) {
            s = "(unknown)";
            error("startup(): cannot get hostname!");
        }
        report(classSetUp);
        report("ready ...");

        // local input
        _stdIn = new BufferedReader(new InputStreamReader(System.in));
        //state
        _state = State.idle;
        //log
        logStartup();
    }

    /**
     * A setup method for the logging. Creates a file and a fileWriter to write in it.
     */

    private static void logStartup() {
        try {
            File fileLog = new File(logDiary);
            fileLog.createNewFile();
            log = new FileWriter(logDiary, false);
        } catch (IOException e) {
            error("io problem with logStartup(): " + e.getClass().getName());
            System.exit(-1);
        }
    }

    /**
     * A wrap up method for writing in the log diary.
     *
     * @param type of record
     * @param s    the line that is process
     */

    private static void makeRecord(RecordType type, String s) {
        try {
            log.write(type + " : " + s + "\n");
            log.flush();
        } catch (IOException e) {
            error("io problem with logStartup(): " + e.getClass().getName());
            //no need to exit as this is only the logging
        }
    }

}
