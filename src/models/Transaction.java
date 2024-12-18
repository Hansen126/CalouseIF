package models;

import java.util.ArrayList;
import java.math.BigDecimal;

import factories.TransactionFactory;
import factories.WishlistFactory;
import services.Response;
import utilities.IdGenerator;

public class Transaction extends Model {

    private String transactionId;
    private String userId;
    private String productId;
    
    private final String TABLE_NAME = "transactions";
    private final String PRIMARY_KEY = "transactionId";
    
    public Transaction() {
        // Default constructor for future use
    }

    public Transaction(String id, String userId, String productId) {
        super();
        this.transactionId = id;
        this.userId = userId;
        this.productId = productId;
    }

    /**
     * Process the purchase of an item by a buyer.
     * [BUYER]
     *
     * @param buyerId    The unique identifier of the buyer.
     * @param productId  The unique identifier of the product to purchase.
     * @return           A Response object containing the Transaction result.
     */
    public static Response<Transaction> purchaseItem(String buyerId, String productId) {
        Response<Transaction> response = new Response<>();
        System.out.println("pruchase item 1");
        
        try {
            // Generate a new transaction ID
        	 System.out.println("pruchase item 2");
            String newTransactionId = IdGenerator.generateNewId(TransactionFactory.createTransaction().getLatestTransaction().getId(), "TRSC");
            
            System.out.println("pruchase item 3");
            // Create and insert the new transaction into the database
            Transaction transaction = TransactionFactory.createTransaction(buyerId, productId, newTransactionId);
            System.out.println("pruchase item 4");
            transaction.insertTransaction();
            
            System.out.println("pruchase item 5");
            // Remove the purchased item from all wishlists
            ArrayList<Wishlist> wishlists = WishlistFactory.createWishlist().findWishlists("productId", "=", productId);
            System.out.println("pruchase item 6");
            for (Wishlist wishlist : wishlists) {
                Wishlist.removeWishlistItem(wishlist.getId());
            }
            System.out.println("pruchase item 7");
            response.setMessage("Success: Item purchased successfully.");
            response.setSuccess(true);
            response.setData(transaction);
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
     * Retrieve the transaction history for a specific user.
     * [BUYER]
     *
     * @param userId  The unique identifier of the user.
     * @return        A Response object containing the list of Transactions.
     */
    public static Response<ArrayList<Transaction>> viewTransactionHistory(String userId){
        Response<ArrayList<Transaction>> response = new Response<>();
        
        try {
            ArrayList<Transaction> transactions = TransactionFactory.createTransaction().findTransactions("userId", "=", userId);
            
            response.setMessage("Success: Retrieved transaction history.");
            response.setSuccess(true);
            response.setData(transactions);
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
        return transactionId;
    }

    public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public String getPRIMARY_KEY() {
		return PRIMARY_KEY;
	}

	public void setId(String id) {
        this.transactionId = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
     * Retrieve the Product associated with this Transaction.
     * [ADMIN]
     *
     * @return The Product linked to this Transaction.
     */
    public Product getProduct() {
        return this.hasOne(Product.class, "products", this.getProductId(), "productId");
    }

    /**
     * Retrieve the User who made this Transaction.
     * [BUYER]
     *
     * @return The User associated with this Transaction.
     */
    public User getUser() {
        return this.hasOne(User.class, "users", this.getUserId(), "userId");
    }

    /**
     * Retrieve all Transactions.
     *
     * @return A list of all Transactions.
     */
    public ArrayList<Transaction> getAllTransactions() {
        Response<ArrayList<Transaction>> response = super.fetchAll(Transaction.class);
        return response.getData();
    }

    /**
     * Retrieve Transactions based on a specific condition.
     *
     * @param columnName The column to filter by.
     * @param operator   The operator for comparison (e.g., '=', '>', '<').
     * @param key        The value to compare against.
     * @return           A list of Transactions matching the condition.
     */
    public ArrayList<Transaction> findTransactions(String columnName, String operator, String key) {
        Response<ArrayList<Transaction>> response = super.findWhere(Transaction.class, columnName, operator, key);
        return response.getData();
    }

    /**
     * Update an existing Transaction.
     *
     * @param fromKey The primary key value identifying the Transaction to update.
     * @return        The updated Transaction.
     */
    public Transaction updateTransaction(String fromKey) {
        Response<Transaction> response = super.updateRecord(Transaction.class, fromKey);
        return response.getData();
    }

    /**
     * Insert a new Transaction into the database.
     *
     * @return The inserted Transaction.
     */
    public Transaction insertTransaction() {
    	System.out.println("insertTransaction");
        Response<Transaction> response = super.insertRecord(Transaction.class);
        System.out.println("insertTransaction 2");
        return response.getData();
    }

    /**
     * Find a Transaction by its primary key.
     *
     * @param fromKey The primary key value of the Transaction.
     * @return        The found Transaction.
     */
    public Transaction findTransaction(String fromKey) {
        Response<Transaction> response = super.findByPrimaryKey(Transaction.class, fromKey);
        return response.getData();
    }

    /**
     * Retrieve the most recently added Transaction.
     *
     * @return The latest Transaction.
     */
    public Transaction getLatestTransaction() {
        Response<Transaction> response = super.fetchLatest(Transaction.class);
        return response.getData();
    }

    /**
     * Delete a Transaction from the database.
     *
     * @param fromKey The primary key value of the Transaction to delete.
     * @return        True if deletion was successful, else False.
     */
    public Boolean deleteTransaction(String fromKey) {
        Response<Boolean> response = super.deleteRecord(Transaction.class, fromKey);
        return response.getData();
    }

    /**
     * Retrieve Transactions where the specified column's value is within the provided list.
     *
     * @param columnName The column to filter by.
     * @param listValues The list of values to include in the IN clause.
     * @return           A list of Transactions matching the criteria.
     */
    public ArrayList<Transaction> findTransactionsIn(String columnName, ArrayList<String> listValues) {
        Response<ArrayList<Transaction>> response = super.findWhereIn(Transaction.class, columnName, listValues);
        return response.getData();
    }
}
