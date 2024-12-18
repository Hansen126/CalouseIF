package models;

import java.math.BigDecimal;
import java.util.ArrayList;

import factories.ItemFactory;
import factories.OfferFactory;
import factories.ProductFactory;
import services.Response;
import utilities.IdGenerator;

public class Item extends Model {

    private String itemId;
    private String itemName;
    private String itemSize;
    private BigDecimal itemPrice;
    private String itemCategory;
    private String itemStatus;
    private String declineReason;
    
    private final String TABLE_NAME = "items";
    private final String PRIMARY_KEY = "itemId";
    
    public Item() {
        // Default constructor for future use
    }
    
    public Item(String id, String name, String size, BigDecimal price, String category,
                String status, String declineReason) {
        super();
        this.itemId = id;
        this.itemName = name;
        this.itemSize = size;
        this.itemPrice = price;
        this.itemCategory = category;
        this.itemStatus = status;
        this.declineReason = declineReason;
    }
    
    /**
     * Retrieve all approved products available for buyers.
     * [BUYER]
     *
     * @return A Response object containing a list of approved Products.
     */
    public static Response<ArrayList<Product>> fetchAllApprovedProducts(){
        
        Response<ArrayList<Product>> response = new Response<ArrayList<Product>>();
        
        try {
            // Fetch all products using ProductFactory
            ArrayList<Product> products = ProductFactory.createProduct().getAllProducts();
            ArrayList<String> approvedIds = new ArrayList<String>();
            
            for (Product product : products) {
                Item item = product.getItem();
                
                // Only include approved items
                if(item.getStatus().equalsIgnoreCase("Approved")) {
                    approvedIds.add(product.getId());
                }
            }
            
            // Fetch products with IDs in approvedIds
            products = ProductFactory.createProduct().findProductsIn("productId", approvedIds);
            
            response.setMessage("Success: Retrieved all approved products.");
            response.setSuccess(true);
            response.setData(products);
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
     * Search for products based on the provided name.
     * [BUYER]
     *
     * @param productName The name keyword to search for.
     * @return            A Response object containing a list of matching Products.
     */
    public static Response<ArrayList<Product>> searchProductsByName(String productName){
        Response<ArrayList<Product>> response = new Response<ArrayList<Product>>();
        
        try {
            ArrayList<Product> products = ProductFactory.createProduct().getAllProducts();
            ArrayList<String> matchingIds = new ArrayList<String>();
            
            for (Product product : products) {
                Item item = product.getItem();
                
                if(item.getStatus().equalsIgnoreCase("Approved") && 
                   item.getName().toLowerCase().contains(productName.toLowerCase())) {
                    matchingIds.add(product.getId());
                }
            }
            
            products = ProductFactory.createProduct().findProductsIn("productId", matchingIds);
            
            response.setMessage("Success: Retrieved searched products.");
            response.setSuccess(true);
            response.setData(products);
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
     * Retrieve all products listed by a specific seller.
     * [SELLER]
     *
     * @param sellerId The unique identifier of the seller.
     * @return         A Response object containing a list of the seller's Products.
     */
    public static Response<ArrayList<Product>> getSellerProducts(String sellerId){
        Response<ArrayList<Product>> response = new Response<ArrayList<Product>>();
        
        try {
            ArrayList<Product> products = ProductFactory.createProduct().findProducts("sellerId", "=", sellerId);
            
            response.setMessage("Success: Retrieved seller's products.");
            response.setSuccess(true);
            response.setData(products);
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
     * Retrieve all items pending approval by the admin.
     * [ADMIN]
     *
     * @return A Response object containing a list of pending Products.
     */
    public static Response<ArrayList<Product>> getPendingProducts() {
    	System.out.println("getting pending products in getPendingProducts()");
    	System.out.println("creating response in getpendingproducts");
        Response<ArrayList<Product>> response = new Response<>();
        System.out.println("response created in getpendingproducts");
        try {
        	System.out.println("get all products in try");
            ArrayList<Product> products = ProductFactory.createProduct().getAllProducts();
            System.out.println("getallproducts in getpendingproducts success");
            ArrayList<String> pendingIds = new ArrayList<>();
            System.out.println("created pendingId");
            int i = 0;
            int j = 0;
            int k = 0;
            for (Product product : products) {
                // Include items with Pending status
            	System.out.println("inside loop " + i);
                if (product.getItem().getStatus().equalsIgnoreCase("Pending")) {
                	System.out.println("inside if loop before " + j);
                    pendingIds.add(product.getId());
                    System.out.println("inside if loop after " + k);
                    j++;
                }
                i++;
            }
            System.out.println("loop finished, findprodutsIn");
            products = ProductFactory.createProduct().findProductsIn("productId", pendingIds);
            System.out.println("products created");
            response.setMessage("Success: Retrieved all pending products.");
            response.setSuccess(true);
            response.setData(products);
            return response;
        } catch (Exception e) {
        	System.out.println("error in trycatch getpendingproducts");
            e.printStackTrace();
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
    }

    /**
     * Retrieve all approved products.
     * [BUYER]
     *
     * @return A Response object containing a list of approved Products.
     */
    public static Response<ArrayList<Product>> fetchApprovedProducts() {
        Response<ArrayList<Product>> response = new Response<>();

        try {
            ArrayList<Product> products = ProductFactory.createProduct().getAllProducts();
            ArrayList<String> approvedIds = new ArrayList<>();

            for (Product product : products) {
                if (product.getItem().getStatus().equalsIgnoreCase("Approved")) {
                    approvedIds.add(product.getId());
                }
            }

            products = ProductFactory.createProduct().findProductsIn("productId", approvedIds);

            response.setMessage("Success: Retrieved all approved products.");
            response.setSuccess(true);
            response.setData(products);
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
     * Retrieve all offers made on a seller's products.
     * [SELLER]
     *
     * @param sellerId The unique identifier of the seller.
     * @return         A Response object containing a list of Offers.
     */
    public static Response<ArrayList<Offer>> getProductOffers(String sellerId) {
        Response<ArrayList<Offer>> response = new Response<>();

        try {
        	System.out.println(sellerId);
            ArrayList<Product> products = ProductFactory.createProduct().findProducts("sellerId", "=", sellerId);
            System.out.println("products fetch");
            ArrayList<Offer> offers = new ArrayList<>();
            System.out.println("created offerlist");

            for (Product product : products) {
                offers.addAll(product.getOffers());
            }
            System.out.println("products added");
            response.setMessage("Success: Retrieved all offers on your products.");
            response.setSuccess(true);
            response.setData(offers);
            
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
     * Add a new item to the listing by a seller.
     * [SELLER]
     *
     * @param sellerId   The unique identifier of the seller.
     * @param name       The name of the item.
     * @param category   The category of the item.
     * @param size       The size specification of the item.
     * @param price      The price of the item.
     * @return           A Response object containing the added Item.
     */
    public static Response<Item> addNewItem(String sellerId, String name, String category, String size, BigDecimal price) {
        Response<Item> response = new Response<Item>();
        
        try {
            // Validate item details
            response = validateItemDetails(name, category, size, price);
            
            if(!response.isSuccess()) {
                return response;				
            }
            
            // Create new Item using ItemFactory and insert into database
            String newItemId = IdGenerator.generateNewId(ItemFactory.createItem().getLatestItem().getId(), "ITEM");
            Item newItem = ItemFactory.createItem(newItemId, name, size, price, category, "Pending", null);
            newItem.insertItem();
            
            // Create new Product using ProductFactory and insert into database
            String newProductId = IdGenerator.generateNewId(ProductFactory.createProduct().getLatestProduct().getId(), "PROD");
            Product newProduct = ProductFactory.createProduct(newProductId, newItem.getId(), sellerId);
            newProduct.insertProduct();
        
            response.setMessage("Success: Item successfully added.");
            response.setSuccess(true);
            response.setData(newItem);
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
     * Update the details of an existing item.
     * [SELLER]
     *
     * @param itemId    The unique identifier of the item to update.
     * @param name      The new name of the item.
     * @param category  The new category of the item.
     * @param size      The new size specification of the item.
     * @param price     The new price of the item.
     * @return          A Response object containing the updated Item.
     */
    public static Response<Item> updateItem(String itemId, String name, String category, String size, BigDecimal price) {
        Response<Item> response = new Response<Item>();
        
        try {
            // Validate updated item details
            response = validateItemDetails(itemId, name, category, size, price);

            if(!response.isSuccess()) {
                return response;
            }
            
            // Retrieve and update the item
            Item item = response.getData();
            item.setName(name);
            item.setCategory(category);
            item.setSize(size);
            item.setPrice(price);
            
            // Update the item in the database
            item.updateItem(item.getId());
            
            response.setMessage("Success: Item updated successfully.");
            response.setSuccess(true);
            response.setData(item);
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
     * Remove an item from the listing.
     * [SELLER]
     *
     * @param itemId The unique identifier of the item to remove.
     * @return       A Response object indicating the result of the removal.
     */
    public static Response<Item> removeItem(String itemId) {
        Response<Item> response = new Response<Item>();
        
        try {
            boolean isDeleted = ItemFactory.createItem().findItem(itemId).deleteItem(itemId);
            
            if(!isDeleted) {
                response.setMessage("Error: Failed to delete the item.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }
            
            response.setMessage("Success: Item has been deleted.");
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
    
    /**
     * Submit an offer for a specific product.
     * [BUYER]
     *
     * @param productId The unique identifier of the product.
     * @param buyerId   The unique identifier of the buyer.
     * @param offerPrice The price offered by the buyer.
     * @return          A Response object containing the Offer.
     */
    public static Response<Offer> makeOffer(String productId, String buyerId, BigDecimal offerPrice) {
        Response<Offer> response = new Response<Offer>();

        try {
            // Validate Product ID and offer price 
            response = validateOfferDetails(productId, offerPrice);
            if (!response.isSuccess()) {
                return response;
            }

            ArrayList<Offer> offers = OfferFactory.createOffer().findOffers("productId", "=", productId);
            
            Offer existingOffer = null;
            for (Offer offer : offers) {
                if (offer.getBuyerId().equals(buyerId)) {
                    existingOffer = offer;
                    break;
                }
            }

            if (existingOffer == null) {
                // Create and insert new offer into database
                String newOfferId = IdGenerator.generateNewId(OfferFactory.createOffer().getLatestOffer().getId(), "OFFR");
                existingOffer = OfferFactory.createOffer(newOfferId, productId, buyerId, offerPrice, "Offered", null);
                existingOffer.insertOffer();
            } else {
                // Ensure new offer price is higher than the existing one
                if (existingOffer.getOfferPrice().compareTo(offerPrice) >= 0) {
                    response.setMessage("Error: Your new offer must be higher than the existing offer.");
                    response.setSuccess(false);
                    response.setData(null);
                    return response;
                }

                existingOffer.setOfferPrice(offerPrice);
                existingOffer.updateOffer(existingOffer.getId());
            }

            response.setMessage("Success: Your offer has been submitted.");
            response.setSuccess(true);
            response.setData(existingOffer);
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
     * Accept an existing offer on a product.
     * [SELLER]
     *
     * @param offerId The unique identifier of the offer to accept.
     * @return        A Response object containing the accepted Offer.
     */
    public static Response<Offer> acceptOffer(String offerId) {
        Response<Offer> response = new Response<>();

        try {
            Offer offer = OfferFactory.createOffer().findOffer(offerId);

            if (offer == null) {
                response.setMessage("Error: Offer not found.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }

            // Update offer status to Accepted
            offer.setOfferStatus("Accepted");
            offer.updateOffer(offerId);

            // Automatically process the purchase for the buyer
            Transaction.purchaseItem(offer.getBuyerId(), offer.getProductId());
            
            response.setMessage("Success: Offer has been accepted.");
            response.setSuccess(true);
            response.setData(offer);
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
     * Decline an existing offer on a product with a specified reason.
     * [SELLER]
     *
     * @param offerId The unique identifier of the offer to decline.
     * @param reason  The reason for declining the offer.
     * @return        A Response object containing the declined Offer.
     */
    public static Response<Offer> declineOffer(String offerId, String reason) {
        Response<Offer> response = new Response<Offer>();

        try {
            Offer offer = OfferFactory.createOffer().findOffer(offerId);
            
            if (offer == null) {
                response.setMessage("Error: Offer not found.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }

            // Update offer status to Declined with reason
            offer.setOfferStatus("Declined");
            offer.setDeclineReason(reason);
            offer.updateOffer(offerId);

            response.setMessage("Success: Offer has been declined.");
            response.setSuccess(true);
            response.setData(offer);
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
     * Approve a pending item for listing.
     * [ADMIN]
     *
     * @param itemId The unique identifier of the item to approve.
     * @return       A Response object containing the approved Item.
     */
    public static Response<Item> approveItem(String itemId) {
        Response<Item> response = new Response<>();

        try {
            Item item = ItemFactory.createItem().findItem(itemId);

            if (item == null) {
                response.setMessage("Error: Item not found.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }

            // Update item status to Approved
            item.setStatus("Approved");
            item.updateItem(itemId);

            response.setMessage("Success: Item has been approved.");
            response.setSuccess(true);
            response.setData(item);
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
     * Decline a pending item with a specified reason.
     * [ADMIN]
     *
     * @param itemId The unique identifier of the item to decline.
     * @param reason The reason for declining the item.
     * @return       A Response object containing the declined Item.
     */
    public static Response<Item> declineItem(String itemId, String reason) {
        Response<Item> response = new Response<Item>();

        try {
            Item item = ItemFactory.createItem().findItem(itemId);

            if (item == null) {
                response.setMessage("Error: Item not found.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }

            // Update item status to Declined with reason
            item.setStatus("Declined");
            item.setDeclineReason(reason);
            item.updateItem(itemId);

            response.setMessage("Success: Item has been declined.");
            response.setSuccess(true);
            response.setData(item);
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
     * Validate offer details before processing.
     *
     * @param productId The unique identifier of the product.
     * @param offerPrice The price offered.
     * @return           A Response object indicating validation status.
     */
    public static Response<Offer> validateOfferDetails(String productId, BigDecimal offerPrice) {
        Response<Offer> response = new Response<>();
        Product product = ProductFactory.createProduct().findProduct(productId);

        if (product == null) {
            response.setMessage("Error: Product not found.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        } 
        if (offerPrice == null) {
            response.setMessage("Error: Offer price cannot be null.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        } 
        if (offerPrice.compareTo(BigDecimal.ZERO) <= 0) {
            response.setMessage("Error: Offer price must be greater than zero.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }

        response.setMessage("Success: Offer details are valid.");
        response.setSuccess(true);
        response.setData(null);
        return response;
    }

    /**
     * Validate item details before adding or updating.
     *
     * @param name      The name of the item.
     * @param category  The category of the item.
     * @param size      The size specification of the item.
     * @param price     The price of the item.
     * @return          A Response object indicating validation status.
     */
    public static Response<Item> validateItemDetails(String name, String category, String size, BigDecimal price) {
        Response<Item> response = new Response<>();

        if (name == null || name.trim().isEmpty()) {
            response.setMessage("Error: Item name cannot be empty.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        } 
        if (name.length() < 3) {
            response.setMessage("Error: Item name must be at least 3 characters long.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (category == null || category.trim().isEmpty()) {
            response.setMessage("Error: Item category cannot be empty.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (category.length() < 3) {
            response.setMessage("Error: Item category must be at least 3 characters long.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (size == null || size.trim().isEmpty()) {
            response.setMessage("Error: Item size cannot be empty.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (price == null) {
            response.setMessage("Error: Item price cannot be null.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            response.setMessage("Error: Item price must be greater than zero.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }

        response.setMessage("Success: Item details are valid.");
        response.setSuccess(true);
        response.setData(null);
        return response;
    }
    
    /**
     * Validate item details before updating an existing item.
     *
     * @param itemId    The unique identifier of the item.
     * @param name      The new name of the item.
     * @param category  The new category of the item.
     * @param size      The new size specification of the item.
     * @param price     The new price of the item.
     * @return          A Response object indicating validation status and containing the Item if valid.
     */
    public static Response<Item> validateItemDetails(String itemId, String name, String category, String size, BigDecimal price) {
        Response<Item> response = new Response<Item>();
        Item item = ItemFactory.createItem().findItem(itemId);
        
        if(item == null) {
            response.setMessage("Error: Item not found.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (name == null || name.trim().isEmpty()) {
            response.setMessage("Error: Item name cannot be empty.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (name.length() < 3) {
            response.setMessage("Error: Item name must be at least 3 characters long.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (category == null || category.trim().isEmpty()) {
            response.setMessage("Error: Item category cannot be empty.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (category.length() < 3) {
            response.setMessage("Error: Item category must be at least 3 characters long.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (size == null || size.trim().isEmpty()) {
            response.setMessage("Error: Item size cannot be empty.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (price == null) {
            response.setMessage("Error: Item price cannot be null.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            response.setMessage("Error: Item price must be greater than zero.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        response.setMessage("Success: Item details are valid.");
        response.setSuccess(true);
        response.setData(item);
        return response;
    }

    // Getters and Setters
    
    

    public String getId() {
        return itemId;
    }

    public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemSize() {
		return itemSize;
	}

	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public String getPRIMARY_KEY() {
		return PRIMARY_KEY;
	}

	public void setId(String id) {
        this.itemId = id;
    }

    public String getName() {
        return itemName;
    }

    public void setName(String name) {
        this.itemName = name;
    }

    
    public String getSize() {
        return itemSize;
    }

    public void setSize(String size) {
        this.itemSize = size;
    }

    public BigDecimal getPrice() {
        return itemPrice;
    }

    public void setPrice(BigDecimal price) {
        this.itemPrice = price;
    }

    public String getCategory() {
        return itemCategory;
    }

    public void setCategory(String category) {
        this.itemCategory = category;
    }

    
    public String getStatus() {
        return itemStatus;
    }

    public void setStatus(String status) {
        this.itemStatus = status;
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
     * Establish a one-to-many relationship with Products.
     *
     * @return A list of Products associated with this Item.
     */
    public ArrayList<Product> getProducts() {
        return this.hasMany(Product.class, "products", this.getId(), "itemId");
    }
    
    // CRUD Operations

    /**
     * Retrieve all Items.
     *
     * @return A list of all Items.
     */
    public ArrayList<Item> getAllItems(){
    	Response<ArrayList<Item>> response = super.fetchAll(Item.class);
        return response.getData() != null ? response.getData() : new ArrayList<>();
    }
    
    /**
     * Retrieve Items based on specific conditions.
     *
     * @param column    The column to findProducts by.
     * @param operator  The operator for comparison.
     * @param key       The value to compare against.
     * @return          A list of Items matching the conditions.
     */
    public ArrayList<Item> findItems(String column, String operator, String key) {
        Response<ArrayList<Item>> response = super.findWhere(Item.class, column, operator, key);
        return response.getData() != null ? response.getData() : new ArrayList<>();
    }
    
    /**
     * Update an existing Item.
     *
     * @param key The key identifying the Item to update.
     * @return    The updated Item.
     */
    public Item updateItem(String key) {
        Response<Item> response = super.updateRecord(Item.class, key);
        return response.getData();
    }
    
    /**
     * Insert a new Item into the database.
     *
     * @return The inserted Item.
     */
    public Item insertItem() {
        Response<Item> response = super.insertRecord(Item.class);
        return response.getData();
    }
    
    /**
     * Find a specific Item by its key.
     *
     * @param key The key identifying the Item.
     * @return    The found Item.
     */
    public Item findItem(String key) {
        Response<Item> response = super.findByPrimaryKey(Item.class, key);
        return response.getData();
    }
    
    /**
     * Retrieve the most recently added Item.
     *
     * @return The latest Item.
     */
    public Item getLatestItem() {
        Response<Item> response = super.fetchLatest(Item.class);
        return response.getData();
    }
    
    /**
     * Delete an Item from the database.
     *
     * @param key The key identifying the Item to delete.
     * @return    True if deletion was successful, else False.
     */
    public Boolean deleteItem(String key) {
        Response<Boolean> response = super.deleteRecord(Item.class, key);
        return response.getData() != null && response.getData();
    }
    
    /**
     * Retrieve Items with IDs within a specified list.
     *
     * @param column      The column to filter by.
     * @param listValues  The list of values to include.
     * @return            A list of Items matching the criteria.
     */
    public ArrayList<Item> filterItemsByIds(String column, ArrayList<String> listValues) {
        Response<ArrayList<Item>> response = super.findWhereIn(Item.class, column, listValues);
        return response.getData() != null ? response.getData() : new ArrayList<>();
    }
}
