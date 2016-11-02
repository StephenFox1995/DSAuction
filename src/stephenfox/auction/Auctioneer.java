package stephenfox.auction;

import java.util.ArrayList;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Auctioneer {

  private static Auctioneer sharedInstance;
  private AuctionItem auctionItem;
  private ArrayList<Bidder> bidders;
  private String auctionInfo = "Welcome to the auction, type \"bid\" to make a new bid.\n";

  private Auctioneer() {
    this.bidders = new ArrayList<>();
    auctionItem = AuctionList.getRandomAuctionItem();
    AuctionExpiration expiration = () -> messageBidders("Bid has expired.");
    auctionItem.auction(expiration);
  }

  public static Auctioneer sharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new Auctioneer();
    }
    return sharedInstance;
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
    bidder.auctionInfoMessage(auctionInfo + "The current auction item is: " + auctionItem.getName());

    System.out.println("New bidder has joined the auction");
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
   * @param auctionItem The auction item currently bidding.
   * */
  public void newBid(AuctionItem auctionItem) {
    messageBidders("New bid made for item: " + auctionItem.getName() + " price: " + auctionItem.getAuctionPrice());
    this.auctionItem = auctionItem;
  }
  public AuctionItem getCurrentAuctionItem() {
    return auctionItem;
  }
}
