package factories;

import models.Item;
import java.math.BigDecimal;

/**
 * Factory class for creating instances of Item.
 */
public class ItemFactory {

    // Private constructor to prevent instantiation
    private ItemFactory() {
        throw new UnsupportedOperationException("ItemFactory is a utility class and cannot be instantiated.");
    }
    
    /**
     * Create a new empty Item instance.
     *
     * @return A new Item object.
     */
    public static Item createItem() {
        return new Item();
    }
    
    /**
     * Create a new Item instance with the specified attributes.
     *
     * @param itemId       The unique identifier for the item.
     * @param itemName     The name of the item.
     * @param itemSize     The size specification of the item.
     * @param itemPrice    The price of the item.
     * @param itemCategory The category to which the item belongs.
     * @param itemStatus   The current status of the item.
     * @param reason       The reason for the item's current status, if applicable.
     * @return             A new Item object populated with the provided details.
     */
    public static Item createItem(String itemId, String itemName, String itemSize, BigDecimal itemPrice, String itemCategory, String itemStatus, String reason) {
        return new Item(itemId, itemName, itemSize, itemPrice, itemCategory, itemStatus, reason);
    }
}
