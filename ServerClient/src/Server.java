import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

public final class Server extends NetworkStructure {
    private static ServerSocket _listen;
    private static Socket client;
    private static FileService fileService;


    public static void main(String[] args) {
        //get details for the server set up and lookup directories
        computeArgs(args);
        //configure log diary
        logDiary = Variables.severDiary;
        //create server socket
        createListeningSocket();
        classSetUp = "Server started on port " + port + ".";
        //greetings
        startup();
        print("");

        while (true) {
            //server keyboard control
            checkServerCommands();

            //check incoming connection
            Socket connection = checkIncomingConnections();

            //if there is one send status(busy/idle)
            if (connection != null) {
                sentState(connection);
            }

            //connection options
            switch (_state) {
                case idle:
                    //assign the client socket to the current connection
                    if (connection != null)
                        connectToClient(connection);
                    break;
                case busy:
                    //get client request
                    String networkLine = recvLine();//the requested file name, list command or null if the client disconnected
                    if (networkLine != null) {
                        if (checkList(networkLine)) {
                            sentListOfFiles();
                            break;
                        }
                        service(networkLine);
                    }
                    break;

            }//switch

            // avoid CPU overhead of continuous looping here
            try {
                Thread.sleep(Variables.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }//while
    }//main

    /**
     * The main functionality of the server: reading a requested file and sending the information to the client.
     *
     * @param networkLine the name of the requested file
     */

    private static void service(String networkLine) {
        if (fileService.containsFile(networkLine)) {
            File fileToRead = fileService.getFile(networkLine);
            BufferedReader fileReader = fileService.getReader(fileToRead);
            if (fileReader != null) {
                report("Sending content of file " + networkLine + "...");
                sentFile(fileReader);
                report("Done.");
            } else {//there was a problem with the reader setup in fileService class
                sentLine("Problem with reading the file!");
            }
        } else {//the file is not found in the server
            sentLine("File not found! Use list command to see available files.");
        }
    }

    /**
     * Sets up a server socket to listen at a certain port.
     */

    private static void createListeningSocket() {
        // create listening socket
        try {
            _listen = new ServerSocket(port);
            _listen.setSoTimeout(Variables.soTimeout);
        } catch (java.io.IOException e) {
            error("server failed! " + e.getClass().getName());
            System.exit(-1);
        }
    }

    /**
     * A method to shutdown th server. It closes the client socket if thee is any and the server one.
     */


    private static void shutdown() {
        try {
            if (client != null) {
                client.close();
            }
            _listen.close();
            report("server shutdown...");
        } catch (IOException e) {
            error("io problem() when shutting down the server " + e.getClass().getName());
            System.exit(-1);
        }
    }

    /**
     * Disconnects the client by closing the input and output streams and the client socket.
     */


    private static void disconnectClient() {
        try {
            _in.close();
            _out.close();
            client.close();
        } catch (IOException e) {
            error("io problem at disconnectClient(): " + e.getClass().getName());
            System.exit(-1);
        }
        client = null;
        _in = null;
        _out = null;
        _state = State.idle;
        report("The client is successfully disconnected!");
    }

    /**
     * Checks if a client has connected to the listening socket.
     *
     * @return the socket (connection)
     */

    private static Socket checkIncomingConnections() {
        Socket connection = null;
        try {
            connection = _listen.accept();
        } catch (java.net.SocketTimeoutException e) {
            // ignore
        } catch (java.net.UnknownHostException e) {
            report("checkIncomingCall(): cannot get hostname of remote host");
        } catch (java.io.IOException e) {
            error("io problem at checkIncomingCall(): " + e.getClass().getName());
            System.exit(-1);
        }
        return connection;
    }

    /**
     * Connects to a client. Assigns the in and out streams and updates the server state. Also reports the made connection
     * and print the client's hostname, address and port.
     *
     * @param connection the socket
     */

    private static void connectToClient(Socket connection) {
        client = connection;
        try {
            _in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            _out = new PrintWriter(client.getOutputStream(), false);
        } catch (IOException e) {
            error("io problem connectToClient(): " + e.getClass().getName());
            System.exit(-1);
        }

        report("new client");
        report("remote: " +
                client.getInetAddress().getHostName() + " " +
                client.getInetAddress().getHostAddress() +
                " port: " + client.getPort());
        print("");

        _state = State.busy;
    }

    /**
     * Sends the current state of the server to block incoming connections if it is busy or wait for further instructions
     * if it is idle.
     *
     * @param connection the socket
     */

    private static void sentState(Socket connection) {
        PrintWriter pw;
        try {
            pw = new PrintWriter(connection.getOutputStream(), true);
            switch (_state) {
                case idle:
                    pw.println(_idle);
                    break;
                case busy:
                    report("client: " +
                            connection.getInetAddress().getHostName() + " " +
                            connection.getInetAddress().getHostAddress() +
                            " port: " + connection.getPort() + " tried to connect to the server.");
                    pw.println(_busy);
                    connection.close();
                    break;
            }
        } catch (IOException e) {
            error("io problem at sentState(); " + e.getClass().getName());
        }
    }

    /**
     * Sends the list of available files on the server.
     */

    private static void sentListOfFiles() {
        report("Sending a list of available files.");
        //sent list
        Collection<String> files = fileService.getFilesCollection();
        //empty collection
        if (files.size() == 0)
            sentLine("No files available in the server!");
        else {
            //files.forEach(NetworkStructure::sentLine);
            sentCollection(files);
        }
        report("Request done.");
    }

    /**
     * Deals with the command line arguments. Checks if there is any port or directories and assigns them or uses the
     * default ones if there aren't any in the arguments.
     *
     * @param args array of command line arguments
     */

    private static void computeArgs(String[] args) {
        if (args.length == 0) {
            report("No commandline arguments found. Looking up for files in the project directory only!");
            String projectDirectory = System.getProperty("user.dir");
            fileService = new FileService(new File(projectDirectory));
        } else {
            boolean portArg = true;
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //no port arg
                portArg = false;
            }
            fileService = new FileService(args, portArg);
        }
        String report = fileService.getReport();
        if (report != null)
            report(report);
    }

    /**
     * Wrap up method of recvLine() from NetworkStructure to check for the command "disconnect" from the user.
     *
     * @return the network line or null if the incoming message is to disconnect.
     */

    static String recvLine() {
        String line = NetworkStructure.recvLine();
        if (checkDisconnect(line)) {
            disconnectClient();
            return null;
        }
        return line;
    }

    /**
     * Checks if there is any server keyboard input. Handles both correct and invalid commands. Wrap up method of the
     * readKeyboard() from the NetworkStructure.
     */

    private static void checkServerCommands() {
        String line = NetworkStructure.readKeyboard();
        if (checkDisconnect(line) && _state == State.busy) {
            sentLine(_disconnect);
            disconnectClient();
        } else if (checkQuit(line)) {
            if (_state == State.busy) {
                sentLine(_disconnect);
                disconnectClient();
            }
            shutdown();
            System.exit(0);
        } else if (line != null) {
            report("unknown command " + line);
            report("Enter \"disconnectClient\" to disconnect from the client or \"exit\" to shutdown the server.");
        }
    }


}
