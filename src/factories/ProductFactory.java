package factories;

import models.Product;

/**
 * Factory class for creating instances of Product.
 */
public class ProductFactory {

    // Private constructor to prevent instantiation
    private ProductFactory() {
        throw new UnsupportedOperationException("ProductFactory is a utility class and cannot be instantiated.");
    }
    
    /**
     * Create a new empty Product instance.
     *
     * @return A new Product object.
     */
    public static Product createProduct() {
        return new Product();
    }
    
    /**
     * Create a new Product instance with the specified attributes.
     *
     * @param productId The unique identifier for the product.
     * @param itemId    The unique identifier of the item associated with the product.
     * @param sellerId  The unique identifier of the seller offering the product.
     * @return          A new Product object populated with the provided details.
     */
    public static Product createProduct(String productId, String itemId, String sellerId) {
        return new Product(productId, itemId, sellerId);
    }
}
