// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 




import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
  // Instance variables **********************************************

  /**
   * The interface type variable. It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  String logIn;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host     The server to connect to.
   * @param port     The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String logIn, String host, int port, ChatIF clientUI)
    throws IOException {
    super(host, port); // Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();

    sendToServer("#login " + logIn);
  }

  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {

    clientUI.display(msg.toString());

  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message) {
 
      // turing the mesg into a String
      String messaage = message.toString();

      // splitting the message at the spcaes so I have accsess to the command and the
      // possile outputs
      String[] list_message = messaage.split(" ");

      // accsessing the command through indexing
      String command = list_message[0];

      // checking if the first element in the string is a # which signals that it is a
      // command
      if (messaage.charAt(0) == '#') {
        if (command.equals("#quit")) {
          // calling the quit comand
          quit();
        }

        if (command.equals("#logoff")) {
          // putting a try statment cause the closeCOnnection method catches errors so I
          // have to put it
          try {
            closeConnection();
          } catch (IOException e) {
          }
        }

        if (command.equals("#sethost")) {
          // chekcing if the client and server are not connected
          if (!isConnected()) {
            // if not connected I then index for the host (index at 1) if there is no index
            // at 1 catch that error and tel them they must put in a host name
            try {
              setHost(list_message[1]);
            } catch (IndexOutOfBoundsException e) {
              System.out.println(
                  "If you are calling setHost you must enter a proper host name after the command call sperated by a space");
            }
          }
          // if the client is not logged off an error will occur
          else {
            connectionException(new Exception("Client must be logged off to set a new host"));
          }

        }

        if (command.equals("#setport")) {
          // chekcing if the client and server are not connected
          if (!isConnected()) {
            // if not connected I then index for the port (index at 1) if there is no index
            // at 1 catch that error and tel them they must put in a port
            try {
              if (Integer.parseInt(list_message[1]) < 0 || Integer.parseInt(list_message[1]) > 65535) {
                throw new IllegalArgumentException("Port must be a number between 0 and 65535");
              }
              setPort(Integer.parseInt(list_message[1]));
            } catch (IndexOutOfBoundsException e) {
              System.out.println(
                  "If you are calling setPort you must enter a proper port after the command call sperated by a space");
            } catch (NumberFormatException e) {
              System.out.println("Must enter number between 0 and 65535");
            }
          }
          // if the client is not logged off an error will occur
          else {
            connectionException(new Exception("Client must be logged off to set a new port"));
          }

        }

        // starts a new connection if there is not already a client connected
        if (command.equals("#login")) {
          if (!isConnected()) {
            try {
              openConnection();
            } catch (IOException e) {
            }
          } else {
            connectionException(new Exception("Client must not be connected to make a new connection"));
          }
        }

        if (command.equals("#gethost")) {
          System.out.println(getHost());
        }

        if (command.equals("#getport")) {
          System.out.println(getPort());
        }

      } 
      else {
        try{
        sendToServer(message);
        }
      
     
    catch (IOException e) {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  }

  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
    }
    System.exit(0);
  }

  // begging of my code

  // connectionClosed method

  protected void connectionClosed() {
    // connectionException(new Exception("cannot connect"));
    clientUI.display("Client quits");
    
  }
  // *
  // * @param exception
  // * the exception raised.
  // */
  protected void connectionException(Exception exception) {
    // System.out.println(exception);
    clientUI.display("Connection closed");
    quit();
  }


  protected void connectionEstablished() {
      try {
        sendToServer("#login " + logIn);
    } 
    catch (IOException e) {
        e.printStackTrace();
    }
  }

}
// End of ChatClient class
