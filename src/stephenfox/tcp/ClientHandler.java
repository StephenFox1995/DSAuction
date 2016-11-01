package stephenfox.tcp;

import stephenfox.auction.AuctionItem;
import stephenfox.auction.Auctioneer;
import stephenfox.auction.Bidder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class ClientHandler implements Runnable {
  private Socket socket;
  private Scanner input;
  private PrintWriter output;
  private Bidder bidder;
  private String outputMessage = "";

  public ClientHandler(Socket socket) {
    this.socket = socket;
    try {
      this.input = new Scanner(this.socket.getInputStream());
      this.output = new PrintWriter(this.socket.getOutputStream(), true);
      this.bidder = new Bidder(this);
    } catch (IOException e) {
      System.out.println("Error trying to setup communication with client.\n");
      try {
        socket.close();
      } catch (IOException err) {
        System.out.println("Error closing socket: " + err.getMessage());
      }
    }
  }

  /**
   * Exposes a way to communicate back to a client machine.
   * @param message The message to send back to the client.
   *
   * */
  public void messageClient(String message) {
    outputMessage = message;
  }

  @Override
  public void run() {
    String inputMessage = input.nextLine();
    // Register with auctioneer.
    Auctioneer auctioneer = Auctioneer.sharedInstance();
    // Create new Bidder instance to represent this client in auction.
    auctioneer.registerBidder(bidder);

    // New thread for this client.
    while (!inputMessage.equals(Server.ServerCommandMessages.SERVER_CLOSE)) {
      // Send output message back to client.
      output.println(outputMessage);
      outputMessage = "";
      inputMessage = input.nextLine();
      bidder.handleClientMessage(inputMessage);

    }
    output.println("Bye Client!");
  }
}
