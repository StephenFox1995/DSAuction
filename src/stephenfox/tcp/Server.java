package stephenfox.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Server {
  private static Server sharedServer;

  /**
   * Returns an instance of Server that is shared across the application
   *
   * @return Server - Shared Server instance.
   */
  public static Server sharedServer() {
    if (sharedServer == null) {
      sharedServer = new Server();
    }
    return sharedServer;
  }


  /**
   * Begins the server to listen on the specified port.
   */
  public void beginListening(int port) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
      System.out.print("Listening on port: " + port);
    } catch (IOException e) {
      System.out.print("Unable to open port: " + port + " " + e.getMessage());
      System.exit(1);
    }
    do {
      handleConnection(serverSocket);
    } while (true);
  }


  public void handleConnection(ServerSocket serverSocket) {
    Socket socket = null;
    try {
      socket = serverSocket.accept();
      Scanner input = new Scanner(socket.getInputStream());
      String message = input.nextLine();
      PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

      while (!message.equals("**CLOSE**")) {
        output.println(message);
        output.println("Hello Bob!");
        message = input.nextLine();
      }
      output.println("Bye Bob!");

    } catch (IOException e) {

    } finally {
      try {
        socket.close();
      } catch (IOException e) { }
    }
  }
}
