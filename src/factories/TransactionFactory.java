package factories;

import models.Transaction;

/**
 * Factory class for creating instances of Transaction.
 */
public class TransactionFactory {

    // Private constructor to prevent instantiation
    private TransactionFactory() {
        throw new UnsupportedOperationException("TransactionFactory is a utility class and cannot be instantiated.");
    }

    /**
     * Create a new empty Transaction instance.
     *
     * @return A new Transaction object.
     */
    public static Transaction createTransaction() {
        return new Transaction();
    }

    /**
     * Create a new Transaction instance with the specified attributes.
     *
     * @param userId        The unique identifier of the user involved in the transaction.
     * @param productId     The unique identifier of the product involved in the transaction.
     * @param transactionId The unique identifier for the transaction.
     * @return              A new Transaction object populated with the provided details.
     */
    public static Transaction createTransaction(String userId, String productId, String transactionId) {
        return new Transaction(userId, productId, transactionId);
    }
}
