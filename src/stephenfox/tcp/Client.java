package stephenfox.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.*;
import java.util.Scanner;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Client {

  private Scanner serverResponse;
  private PrintWriter output;
  private Scanner keyboardScanner;


  public void accessServer(InetAddress host, int port) {
    Socket socket = null;
    try {
      // Connect to server.
      socket = new Socket(host, port);
      System.out.println("Connect to server on port: " + port);

      serverResponse = new Scanner(socket.getInputStream());
      output = new PrintWriter(socket.getOutputStream(), true);

      // Get input from keyboard.
      keyboardScanner = new Scanner(System.in);
      while (true) {
        System.out.print("Enter message: ");
        output.println(keyboardScanner.nextLine()); // Send message to server.
        System.out.println(serverResponse.nextLine());
      }
    } catch(IOException e) {
      System.out.println("Could not connect to server.");
    }
    finally {
      try {
        socket.close();
      } catch (IOException e) {
        System.out.println("Error setting up socket.");
      } catch (NullPointerException e) {
        System.out.println("Error setting up socket.");
      }
    }
  }
}
