package models;

import java.math.BigDecimal;
import java.util.ArrayList;

import services.Response;

public class Offer extends Model {

    private String offerId;
    private String productId;
    private String buyerId;
    private BigDecimal offerPrice;
    private String offerStatus;
    private String declineReason;
    
    private final String TABLE_NAME = "offers";
    private final String PRIMARY_KEY = "offerId";

    public Offer() {
        // Default constructor for future use
    }
    
    public Offer(String id, String productId, String buyerId, BigDecimal offerPrice,
                 String offerStatus, String declineReason) {
        super();
        this.offerId = id;
        this.productId = productId;
        this.buyerId = buyerId;
        this.offerPrice = offerPrice;
        this.offerStatus = offerStatus;
        this.declineReason = declineReason;
    }

    // Getters and Setters
    
    

    public String getId() {
        return offerId;
    }

    public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public String getPRIMARY_KEY() {
		return PRIMARY_KEY;
	}

	public void setId(String id) {
        this.offerId = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(BigDecimal offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
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
     * Retrieve the associated Product for this Offer.
     * [SELLER]
     *
     * @return The Product associated with this Offer.
     */
    public Product getProduct() {
        return this.hasOne(Product.class, "products", this.getProductId(), "productId");
    }

    /**
     * Retrieve the User (Buyer) who made this Offer.
     * [BUYER]
     *
     * @return The User who made the Offer.
     */
    public User getBuyer() {
        return this.hasOne(User.class, "users", this.getBuyerId(), "userId");
    }

    /**
     * Retrieve all Offers.
     *
     * @return A list of all Offers.
     */
    public ArrayList<Offer> getAllOffers() {
        Response<ArrayList<Offer>> response = super.fetchAll(Offer.class);
        return response.getData();
    }

    /**
     * Retrieve Offers based on a specific condition.
     *
     * @param columnName The column to filter by.
     * @param operator   The operator for comparison (e.g., '=', '>', '<').
     * @param key        The value to compare against.
     * @return           A list of Offers matching the condition.
     */
    public ArrayList<Offer> findOffers(String columnName, String operator, String key) {
        Response<ArrayList<Offer>> response = super.findWhere(Offer.class, columnName, operator, key);
        return response.getData();
    }

    /**
     * Update an existing Offer.
     *
     * @param fromKey The primary key value identifying the Offer to update.
     * @return        The updated Offer.
     */
    public Offer updateOffer(String fromKey) {
        Response<Offer> response = super.updateRecord(Offer.class, fromKey);
        return response.getData();
    }

    /**
     * Insert a new Offer into the database.
     *
     * @return The inserted Offer.
     */
    public Offer insertOffer() {
        Response<Offer> response = super.insertRecord(Offer.class);
        return response.getData();
    }

    /**
     * Find an Offer by its primary key.
     *
     * @param fromKey The primary key value of the Offer.
     * @return        The found Offer.
     */
    public Offer findOffer(String fromKey) {
        Response<Offer> response = super.findByPrimaryKey(Offer.class, fromKey);
        return response.getData();
    }

    /**
     * Retrieve the most recently added Offer.
     *
     * @return The latest Offer.
     */
    public Offer getLatestOffer() {
        Response<Offer> response = super.fetchLatest(Offer.class);
        return response.getData();
    }

    /**
     * Delete an Offer from the database.
     *
     * @param fromKey The primary key value of the Offer to delete.
     * @return        True if deletion was successful, else False.
     */
    public Boolean deleteOffer(String fromKey) {
        Response<Boolean> response = super.deleteRecord(Offer.class, fromKey);
        return response.getData();
    }

    /**
     * Retrieve Offers where the specified column's value is within the provided list.
     *
     * @param columnName The column to filter by.
     * @param listValues The list of values to include in the IN clause.
     * @return           A list of Offers matching the criteria.
     */
    public ArrayList<Offer> findOffersIn(String columnName, ArrayList<String> listValues) {
        Response<ArrayList<Offer>> response = super.findWhereIn(Offer.class, columnName, listValues);
        return response.getData();
    }
}
