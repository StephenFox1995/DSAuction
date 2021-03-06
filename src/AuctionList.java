
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class AuctionList {
  private static Object[][] items = {
          {"Computer", 375.00},
          {"iPhone", 503.35},
          {"Laptop", 211.96},
          {"Chair", 29.02},
          {"Door", 99.09},
          {"Table", 100.09},
	  {"Mona Lisa", 2300.00},
	  {"Toy Car", 10}
  };

  public static AuctionItem getRandomAuctionItem() {
    Random rand = new Random();
    int x = rand.nextInt(5);
    String name = (String)items[x][0];
    double price = (double)items[x][1];
    AuctionItem item = new AuctionItem(name, price, price);
    return item;
  }
}
