package factories;

import models.Offer;
import java.math.BigDecimal;

/**
 * Factory class for creating instances of Offer.
 */
public class OfferFactory {

    // Private constructor to prevent instantiation
    private OfferFactory() {
        throw new UnsupportedOperationException("OfferFactory is a utility class and cannot be instantiated.");
    }
    
    /**
     * Create a new empty Offer instance.
     *
     * @return A new Offer object.
     */
    public static Offer createOffer() {
        return new Offer();
    }

    /**
     * Create a new Offer instance with the specified attributes.
     *
     * @param offerId          The unique identifier for the offer.
     * @param productId        The unique identifier of the product being offered.
     * @param buyerId          The unique identifier of the buyer making the offer.
     * @param offerPrice       The price proposed in the offer.
     * @param offerStatus      The current status of the offer.
     * @param reason           The reason for the offer's current status, if applicable.
     * @return                 A new Offer object populated with the provided details.
     */
    public static Offer createOffer(String offerId, String productId, String buyerId, BigDecimal offerPrice, String offerStatus, String reason) {
        return new Offer(offerId, productId, buyerId, offerPrice, offerStatus, reason);
    }
}
