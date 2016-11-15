
/**
 * Created by stephenfox on 31/10/2016.
 */
public interface AuctionTimeUpdate {
  /**
   * Called when an enterIntoAuctionWithAuctioneer item has expired.
   * */
  void expired();
  void thirtySecondUpdate(); // Called every time thirty seconds passes in the enterIntoAuctionWithAuctioneer.

}
