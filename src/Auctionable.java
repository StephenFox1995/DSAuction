
/**
 * Created by stephenfox on 31/10/2016.
 */
public interface Auctionable {
  void setName(String name);
  String getName();
  void setHighestBidder(Bidder bidder); // Set the current highest bidder.
  Bidder getHighestBidder();
  double getBasePrice();
  double getAuctionPrice();
  void increaseAuctionPrice(double amount) throws AuctionPriceException;
  void enterIntoAuctionWithAuctioneer(AuctionTimeUpdate expirationCallback);
}
