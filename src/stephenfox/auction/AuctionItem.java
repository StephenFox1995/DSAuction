package stephenfox.auction;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class AuctionItem implements Auctionable {

  private double basePrice = 0;
  private double auctionPrice = 0;
  private String name = null;
  private Timer timer = null;
  private boolean expired = false;

  public AuctionItem(String name, double basePrice, double auctionPrice) {
    this.setName(name);
    this.basePrice = basePrice;
    this.auctionPrice = auctionPrice;
    this.timer = new Timer(name, false);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public double getAuctionPrice() {
    return this.auctionPrice;
  }

  @Override
  public double getBasePrice() {
    return this.basePrice;
  }

  @Override
  public void increaseAuctionPrice(double amount) throws AuctionPriceException {
    if (amount < this.auctionPrice) {
      throw new AuctionPriceException("New auction price: " + amount +
              " cannot be lower than the previous auction price: " + this.auctionPrice);
    } else {
      this.auctionPrice = amount;
    }
  }

  @Override
  public boolean hasExpired() {
    return this.expired;
  }

  @Override
  public void auction(AuctionExpiration expirationCallback) {
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        AuctionItem.this.expired = true;
        expirationCallback.expired();
      }
    }, 60 * 1 * 1000, 0);
  }
}
