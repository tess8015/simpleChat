import java.io.IOException;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 




/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  ChatIF serverUI;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) {
    super(port);
    this.serverUI = serverUI;
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    String[] msg_list = msg.toString().split(" ");
    if(msg.toString().charAt(0) == '#') {
      if(msg_list[0].equals("#login")){
        if(client.getInfo("loginID") == null){
          client.setInfo("loginID", msg_list[1]);
        }
        else{
          try{
          client.close();
          }
          catch(IOException e){}
        }
      }
      System.out.println("Message received, login ID is " + msg_list[1] + " from null");
      clientConnected(client);
    }
    else{
      System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
      this.sendToAllClients(client.getInfo("loginID") + "> " + msg);
    }
    
  }

  public void handleMessageFromServerUI(String message) {

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
        System.exit(0);
      }
      if (command.equals("#stop")) {
        // calling the quit comand
        stopListening();
      }
      if (command.equals("#close")) {
        // calling the quit comand
        try {
          close();
        } 
        catch(IOException e){}

      }
      if (command.equals("#setport")) {
        if (getNumberOfClients() == 0 && isListening() == false) {
          super.setPort(Integer.parseInt(list_message[1]));
        }
      }
      if (command.equals("#start")) {
        // calling the quit comand
        if (isListening() == false) {
          serverStarted();
        }
      }

    }

    else { // message is not a command
      this.sendToAllClients("SERVER MESSAGE> " + message);
      serverUI.display(message);
    }

  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
    System.out.println("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    System.out.println("Server has stopped listening for connections.");
  }

  synchronized protected void clientDisconnected(ConnectionToClient client) {

    sendToAllClients("client " + client.getInfo("loginID") + " disconnected" );
    System.out.println("client " + client.getInfo("loginID") + " logged on");
   
  }

  protected void clientConnected(ConnectionToClient client) {
    if(client.getInfo("loginID") == null){
      System.out.println("A new client has connected to the server");
    }
    else{
    sendToAllClients("client " + client.getInfo("loginID") + " logged on" );
    System.out.println("client " + client.getInfo("loginID") + " logged on");
   
    }

  }
}

// Class methods ***************************************************

/**
 * This method is responsible for the creation of
 * the server instance (there is no UI in this phase).
 *
 * @param args[0] The port number to listen on. Defaults to 5555
 *                if no argument is entered.
 */
// public static void main(String[] args)
// {
// int port = 0; //Port to listen on

// try
// {
// port = Integer.parseInt(args[0]); //Get port from command line
// }
// catch(Throwable t)
// {
// port = DEFAULT_PORT; //Set port to 5555
// }

// EchoServer sv = new EchoServer(port);

// try
// {
// sv.listen(); //Start listening for connections
// }
// catch (Exception ex)
// {
// System.out.println("ERROR - Could not listen for clients!");
// }
// }
// }
// End of EchoServer class
