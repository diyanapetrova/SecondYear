import java.util.HashMap;

/**
 * A class to represent a single state of the finite state automaton.
 */

public class State {
    private HashMap<Character, Integer> mapping;

    public State(char input, int nextState) {
        mapping = new HashMap<>();
        mapping.put(input, nextState);
    }

    /**
     * Constructor used for the DeadState.
     */
    public State() {
        mapping = new HashMap<>();
    }

    /**
     * Add a line of the transition table to the mappings in the HashMap.
     * @param input that leads to the next state
     * @param nextState the end point of the current transition
     */
    public void addMapping(char input, int nextState) {
        mapping.put(input, nextState);
    }

    /**
     * Looks up the index of the state which is mapped to the input.
     * @param input char that should map the current state to the nest one
     * @return the index or null if the input doesn't map to a particular state
     */
    public Integer nextState(char input) {
        if (mapping.containsKey(input))
            return mapping.get(input);
        else return null;
    }
}
