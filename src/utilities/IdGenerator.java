package utilities;

/**
 * Utility class for generating new unique IDs based on existing IDs.
 */
public class IdGenerator {

    /**
     * Generates a new ID by incrementing the numerical part of the old ID.
     *
     * @param oldId     The existing ID to base the new ID on.
     * @param prefix    The desired prefix for the new ID.
     * @return          A new unique ID with the specified prefix.
     */
    public static String generateNewId(String oldId, String prefix) {
        if (oldId == null || oldId.length() <= 4) {
            throw new IllegalArgumentException("Invalid oldId provided.");
        }

        String numericPart = oldId.substring(4);
        int numericId;

        try {
            numericId = Integer.parseInt(numericPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Old ID does not contain a valid numerical part.", e);
        }

        return String.format("%s%03d", prefix, (numericId + 1));
    }
}
