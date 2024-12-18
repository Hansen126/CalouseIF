package models;

import java.util.ArrayList;

import factories.WishlistFactory;
import services.Response;
import utilities.IdGenerator;

public class Wishlist extends Model {
    
    private String wishlistId;
    private String productId;
    private String userId;
    
    private final String TABLE_NAME = "wishlists";
    private final String PRIMARY_KEY = "wishlistId";
    
    public Wishlist() {
        // Default constructor for future use
    }
    
    public Wishlist(String id, String productId, String userId) {
        super();
        this.wishlistId = id;
        this.productId = productId;
        this.userId = userId;
    }
    
    /**
     * Retrieve all wishlists associated with a specific user.
     * [BUYER]
     *
     * @param userId The unique identifier of the user.
     * @return       A Response object containing the list of Wishlists or an error message.
     */
    public static Response<ArrayList<Wishlist>> viewWishlist(String userId) {
        Response<ArrayList<Wishlist>> response = new Response<>();
        
        try {
            ArrayList<Wishlist> wishlistList = WishlistFactory.createWishlist().findWishlists("userId", "=", userId);
            
            response.setMessage("Success: Retrieved all wishlists.");
            response.setSuccess(true);
            response.setData(wishlistList);
            return response;
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }

    /**
     * Add a new item to the user's wishlist.
     * [BUYER]
     *
     * @param productId The unique identifier of the product to add.
     * @param userId    The unique identifier of the user.
     * @return          A Response object containing the newly created Wishlist or an error message.
     */
    public static Response<Wishlist> addWishlistItem(String productId, String userId) {
        Response<Wishlist> response = new Response<>();
        
        try {
            // Generate a new wishlist ID
            String newWishlistId = IdGenerator.generateNewId(WishlistFactory.createWishlist().getLatestWishlist().getId(), "WISH");
            
            // Create and insert the new wishlist into the database
            Wishlist wishlist = WishlistFactory.createWishlist(newWishlistId, productId, userId);
            wishlist.insertWishlist();
            
            response.setMessage("Success: Wishlist item added.");
            response.setSuccess(true);
            response.setData(wishlist);
            return response;
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }

    /**
     * Remove an item from the user's wishlist.
     * [BUYER]
     *
     * @param wishlistId The unique identifier of the wishlist item to remove.
     * @return           A Response object indicating the success or failure of the removal.
     */
    public static Response<Wishlist> removeWishlistItem(String wishlistId) {
        Response<Wishlist> response = new Response<>();
        
        try {
            Boolean isDeleted = WishlistFactory.createWishlist().deleteWishlist(wishlistId);
            
            if (!isDeleted) {
                response.setMessage("Error: Failed to delete wishlist item.");
                response.setSuccess(false);
                response.setData(null);
                return response;                
            }
            
            response.setMessage("Success: Wishlist item removed.");
            response.setSuccess(true);
            response.setData(null);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }

    // Getters and Setters
    
    
    
    public String getId() {
        return wishlistId;
    }
    
    public String getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(String wishlistId) {
		this.wishlistId = wishlistId;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public String getPRIMARY_KEY() {
		return PRIMARY_KEY;
	}

	public void setId(String id) {
        this.wishlistId = id;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }
    
    @Override
    protected String getPrimaryKey() {
        return PRIMARY_KEY;
    }
    
    /**
     * Retrieve the User associated with this Wishlist.
     *
     * @return The User who owns this Wishlist.
     */
    public User getUser() {
        return this.hasOne(User.class, "users", this.getUserId(), "userId");
    }
    
    /**
     * Retrieve the Product associated with this Wishlist.
     *
     * @return The Product added to the Wishlist.
     */
    public Product getProduct() {
        return this.hasOne(Product.class, "products", this.getProductId(), "productId");
    }
    
    /**
     * Retrieve all Wishlists.
     *
     * @return A list of all Wishlists.
     */
    public ArrayList<Wishlist> getAllWishlists() {
        Response<ArrayList<Wishlist>> response = super.fetchAll(Wishlist.class);
        return response.getData();
    }
    
    /**
     * Retrieve Wishlists based on a specific condition.
     *
     * @param columnName The column to filter by.
     * @param operator   The operator for comparison (e.g., '=', '>', '<').
     * @param key        The value to compare against.
     * @return           A list of Wishlists matching the condition.
     */
    public ArrayList<Wishlist> findWishlists(String columnName, String operator, String key) {
        Response<ArrayList<Wishlist>> response = super.findWhere(Wishlist.class, columnName, operator, key);
        return response.getData();
    }
    
    /**
     * Update an existing Wishlist.
     *
     * @param fromKey The primary key value identifying the Wishlist to update.
     * @return        The updated Wishlist.
     */
    public Wishlist updateWishlist(String fromKey) {
        Response<Wishlist> response = super.updateRecord(Wishlist.class, fromKey);
        return response.getData();
    }
    
    /**
     * Insert a new Wishlist into the database.
     *
     * @return The inserted Wishlist.
     */
    public Wishlist insertWishlist() {
        Response<Wishlist> response = super.insertRecord(Wishlist.class);
        return response.getData();
    }
    
    /**
     * Find a Wishlist by its primary key.
     *
     * @param fromKey The primary key value of the Wishlist.
     * @return        The found Wishlist.
     */
    public Wishlist findWishlist(String fromKey) {
        Response<Wishlist> response = super.findByPrimaryKey(Wishlist.class, fromKey);
        return response.getData();
    }
    
    /**
     * Retrieve the most recently added Wishlist.
     *
     * @return The latest Wishlist.
     */
    public Wishlist getLatestWishlist() {
        Response<Wishlist> response = super.fetchLatest(Wishlist.class);
        return response.getData();
    }
    
    /**
     * Delete a Wishlist from the database.
     *
     * @param fromKey The primary key value of the Wishlist to delete.
     * @return        True if deletion was successful, else False.
     */
    public Boolean deleteWishlist(String fromKey) {
        Response<Boolean> response = super.deleteRecord(Wishlist.class, fromKey);
        return response.getData();
    }
    
    /**
     * Retrieve Wishlists where the specified column's value is within the provided list.
     *
     * @param columnName The column to filter by.
     * @param listValues The list of values to include in the IN clause.
     * @return           A list of Wishlists matching the criteria.
     */
    public ArrayList<Wishlist> findWishlistsIn(String columnName, ArrayList<String> listValues) {
        Response<ArrayList<Wishlist>> response = super.findWhereIn(Wishlist.class, columnName, listValues);
        return response.getData();
    }
    
}
