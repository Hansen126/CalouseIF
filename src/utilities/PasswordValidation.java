package utilities;

/**
 * Utility class for validating password strength and criteria.
 */
public class PasswordValidation {

    // Array of special characters considered valid in a password
    private static final char[] SPECIAL_CHARACTERS = {'!', '@', '#', '$', '%', '^', '&', '*'};

    /**
     * Validates whether the provided password contains at least one special character.
     *
     * @param password The password string to validate.
     * @return         True if the password contains at least one special character, else false.
     */
    public static boolean hasSpecialCharacter(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        for (char specialChar : SPECIAL_CHARACTERS) {
            if (password.indexOf(specialChar) >= 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Validates the overall strength of the password based on multiple criteria.
     *
     * @param password The password string to validate.
     * @return         True if the password meets all criteria, else false.
     */
    public static boolean isValid(String password) {
        // Example criteria: at least 8 characters, contains a special character, etc.
        if (password == null || password.length() < 8) {
            return false;
        }

        if (!hasSpecialCharacter(password)) {
            return false;
        }

        // Additional validation rules can be added here

        return true;
    }
}
