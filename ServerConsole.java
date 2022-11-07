import java.io.*;
import java.util.Scanner;

public class ServerConsole implements ChatIF {

    /**
     * Scanner to read from the console
     */
    Scanner fromConsole;

    EchoServer server;


    final public static int DEFAULT_PORT = 5555;

    public ServerConsole(int port) {
        server = new EchoServer(port, this);

        // Create scanner object to read from console
        fromConsole = new Scanner(System.in);
    }

    /**
     * This method waits for input from the console. Once it is
     * received, it sends it to the client's message handler.
     */
    public void accept() {
        try {
            String message;

            while (true) {
                message = fromConsole.nextLine();
                server.handleMessageFromServerUI(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    public void display(String message) {
      if(message.charAt(0) == '#') {
        return;
      }
        System.out.println("SERVER MSG> " + message);
    }

    public static void main(String[] args) 
    {
        int port; //Port to listen on
    
        try{
          port = Integer.parseInt(args[0]);
          if(port < 0 || port > 65535){
            throw new IllegalArgumentException("Port number must be between 0 and 65535");
          }
        }
        catch(ArrayIndexOutOfBoundsException e){
          port = DEFAULT_PORT;
        }
        catch(NumberFormatException e){
          port = DEFAULT_PORT;
          //throw new IllegalArgumentException("Port must be a number between 0 and 65535. Therefore defult port will be used");
          
        }
        // catch(Throwable t)
        // {
        //   port = DEFAULT_PORT; //Set port to 5555
        // }
        
        ServerConsole chater = new ServerConsole(port);
        
        try 
        {
          chater.server.listen();
        } 
        catch (IOException e)         {
          System.out.println("ERROR - Could not listen for clients!");
        }
        chater.accept();
      }



}
