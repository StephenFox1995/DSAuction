package stephenfox.auction;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class AuctionItem implements Auctionable {

  private int basePrice = 0;
  private int auctionPrice = 0;
  private String name = null;

  public AuctionItem(String name, int basePrice, int auctionPrice) {
    this.setName(name);
    this.basePrice = basePrice;
    this.auctionPrice = auctionPrice;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String setName(String name) {
    return this.name;
  }

  @Override
  public int getAuctionPrice() {
    return this.auctionPrice;
  }

  @Override
  public int getBasePrice() {
    return this.basePrice;
  }

  @Override
  public void increaseAuctionPrice(int amount) {
    this.auctionPrice = this.auctionPrice + amount;
  }
}
