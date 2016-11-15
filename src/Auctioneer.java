
import java.util.ArrayList;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Auctioneer implements AuctionTimeUpdate {

  private static Auctioneer sharedInstance;
  private AuctionItem auctionItem;
  private ArrayList<Bidder> bidders;
  private String auctionInfo = "To auction, type \"bid <amount>\" e.g. bid 400 to make a new bid.\n";


  private Auctioneer() {
    this.bidders = new ArrayList<>();
    newAuctionItem(); // Set new auction item on startup.
    auctionItem.enterIntoAuctionWithAuctioneer(this);
  }

  public static Auctioneer sharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new Auctioneer();
    }
    return sharedInstance;
  }

  @Override
  public void expired() {
    if (auctionItem.wasBidMade()) { // If there was a bid made, notify the bidders.
      messageBidders("Auction time has expired, " +
              auctionItem.getHighestBidder().getName() +
              " won that auction, with a highest bidding price of: " +
              auctionItem.getAuctionPrice());
      newAuctionItem();
      messageBidders("Auction item: " +
              auctionItem.getName() +
              " will now be auctioned, starting price is: " +
              auctionItem.getAuctionPrice());
    } else { // There was no bid made, re auction the item.
      messageBidders("Auction time has expired. Auction item: " +
              auctionItem.getName() +
              " will be re auctioned, starting price is: " +
              auctionItem.getAuctionPrice());
    }
    auctionItem.enterIntoAuctionWithAuctioneer(this);
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
    bidder.setName("bidder" + bidders.size()); // Set the name of the bidder to bidderX
    // Once the bidder has been registered, notify of the current AuctionItem.
    bidder.auctionInfoMessage("Welcome, you have been given the name: " + bidder.getName());
    bidder.auctionInfoMessage(auctionInfo + "The current auction item is: " + auctionItem.getName()
            + " starting price is: " + auctionItem.getAuctionPrice());
  }

  /**
   * Removes a bidder from the auction.
   * @param bidder The bidder to remove from the auction.
   * */
  public void removeBidder(Bidder bidder) {
    if (bidders.contains(bidder)) {
      bidders.remove(bidder);
    }
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
    auctionItem.increaseAuctionPrice(amount);
    auctionItem.setHighestBidder(bidder);
    messageBidders("New bid made for item: " +
            auctionItem.getName() +
            " price: " +
            auctionItem.getAuctionPrice() +
            " by bidder " + bidder.getName() +
            ". Auction time reset to 1 minute.");
  }
}
