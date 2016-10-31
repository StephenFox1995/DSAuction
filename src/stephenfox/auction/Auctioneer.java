package stephenfox.auction;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Auctioneer {

  private static Auctioneer sharedInstance;
  private AuctionItem auctionItem;

  public static Auctioneer sharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new Auctioneer();
    }
    return sharedInstance;
  }

  /**
   * Returns the current item being auctioned.
   * */
  public AuctionItem currentAuctionItem() {
    return AuctionList.getRandomAuctionItem();
  }
}
