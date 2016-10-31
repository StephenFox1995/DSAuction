package stephenfox.auction;

/**
 * Created by stephenfox on 31/10/2016.
 */
public interface Auctionable {
  String setName(String name);
  String getName();
  int getBasePrice();
  int getAuctionPrice();
  void increaseAuctionPrice(int amount);
}
