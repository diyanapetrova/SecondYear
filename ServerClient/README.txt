Simple client-server system that serves text-only pages.

Execution:
Server:
java Server <portToSetUp> <directoriesToLookUp> <directoriesToLookUp>  ...
Can handle only port arguments, only directory/ies and both. Things that are not specified take the default values:
port 12344 and the user directory.

Client:
java Client

Protocol:

Server commands:
exit - at any point
disconnect - only if there is connected client

Client session:
-*-start state
output
    Waiting for:
    1) hostname/address
    2) port //wrong format -> default value
input
    hostname/address
    port
<connecting to a server>//error with socket or busy server go back to start state
-*-service state
    <filename>, list        or      disconnect
    <serverResponse>                <disconnectingFromServer>
    //go to service state           //go to start state
Can exit the program at any point with exit.
