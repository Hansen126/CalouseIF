package factories;

import models.Wishlist;

/**
 * Factory class for creating instances of Wishlist.
 */
public class WishlistFactory {

    // Private constructor to prevent instantiation
    private WishlistFactory() {
        throw new UnsupportedOperationException("WishlistFactory is a utility class and cannot be instantiated.");
    }
    
    /**
     * Create a new empty Wishlist instance.
     *
     * @return A new Wishlist object.
     */
    public static Wishlist createWishlist() {
        return new Wishlist();
    }
    
    /**
     * Create a new Wishlist instance with the specified attributes.
     *
     * @param wishlistId The unique identifier for the wishlist.
     * @param productId  The unique identifier of the product added to the wishlist.
     * @param userId     The unique identifier of the user who owns the wishlist.
     * @return           A new Wishlist object populated with the provided details.
     */
    public static Wishlist createWishlist(String wishlistId, String productId, String userId) {
        return new Wishlist(wishlistId, productId, userId);
    }
}
