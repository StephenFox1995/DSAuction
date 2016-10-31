package stephenfox.auction;

/**
 * Created by stephenfox on 31/10/2016.
 */
public interface AuctionExpiration {
  /**
   * Called when an auction item has expired.
   * */
  void expired();
}
