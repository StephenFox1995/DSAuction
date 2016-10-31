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

  public void accessServer(InetAddress host, int port) {
    Socket socket = null;
    try {
      socket = new Socket(host, port);
      System.out.println("Connect to server on port: " + port);
      Scanner input = new Scanner(socket.getInputStream());
      PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

      // Get input from keyboard.
      Scanner scanner = new Scanner(System.in);
      String message, response;

      do {
        System.out.print("Enter message: ");
        message = scanner.nextLine();
        output.println(message);
        response = input.nextLine();
        System.out.println(response.toString());
      } while (true);

    } catch(IOException e) {
      System.out.println("Could not connect to server.");
    }
    finally {
      try {
        socket.close();
      } catch (IOException e) { System.exit(1); }
    }
  }
}
