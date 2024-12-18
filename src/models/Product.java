package models;

import java.util.ArrayList;

import services.Response;

public class Product extends Model {

    private String productId;
    private String itemId;
    private String sellerId;
    
    private final String TABLE_NAME = "products";
    private final String PRIMARY_KEY = "productId";
    
    public Product() {
        // Default constructor for future use
    }

    public Product(String id, String itemId, String sellerId) {
        super();
        this.productId = id;
        this.itemId = itemId;
        this.sellerId = sellerId;
    }

    // Getters and Setters
    
    

    public String getId() {
        return productId;
    }

    public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public String getPRIMARY_KEY() {
		return PRIMARY_KEY;
	}

	public void setId(String id) {
        this.productId = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
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
     * Retrieve the User (Seller) associated with this Product.
     * [SELLER]
     *
     * @return The User who is selling this Product.
     */
    public User getSeller() {
        return this.hasOne(User.class, "users", this.getSellerId(), "userId");
    }

    /**
     * Retrieve the Item associated with this Product.
     * [ADMIN]
     *
     * @return The Item linked to this Product.
     */
    public Item getItem() {
    	System.out.println("returning Item");
        return this.hasOne(Item.class, "items", this.getItemId(), "itemId");
    }

    /**
     * Retrieve all Wishlists that include this Product.
     * [BUYER]
     *
     * @return A list of Wishlists containing this Product.
     */
    public ArrayList<Wishlist> getWishlists() {
        return this.hasMany(Wishlist.class, "wishlists", this.getId(), "productId");
    }

    /**
     * Retrieve all Offers made on this Product.
     * [SELLER]
     *
     * @return A list of Offers for this Product.
     */
    public ArrayList<Offer> getOffers() {
        return this.hasMany(Offer.class, "offers", this.getId(), "productId");
    }

    /**
     * Retrieve all Transactions involving this Product.
     * [ADMIN]
     *
     * @return A list of Transactions for this Product.
     */
    public ArrayList<Transaction> getTransactions() {
        return this.hasMany(Transaction.class, "transactions", this.getId(), "productId");
    }

    /**
     * Retrieve all Products.
     *
     * @return A list of all Products.
     */
    public ArrayList<Product> getAllProducts() {
        Response<ArrayList<Product>> response = super.fetchAll(Product.class);
        return response.getData();
    }

    /**
     * Retrieve Products based on a specific condition.
     *
     * @param columnName The column to filter by.
     * @param operator   The operator for comparison (e.g., '=', '>', '<').
     * @param key        The value to compare against.
     * @return           A list of Products matching the condition.
     */
    public ArrayList<Product> findProducts(String columnName, String operator, String key) {
        Response<ArrayList<Product>> response = super.findWhere(Product.class, columnName, operator, key);
        return response.getData();
    }

    /**
     * Update an existing Product.
     *
     * @param fromKey The primary key value identifying the Product to update.
     * @return        The updated Product.
     */
    public Product updateProduct(String fromKey) {
        Response<Product> response = super.updateRecord(Product.class, fromKey);
        return response.getData();
    }

    /**
     * Insert a new Product into the database.
     *
     * @return The inserted Product.
     */
    public Product insertProduct() {
        Response<Product> response = super.insertRecord(Product.class);
        return response.getData();
    }

    /**
     * Find a Product by its primary key.
     *
     * @param fromKey The primary key value of the Product.
     * @return        The found Product.
     */
    public Product findProduct(String fromKey) {
        Response<Product> response = super.findByPrimaryKey(Product.class, fromKey);
        return response.getData();
    }

    /**
     * Retrieve the most recently added Product.
     *
     * @return The latest Product.
     */
    public Product getLatestProduct() {
        Response<Product> response = super.fetchLatest(Product.class);
        return response.getData();
    }

    /**
     * Delete a Product from the database.
     *
     * @param fromKey The primary key value of the Product to delete.
     * @return        True if deletion was successful, else False.
     */
    public Boolean deleteProduct(String fromKey) {
        Response<Boolean> response = super.deleteRecord(Product.class, fromKey);
        return response.getData();
    }

    /**
     * Retrieve Products where the specified column's value is within the provided list.
     *
     * @param columnName The column to filter by.
     * @param listValues The list of values to include in the IN clause.
     * @return           A list of Products matching the criteria.
     */
    public ArrayList<Product> findProductsIn(String columnName, ArrayList<String> listValues) {
        Response<ArrayList<Product>> response = super.findWhereIn(Product.class, columnName, listValues);
        return response.getData();
    }
}
