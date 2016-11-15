
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


/**
 * Created by stephenfox on 31/10/2016.
 */
public class Server {
  private static Server sharedServer;
  private ArrayList<ClientHandler> clients;

  private Server() {
    clients = new ArrayList<>();
  }

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
    while(true) {
      handleConnection(serverSocket);
    }
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
      clients.add(clientHandler);
      new Thread(clientHandler).start(); // Start new thread for this connection.
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Private class to hold command messages that are used by the server.
   * */
  public static class ServerCommandMessages {
    public static final String CLIENT_EXIT = "exit";
    public static final String UNKNOWN_COMMAND = "Unknown Command";
    public static final String INVALID_BID_FORMAT_COMMAND = "Unsupported format, expected: bid 00.00 got bid";
    public static final String CLIENT_BID_COMMAND = "bid"; // Command used to make new bid.
    public static final String CLIENT_SET_NAME_COMMAND = "setname"; // Command used to set the name for the client.
    public static final String CLIENT_JOIN_AUCTION_COMMAND = "join"; // Command used to join the enterIntoAuctionWithAuctioneer.
  }
}
