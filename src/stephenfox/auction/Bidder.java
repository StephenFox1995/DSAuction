package stephenfox.auction;

import stephenfox.tcp.ClientHandler;
import stephenfox.tcp.Server;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Bidder implements Registrable {

  private ClientHandler clientHandler;
  private final String CLIENT_BID_MESSAGE = "bid";

  /**
   * Constructs a new instance with reference to a ClientHandler object.
   * The ClientHandler object will be messaged, when new information about the current auction is anounced
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
    Auctioneer auctioneer = Auctioneer.sharedInstance();

    if (message.contains(CLIENT_BID_MESSAGE)) {
      AuctionItem item = auctioneer.getCurrentAuctionItem();
      item.increaseAuctionPrice(29.0);
      auctioneer.newBid(item);
    } else {
      auctionInfoMessage(Server.ServerCommandMessages.UNKOWN_COMMAND);
    }
  }


  /// Registrable interface.
  @Override
  public void auctionInfoMessage(String message) {
    // Message the client of new info about the auction.
    this.clientHandler.messageClient(message);
  }

}
