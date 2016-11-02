package stephenfox.auction;

import java.util.ArrayList;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Auctioneer implements AuctionTimeUpdate {

  private static Auctioneer sharedInstance;
  private AuctionItem auctionItem;
  private ArrayList<Bidder> bidders;
  private String auctionInfo = "Welcome to the auction, type \"bid\" to make a new bid.\n";


  private Auctioneer() {
    this.bidders = new ArrayList<>();
    newAuctionItem(); // Set new auction item on startup.
    auctionItem.enterIntoAuction(this);
  }

  public static Auctioneer sharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new Auctioneer();
    }
    return sharedInstance;
  }

  @Override
  public void expired() {
    messageBidders("Auction time has expired");
    newAuctionItem();
    messageBidders("The current auction item is: " + auctionItem.getName()
            + " starting price is: " + auctionItem.getAuctionPrice());
    auctionItem.enterIntoAuction(this);
  }

  @Override
  public void thirtySecondUpdate() {
    messageBidders("30 seconds left in this auction.");
  }

  private void newAuctionItem() {
    auctionItem = null;
    auctionItem = AuctionList.getRandomAuctionItem();
  }

  /**
   * Registers a new Bidder into the auction.
   * @param bidder A new bidder to registerBidder to the auction.
   *               A bidder can only be registered once to the auction.
   * */
  public void registerBidder(Bidder bidder) {
    if (bidders.contains(bidder)) {
      bidder.auctionInfoMessage("Bidder already in auction");
      return;
    }
    bidders.add(bidder);
    // Once the bidder has been registered, notify of the current AuctionItem.
    bidder.auctionInfoMessage(auctionInfo + "The current auction item is: " + auctionItem.getName()
            + " starting price is: " + auctionItem.getAuctionPrice());
  }

  /**
   * Messages all bidders.
   * @param message The message to send to the bidder.
   * */
  private void messageBidders(String message) {
    for (Bidder b : bidders) {
      b.auctionInfoMessage(message);
    }
  }

  /**
   * Makes a new bid for the current Auction item.
   * @param bidder The bidder.
   * @param amount The new bid amount for item currently bidding.
   * */
  public void newBid(Bidder bidder, double amount) throws AuctionPriceException {
    this.auctionItem.increaseAuctionPrice(amount);
    messageBidders("New bid made for item: " + auctionItem.getName() + " price: " + auctionItem.getAuctionPrice() + ". Auction time reset to 1 minute.");
  }
}
