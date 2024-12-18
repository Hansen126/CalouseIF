package utilities;

/**
 * Utility class for validating phone numbers.
 */
public class PhoneNumValidation {

    private static final String COUNTRY_CODE_PREFIX = "+62";

    /**
     * Validates whether the provided phone number starts with the required country code.
     *
     * @param phoneNumber The phone number string to validate.
     * @return            True if the phone number starts with the specified country code, else false.
     */
    public static boolean hasValidCountryCode(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }

        return phoneNumber.startsWith(COUNTRY_CODE_PREFIX);
    }

    /**
     * Validates the overall format and criteria of the phone number.
     *
     * @param phoneNumber The phone number string to validate.
     * @return            True if the phone number meets all criteria, else false.
     */
    public static boolean isValid(String phoneNumber) {
        // Example criteria: starts with +62, followed by 9-12 digits
        if (!hasValidCountryCode(phoneNumber)) {
            return false;
        }

        String numericPart = phoneNumber.substring(COUNTRY_CODE_PREFIX.length());

        // Check if the remaining part contains only digits and has appropriate length
        if (!numericPart.matches("\\d{9,12}")) {
            return false;
        }

        return true;
    }
}
