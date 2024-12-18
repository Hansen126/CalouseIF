package controllers;

import java.util.ArrayList;

import models.Wishlist;
import services.Response;

public class WishlistController {
    
    public WishlistController() {
        // Constructor reserved for future enhancements
    }
    
    /**
     * Retrieve the wishlist items for a specific user.
     * [BUYER]
     *
     * @param userId The unique identifier of the user.
     * @return       A Response object containing a list of Wishlist items.
     */
    public static Response<ArrayList<Wishlist>> getUserWishlist(String userId){
        return Wishlist.viewWishlist(userId);
    }
    
    /**
     * Add a new product to the user's wishlist.
     * [BUYER]
     *
     * @param productId The unique identifier of the product to add.
     * @param userId    The unique identifier of the user.
     * @return          A Response object containing the added Wishlist item.
     */
    public static Response<Wishlist> addItemToWishlist(String productId, String userId) {
        return Wishlist.addWishlistItem(productId, userId);
    }
    
    /**
     * Remove an item from the user's wishlist.
     * [BUYER]
     *
     * @param wishlistId The unique identifier of the wishlist item to remove.
     * @return           A Response object indicating the result of the removal.
     */
    public static Response<Wishlist> deleteWishlistItem(String wishlistId) {
        return Wishlist.removeWishlistItem(wishlistId);
    }

}
