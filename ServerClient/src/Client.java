import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public final class Client extends NetworkStructure {
    private static Socket server;

    public static void main(String[] args) {
        //diary
        logDiary = Variables.clientDiary;
        classSetUp = "Client started.";
        startup();
        print("Waiting for:");
        print("1) hostname/address");
        print("2) port");

        while (true) {

            String keyboardLine = readKeyboard();

            switch (_state) {
                case busy:
                    recvLine();
                    if (keyboardLine != null) {
                        while (server != null) {
                            //user input - keyboardLine
                            if (keyboardLine != null) {
                                //requested file name or list command
                                sentLine(keyboardLine);

                                //getting the first line of response
                                String line = null;
                                while (line == null) {
                                    line = recvLine();
                                }

                                //if not a disconnect flag => response to request
                                if (server != null) {
                                    printResponse(line, keyboardLine);
                                    //check for disconnect
                                    recvLine();
                                }

                            }// if (keyboardLine != null)
                            keyboardLine = readKeyboard();
                            //checking for disconnect flag
                            recvLine();

                        }//while in the server
                        //checking for disconnect flag
                        recvLine();
                    }//if(keyboardLine!=null)
                    break;
                case idle:
                    if (keyboardLine != null) {
                        //get port
                        getPort();
                        //connect to server
                        Socket connection = getConnection(keyboardLine);
                        //check if the connection is established
                        if (connection != null)
                            getState(connection);
                    }
                    break;
            }//switch

            try {
                Thread.sleep(Variables.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Wrap up method of readKeyboard() from NetworkStructure to handle disconnect and exit commands.
     *
     * @return the line or null if the line is disconnect or exit
     */

    static String readKeyboard() {
        String line = NetworkStructure.readKeyboard();
        if (checkDisconnect(line) && _state == State.busy) {
            sentLine(_disconnect);
            disconnectServer();
            return null;
        } else if (checkQuit(line)) {
            if (_state == State.busy) {
                sentLine(_disconnect);
                disconnectServer();
            }
            shutdown();
            System.exit(0);
        }
        return line;
    }

    /**
     * getting a port from the keyboard and assigning to the global port if it is an integer. In case it is not the
     * program uses the default port in Variables.
     */

    private static void getPort() {
        String portString = readKeyboardNonNull();
        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            //wrong format
            error("Expected port number(int), but found: \"" + portString + "\".");
            report("The client will try to connect with the default port: " + port + ".");
        }
    }

    /**
     * Shutdowns the client by closing the socket.
     */

    private static void shutdown() {
        try {
            if (server != null)
                server.close();
            report("Client shutdown...");
        } catch (IOException e) {
            error("io problem() when shutting down the client " + e.getClass().getName());
            System.exit(-1);
        }
    }

    /**
     * Connects to the server and binding the in and out streams. Reports the local and server details.
     *
     * @param connection the socket
     */

    private static void connectToServer(Socket connection) {
        server = connection;
        try {
            _in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            _out = new PrintWriter(server.getOutputStream(), true);
        } catch (IOException e) {
            error("io problem connectToServer(): " + e.getClass().getName());
            report("Disconnecting from the server.");
            disconnectServer();
        }

        report("local: " +
                server.getLocalAddress().getHostName() + " " +
                server.getLocalAddress().getHostAddress() +
                " port: " + server.getLocalPort());
        report("server: " +
                server.getInetAddress().getHostName() + " " +
                server.getInetAddress().getHostAddress() +
                " port: " + server.getPort());
        report("");

        //update state
        _state = State.busy;
    }

    /**
     * Gets the current state of a server and react properly to it. If it is busy the socket is closed, if it is idle then
     * it connects to it and if any other status is found there is an error displayed and the socket is closed as well.
     *
     * @param connection the socket
     */

    private static void getState(Socket connection) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String state = in.readLine();
            switch (state) {
                case _idle:
                    connectToServer(connection);
                    break;
                case _busy:
                    report("Server is busy! Try again later.");
                    connection.close();
                    break;
                default: {
                    error("Unrecognizable server status. Expected busy/idle found " + state + ".");
                    connection.close();
                }
            }
        } catch (IOException e) {
            error("io problem, when reading the server status: " + e.getClass().getName());
        }

    }

    /**
     * Gets the socket from the current port and hostname/address. In case of incorrect hostname or port a error is displayed
     * and the user can try inputting them again.
     *
     * @param lineKeyboard the hostname/address
     * @return the socket, null if any exceptions interrupted
     */

    private static Socket getConnection(String lineKeyboard) {
        Socket connection = null;
        try {
            connection = new Socket(lineKeyboard, port);
        } catch (UnknownHostException e) {
            error("Client failed to connect to the server! Exception: " + e.getClass().getName());
            report("Please try again with different hostname/address.");
        } catch (ConnectException e) {
            error("Client failed to connect to the server! Exception: " + e.getClass().getName());
            report("Please try again with different port.");
        } catch (IllegalArgumentException e) {
            error(e.getLocalizedMessage());
            report("Try again.");
        } catch (IOException e) {
            error("Client failed! Exception: " + e.getClass().getName());
            System.exit(-1);
        }
        return connection;
    }

    /**
     * Printing a response from the server.
     *
     * @param line     the first line of response
     * @param filename the name of the requested file
     */

    private static void printResponse(String line, String filename) {
        report("Server response for " + filename + ".");
        while (line != null) {
            print(line);
            line = NetworkStructure.recvLine();
        }
        report("End of response.");
    }

    /**
     * Disconnects from the server by closing the in and out streams and the socket.
     */

    private static void disconnectServer() {
        try {
            _in.close();
            _out.close();
            server.close();
        } catch (IOException e) {
            error("Disconnecting from the server failed! Exceptions: " + e.getClass().getName());
        }
        server = null;
        _in = null;
        _out = null;
        report("Disconnected from the server!");

        //update state
        _state = State.idle;
    }

    /**
     * Wrap up method of recvLine() from NetworkStructure to check for the command "disconnect" from the user.
     *
     * @return the network line or null if the incoming message is to disconnect.
     */

    static String recvLine() {
        String line = NetworkStructure.recvLine();
        if (checkDisconnect(line)) {
            disconnectServer();
        }
        return line;
    }


}

