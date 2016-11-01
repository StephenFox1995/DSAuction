package stephenfox.tcp;

import java.io.IOException;
import java.net.*;


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
   * @param port The port to listen on.
   */
  public void beginListening(int port) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Listening on port: " + port);
    } catch (IOException e) {
      System.out.println("Unable to open port: " + port + " " + e.getMessage());
      System.exit(1);
    }
    do {
      handleConnection(serverSocket);
    } while (true);
  }

  /**
   * Handle any incoming connection request from client
   * and set up ClientHandler to handle communications back to the client.
   *
   * @param serverSocket The server socket used to accept the connection.
   * */
  public void handleConnection(ServerSocket serverSocket) {
    try {
      Socket client = serverSocket.accept();
      ClientHandler clientHandler = new ClientHandler(client);
      new Thread(clientHandler).start(); // Start new thread for this connection.
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Private class to hold command messages that are used by the server.
   * */
  public static class ServerCommandMessages {
    public static final String SERVER_CLOSE = "**CLOSE**";
    public static final String UNKOWN_COMMAND = "Unknown Command";
  }
}
