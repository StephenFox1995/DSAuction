package stephenfox.auction;

import stephenfox.tcp.ClientHandler;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Bidder implements Registrable {

  private ClientHandler clientHandler;


  public Bidder(ClientHandler clientHandler) {
    this.clientHandler = clientHandler;
  }

  @Override
  public void auctionInfoMessage(String message) {
    // Message the client of new info about the auction.
    this.clientHandler.messageClient(message);
  }
}
