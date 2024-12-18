package controllers;

import java.util.ArrayList;

import models.Transaction;
import services.Response;

public class TransactionController {
    
    public TransactionController() {
        // Constructor for future initializations if needed
    }

    /**
     * Execute the purchase of an item by a buyer.
     * [BUYER]
     *
     * @param userId  The unique identifier of the buyer.
     * @param itemId  The unique identifier of the item to be purchased.
     * @return        A Response object containing the Transaction details if successful.
     */
    public static Response<Transaction> executePurchase(String userId, String itemId) {
        return Transaction.purchaseItem(userId, itemId);
    }
    
    /**
     * Retrieve the purchase history for a specific user.
     * [BUYER]
     *
     * @param userId  The unique identifier of the user whose history is being fetched.
     * @return        A Response object containing a list of Transactions.
     */
    public static Response<ArrayList<Transaction>> getPurchaseHistory(String userId){
        return Transaction.viewTransactionHistory(userId);
    }

}
