package stephenfox.auction;

/**
 * Created by stephenfox on 31/10/2016.
 * This interface is used to update bidders on new bids made.
 */
public interface Registrable {
  void auctionInfoMessage(String message);
}
