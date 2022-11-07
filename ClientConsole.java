// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

// import client.*;
// import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String login, String host, int port) 
  {
    try 
    {
      client = new ChatClient(login, host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {

    int port;

    String host = "";

    String login = "";


    try{
      login = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e){
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
      System.exit(0);
    }

    try
    {
      host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    try{
      port = Integer.parseInt(args[2]);
      if(port < 0 || port > 65535){
        throw new IllegalArgumentException("Port number must be between 0 and 65535");
      }
    }
    catch(ArrayIndexOutOfBoundsException e){
      port = DEFAULT_PORT;
    }
    catch(NumberFormatException e){
      port = DEFAULT_PORT;
      throw new IllegalArgumentException("Port must be a number between 0 and 65535. Therefore defult port will be used");
      
    }

    ClientConsole chat = new ClientConsole(login, host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
