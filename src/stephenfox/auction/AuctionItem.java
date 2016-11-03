package stephenfox.auction;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class AuctionItem implements Auctionable {
  private Bidder highestBidder; // The current top highestBidder for the AuctionItem.
  private double basePrice = 0;
  private double auctionPrice = 0;
  private String name = null;
  private Timer timer = null;
  private int timerNotifications = 0; // When == 2, auction for this item has ended.



  public AuctionItem(String name, double basePrice, double auctionPrice) {
    this.setName(name);
    this.basePrice = basePrice;
    this.auctionPrice = auctionPrice;
    this.timer = new Timer(name, false);
  }


  @Override
  public void increaseAuctionPrice(double amount) throws AuctionPriceException {
    if (amount <= this.auctionPrice) {
      throw new AuctionPriceException("New enterIntoAuctionWithAuctioneer price: " + amount +
              " must be higher than the previous enterIntoAuctionWithAuctioneer price: " + this.auctionPrice);
    } else {
      this.auctionPrice = amount;
      // As new bid has been made restart the timer notifications.
      restartTimerNotifications();
    }
  }


  @Override
  public void enterIntoAuctionWithAuctioneer(AuctionTimeUpdate auctionTimeUpdate) {
    // Start the timer notifications so bidders are updated on time remaining.
    startTimerNotifications(auctionTimeUpdate);
  }


  private void restartTimerNotifications() {
    timerNotifications = 0;
  }

  private void startTimerNotifications(AuctionTimeUpdate auctionTimeUpdate) {
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        AuctionItem.this.timerNotifications += 1;
        if (AuctionItem.this.timerNotifications == 2) { // Message callback that time has expired.
          auctionTimeUpdate.expired();
        } else { // Message callback that 30 seconds has elapsed.
          auctionTimeUpdate.thirtySecondUpdate();
        }
      }
    }, 30 * 1 * 1000, 30 * 1 * 1000);
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
  public void setHighestBidder(Bidder bidder) {
    this.highestBidder = bidder;
  }

  @Override
  public Bidder getHighestBidder() {
    return highestBidder;
  }

  @Override
  public double getAuctionPrice() {
    return this.auctionPrice;
  }

  @Override
  public double getBasePrice() {
    return this.basePrice;
  }

}
