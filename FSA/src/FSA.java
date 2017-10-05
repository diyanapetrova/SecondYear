import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * A class that represent a finite state automaton.
 */
public class FSA {
    private static int count = 0;
    private HashMap<Integer, State> states;
    private State initialState;
    private boolean accepting;
    private boolean readyToPass;
    private int number;

    public FSA(String fsaDescription) throws IOException, WrongFormatException {
        count++;
        number = count;
        states = new HashMap<>();
        accepting = false;
        readyToPass = false;
        loadFSA(fsaDescription);
    }

    public int getNumber() {
        return number;
    }

    public boolean isReadyToPass() {
        return readyToPass;
    }

    /**
     * Reads the fsa description from a file and store the mappings into a hashMap.
     * @param fsaDescription the String reference to the file containing a transition table
     * @throws WrongFormatException the extension of the file differ from ".fsa"
     * @throws IOException
     */
    private void loadFSA(String fsaDescription) throws WrongFormatException, IOException {
        //check for the format of the input file
        String extension = fsaDescription.substring(fsaDescription.lastIndexOf('.') + 1, fsaDescription.length());
        if (!extension.equals("fsa")) {
            throw new WrongFormatException();
        }

        boolean initialStateTaken = false;
        BufferedReader reader = new BufferedReader(new FileReader(fsaDescription));
            String line;
            //read the file line by line
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] arr = line.split(" ");
                    int stateN = Integer.parseInt(arr[0]);
                    char input = arr[1].charAt(0);
                    int nextState = Integer.parseInt(arr[2]);

                    //add accepting state
                    if (arr.length > 3) {
                        if (arr[3].equals("*")) {
                            State accState = new AcceptingState();
                            states.put(nextState, accState);
                        }
                    }
                    //add new mappings
                    if (states.containsKey(stateN)) {
                        states.get(stateN).addMapping(input, nextState);
                    } else {
                        //add new state
                        State currState = new State(input, nextState);
                        states.put(stateN, currState);
                        if (!initialStateTaken) {
                            initialState = currState;
                            initialStateTaken = true;
                        }
                    }
                }
            }
            reader.close();

    }

    /**
     * Running the user input through the mappings of the fsa.
     * @param userInput a shared reader stream
     * @throws IOException
     */
    public void runFSA(BufferedReader userInput) throws IOException {
        //reset the input stream
        userInput.reset();
        State currState = initialState;

        int c;
        while ((c = userInput.read()) != 10) {

            char character = (char) c;
            Integer nextStateN = currState.nextState(character);
            //no such input
             if (nextStateN == null) {
                if (currState instanceof AcceptingState) {
                    readyToPass = true;
                }
                currState = new DeathState();
                break;
            }
            //no such state
            if (!states.containsKey(nextStateN)) {
                currState = new DeathState();
                break;
            }
            //get next state
            currState = states.get(nextStateN);

            if (currState instanceof AcceptingState) {
                userInput.mark(1000);
            }

        }

        if (currState instanceof AcceptingState) {
            accepting = true;
        }else if(currState instanceof DeathState){
            accepting = false;
        }
    }
    public void resetReadyToPass(){
        readyToPass = false;
    }

    public boolean isAccepting() {
        return accepting;
    }
}
