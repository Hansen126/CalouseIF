package controllers;

import models.Item;
import models.Offer;
import models.Product;
import services.Response;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemController {
	
	
	public ItemController() {
		// TODO Auto-generated constructor stub
	}
	
// View Items for Buyer
	public static Response<ArrayList<Product>> ViewItem(){
		return Item.fetchAllApprovedProducts();
	}
	
// Browse Item for Buyer
	public static Response<ArrayList<Product>> BrowseItem(String itemName){
		return Item.searchProductsByName(itemName);
	}
	

// Upload Item for Seller
	public static Response<Item> UploadItem(String sellerId, String itemName, String itemCategory, String itemSize, String itemPrice) {
		Response<Item> res = new Response<Item>();
		
		if(itemName.isEmpty()) {
			res.setMessage("Item name cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemName.length() < 3) {
			res.setMessage("Iten name must at least be 3 character long!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemCategory.isEmpty()) {
			res.setMessage("Item category cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemCategory.length() < 3) {
			res.setMessage("Item category cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemSize.isEmpty()) {
			res.setMessage("Item size cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemPrice.isEmpty()) {
			res.setMessage("Item price cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		}
		
		try {
			 BigDecimal price = new BigDecimal(itemPrice);
			if(price.compareTo(BigDecimal.ZERO) <= 0) {
				res.setMessage("Item price cannot be 0!");
				res.setSuccess(false);
				res.setData(null);
				return res;
			} 
			
		} catch (NumberFormatException e) {
		
			res.setMessage("Item price must be in number!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		}
		
		return Item.addNewItem(sellerId,itemName, itemCategory, itemSize, new BigDecimal(itemPrice));
	}
	
//	Edit item for seller
	public static Response<Item> EditItem(String itemId, String itemName, String itemCategory, String itemSize, String itemPrice) {
		Response<Item> res = new Response<Item>();

		if(itemName.isEmpty()) {
			res.setMessage("Item name cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemName.length() < 3) {
			res.setMessage("Iten name must at least be 3 character long!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemCategory.isEmpty()) {
			res.setMessage("Item category cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemCategory.length() < 3) {
			res.setMessage("Item category cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemSize.isEmpty()) {
			res.setMessage("Item size cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		} else if(itemPrice.isEmpty()) {
			res.setMessage("Item price cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		}
		
		try {
			 BigDecimal price = new BigDecimal(itemPrice);
			if(price.compareTo(BigDecimal.ZERO) <= 0) {
				res.setMessage("Item price cannot be 0!");
				res.setSuccess(false);
				res.setData(null);
				return res;
			} 
			
		} catch (NumberFormatException e) {
		
			res.setMessage("Item price must be in number!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		}
		
		return Item.updateItem(itemId, itemName, itemCategory, itemSize, new BigDecimal(itemPrice));
	}
	
	
// Delete Item for Seller
	public static Response<Item> DeleteItem(String itemId) {		
		return Item.removeItem(itemId);
	}
	
// View Seller Item
	public static Response<ArrayList<Item>> ViewSellerItem(String sellerId) {
		System.out.println("ViewSellerItem 1");
		Response<ArrayList<Product>> productResponse = new Response<>();
	    Response<ArrayList<Item>> response = new Response<>();
	    System.out.println("ViewSellerItem 2");
	    try {
	        // Fetch items using Model's method
	    	System.out.println("ViewSellerItem 3 " + sellerId);
	        Item itemModel = new Item();
	        Product productModel = new Product();
	        ArrayList<Product> products = productModel.findProducts("sellerId", "=", sellerId);
	        
	        ArrayList<Item> items = new ArrayList<>();;
	        System.out.println("ViewSellerItem 4");
	        
	        for (Product product : products) {
	            // Fetch the item based on itemId
	            ArrayList<Item> itemList = itemModel.findItems("itemId", "=", product.getItemId());
	            if (!itemList.isEmpty()) {
	                items.add(itemList.get(0)); // Add the fetched item
	            }
	        }
	        
	        response.setData(items);
	        response.setSuccess(true);
	        response.setMessage("Successfully retrieved seller items.");
	        System.out.println("ViewSellerItem 5");
	    } catch (Exception e) {
	    	System.out.println("ViewSellerItem 6");
	        e.printStackTrace();
	        response.setSuccess(false);
	        response.setMessage("Error: Unable to fetch seller items.");
	        response.setData(new ArrayList<>()); // Return an empty list on error
	    }
	    return response;
	}
	

// View Requested Item
	public static Response<ArrayList<Item>> ViewRequestItem(String itemStatus){
		System.out.println("getting pending products");
		Response<ArrayList<Product>> res = Item.getPendingProducts();
		System.out.println("pending products taken");
		ArrayList<Product> data = res.getData();
		// Temporary array to store seller items
		ArrayList<Item> item = new ArrayList<Item>();
		
		for (Product product : data) {
			item.add(product.getItem());
		}
		
		Response<ArrayList<Item>> resResult = new Response<ArrayList<Item>>();
		resResult.setMessage(res.getMessage());
		resResult.setSuccess(res.isSuccess());
		resResult.setData(item);
		return resResult;
	}
	
	/**
	 * VIEW ACCEPTED ITEM
	 * @return
	 */
	public static Response<ArrayList<Product>> ViewAcceptedItem(){
		return Item.fetchApprovedProducts();
	}
	
	/**
	 * VIEW OFFERED ITEM
	 * [SELLER]
	 */
	public static Response<ArrayList<Offer>> ViewOfferItem(String sellerId){
		return Item.getProductOffers(sellerId);
	}
	
	/**
	 * OFFER PRICE 
	 * [BUYER]
	 * @param productId
	 * @param buyerId
	 * @param itemPrice
	 * @return
	 */
	public static Response<Offer> OfferPrice(String productId, String buyerId, String itemPrice) {
		Response<Offer> res = new Response<Offer>();
		
		// Validating Price 
		try {
			if(new BigDecimal(itemPrice).compareTo(BigDecimal.ZERO) == 0) {
				res.setMessage("Item price cannot be 0!");
				res.setSuccess(false);
				res.setData(null);
				return res;
			} 
			
		} catch (Exception e) {
			res.setMessage("Item price must be in number!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		}
		return Item.makeOffer(productId, buyerId, new BigDecimal(itemPrice));
	}
	
	/**
	 * ACCEPT OFFER 
	 * [SELLER]
	 * @param itemId
	 * @return
	 */
	public static Response<Offer> AcceptOffer(String itemId) {
		return Item.acceptOffer(itemId);
	}
	
	/**
	 * DECLINE OFFER
	 * [SELLER]
	 * @param offerId
	 * @param Reason
	 * @return
	 */
	public static Response<Offer> DeclineOffer(String offerId, String Reason) {
		Response<Offer> res = new Response<Offer>();
		// Validate reason 
		if(Reason.isEmpty()) {
			res.setMessage("Item price cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		}
		
		return Item.declineOffer(offerId, Reason);
	}

	/**
	 * APPROVE ITEM
	 * [ADMIN]
	 * @param itemId
	 * @return
	 */
	public static Response<Item> ApproveItem(String itemId) {
		return Item.approveItem(itemId);
	}
	
	/**
	 * DECLINE ITEM
	 * [ADMIN]
	 * @param itemId
	 * @param Reason
	 * @return
	 */
	public static Response<Item> DeclineItem(String itemId, String Reason) {
		Response<Item> res = new Response<Item>();
		if(Reason.isEmpty()) {
			res.setMessage("Item price cannot be empty!");
			res.setSuccess(false);
			res.setData(null);
			return res;
		}
		
		return Item.declineItem(itemId, Reason);
	}
	


}
