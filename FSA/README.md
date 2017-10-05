#Finite state automaton interpreter 

##Design 
I started my practical by roughly defining my classes, their fields and functionalities: 
 - State class - keeps all the mapping from this state to another  
 - FSA class – reads the description, saves the rules into convenient data structures and compute the input 
 - Accepting state and Death state to control the end state of the machine 
 - Exception – WrongFormat (thrown when the extension of the file isn’t “.fsa”) 

My first idea was to use ArrayList to hold the states of the automaton. As the states are numbered I wanted to use the indexing of the array to make the links between them. This proved to be hard to implement as I kept getting a IndexOutOfBoundsException, when trying to put a new state at a certain index.  

I also considered using graphs, but the only solution I came up with to represent them was with two ArrayLists: one for storing the nodes (states) and one for the edges (Point objects). This didn’t seem a good choice as the lookup for edges would be linear and rather inefficient. 

Another design I come up with was to implement a custom LinkedList, where the nodes are the states of the automaton and a HashMap maps the input to the next state. I found it difficult to populate the list as I had to either keep handles to all states in an ArrayList or to search for two states at each line of FSA description. 

My final solution is to use a HashMap for the states in the automaton and another one in the states for the transition between the states. The flaws of this implementation are the rugged way to get a handle on the initial state and the extra space taken by the HashMap. 

The problem with the initial state comes from the assumption that all FSA have initial state “1”. I didn’t want to make the program dependent on that so I used a flag to get the first state from the transition table, which I think is better. 
I read the description file in my FSA class and set it up in the constructor. I use a BufferedReader to read the file line by line and extract the mappings and interpret the behaviour of the FSA.  
 
##Extension 
I added the functionality to compose FSA descriptions by making one machine to “lead into” another. The transition from one machine to another proved to be challenging so I used the following diagram to help me with the implementation.  
  
The transition from one machine to another happens at an accepting state of the first one. If the following input doesn’t map to the current FSA, then the input stream and the current char need to pass to the next machine. I used the mark() and reset() of the BufferedReader class to implement this transition. I mark the stream every time the current machine ends at an accepting state and reset the stream every time I start a run thought a FSA. The only limitation of this implementation I can think of is that it can’t deal with non-deterministic compositions. For example, if the same input maps from an accepting state of a machine to another state in the same machine as well as from an initial state of the next FSA to a state of the last machine.  
  
I faced a few problems when trying to make the program work with multiple strings inputs. The program must reset the flags (accepting readyToPass) of a FSA object after finishing computing a certain string to ensure that the machine would work correctly on the next one. 
 
 
