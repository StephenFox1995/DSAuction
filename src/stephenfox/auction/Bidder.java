package stephenfox.auction;

import stephenfox.tcp.ClientHandler;
import stephenfox.tcp.Server;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Bidder implements Registrable {

  private ClientHandler clientHandler;
  private String name;
  private boolean joined = false;

  /**
   * Constructs a new instance with reference to a ClientHandler object.
   * The ClientHandler object will be messaged, when new information about the current enterIntoAuctionWithAuctioneer is announced
   * from the auctioneer.
   * @param clientHandler The client handler object.
   * */
  public Bidder(ClientHandler clientHandler) {
    this.clientHandler = clientHandler;
  }

  /**
   * Parses incoming messages from clients and performs appropriate action.
   * @param message The message that was send from the client.
   * */
  public void handleClientMessage(String message) {
    System.out.println("New message received from client: " + message);
    Auctioneer auctioneer = Auctioneer.sharedInstance();

    if (message.contains(Server.ServerCommandMessages.CLIENT_JOIN_AUCTION_COMMAND)) {
      // Register with auctioneer.
      auctioneer.registerBidder(this);
      joined = true;
      return;
    }
    else if (message.equals("help")) {
      String helpMessage = "\nhelp menu\n" +
              "exit : Exit the current auction.\n" +
              "join : Join the current auction.\n" +
              "bid : Bid a new amount. Use like the following: 'bid 400' to make a bid for 400.\n" +
              "setname : Sets a new name for the bidder. Use like the following: 'setname stephen' to set bidder name as stephen." +
              "help: Help instructions.";
      auctionInfoMessage(helpMessage);
      return;
    }

    if(!joined) { // Make sure the bidder is joined before they can make any command below.
      auctionInfoMessage("Please 'join' to use that command.");
      return;
    }

    if (message.equals(Server.ServerCommandMessages.CLIENT_EXIT)) {
      auctioneer.removeBidder(this);
      // Tell the client handler managing the connection for this class to close the connection.
      clientHandler.closeConnection();
    }
    else if (message.contains(Server.ServerCommandMessages.CLIENT_BID_COMMAND)) {
      String[] commandSplit = message.split(" ");
      String bidAmountString;
      double bidAmount;
      if (commandSplit.length > 1) {
        bidAmountString = commandSplit[1];
        try {
          bidAmount = Double.parseDouble(bidAmountString);
        } catch (NumberFormatException e) {
          auctionInfoMessage(Server.ServerCommandMessages.INVALID_BID_FORMAT_COMMAND);
          return;
        }
      }
      else {
        auctionInfoMessage(Server.ServerCommandMessages.INVALID_BID_FORMAT_COMMAND);
        return;
      }
      try {
        auctioneer.newBid(this, bidAmount);
      } catch (AuctionPriceException e) {
        auctionInfoMessage(e.getMessage());
        return;
      }
    }
    else if (message.contains(Server.ServerCommandMessages.CLIENT_SET_NAME_COMMAND)) {
      String[] commandSplit = message.split(" ");
      if (commandSplit.length > 1) {
        String oldName = name;
        setName(commandSplit[1]);
        auctionInfoMessage(oldName + " has been renamed to " + name);
      }
      else {
        auctionInfoMessage(Server.ServerCommandMessages.INVALID_BID_FORMAT_COMMAND);
        return;
      }
    }
    else {
      auctionInfoMessage(Server.ServerCommandMessages.UNKNOWN_COMMAND);
    }
  }


  /// Registrable interface.
  @Override
  public void auctionInfoMessage(String message) {
    // Message the client of new info about the enterIntoAuctionWithAuctioneer.
    this.clientHandler.messageClient(message);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
