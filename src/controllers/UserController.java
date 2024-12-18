package controllers;

import models.User;
import services.Response;
// Removed dependencies on utils by integrating validation directly
// import utils.ValidatePassword;
// import utils.ValidatePhoneNumber;

public class UserController {

    public UserController() {
        // Constructor reserved for future enhancements
    }
    
    /**
     * Authenticate a user based on provided credentials.
     * [GUEST]
     *
     * @param username The user's chosen username.
     * @param password The user's password.
     * @return         A Response object containing the authenticated User if successful.
     */
    public static Response<User> authenticateUser(String username, String password) {
        Response<User> response = new Response<>();
        System.out.println("login2");
        
        // Input Validation
        if(username == null || username.trim().isEmpty()) {
            response.setMessage("Username must not be empty!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        if(password == null || password.trim().isEmpty()) {
            response.setMessage("Password must not be empty!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        System.out.println("login");
        // Proceed with authentication
        return User.login(username, password);
    }
    
    /**
     * Register a new user with the provided details.
     * [GUEST]
     *
     * @param username     Desired username for the new account.
     * @param password     Password adhering to security requirements.
     * @param phoneNumber  Contact number following the specified format.
     * @param address      Residential address of the user.
     * @param role         Role assigned to the user (e.g., Seller).
     * @return             A Response object containing the newly registered User if successful.
     */
    public static Response<User> registerUser(String username, String password, String phoneNumber, String address, String role) {
        Response<User> response = new Response<>();
        
        // Input Validations
        if(username == null || username.trim().isEmpty()) {
            response.setMessage("Username must not be empty!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        if(password == null || password.trim().isEmpty()) {
            response.setMessage("Password must not be empty!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        if(phoneNumber == null || phoneNumber.trim().isEmpty()) {
            response.setMessage("Phone Number must not be empty!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        if(address == null || address.trim().isEmpty()) {
            response.setMessage("Address must not be empty!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        if(role == null || role.trim().isEmpty()) {
            response.setMessage("Role must be selected!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        if(username.length() < 3) {
            response.setMessage("Username must be at least 3 characters long!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        if(password.length() < 8) {
            response.setMessage("Password must contain a minimum of 8 characters!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        if(phoneNumber.length() < 12) { // Assuming +62 is included in the count
            response.setMessage("Phone Number must include '+62' followed by at least 10 digits!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        // Check for unique username
        Response<User> validationResponse = User.validateUserAccount(username, password, phoneNumber, address);
        if(validationResponse.isSuccess()) { // Assuming true means validation failed for uniqueness
            response.setMessage("Username already exists! Please choose a different one.");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        // Password complexity validation
        if(!isPasswordComplex(password)) {
            response.setMessage("Password must include at least one special character (!, @, #, $, %, ^, &, *)!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        // Phone number format validation
        if(!isValidPhoneNumber(phoneNumber)) {
            response.setMessage("Phone Number must start with '+62' and contain only digits thereafter!");
            response.setSuccess(false);
            response.setData(null);
            return response;
        }
        
        // Proceed with user registration
        return User.register(username, password, phoneNumber, address, role);
    }
    
    /**
     * Validates the complexity of the provided password.
     *
     * @param password The password to validate.
     * @return         True if the password contains at least one special character, else false.
     */
    private static boolean isPasswordComplex(String password) {
        String specialCharacters = "!@#$%^&*";
        for(char ch : password.toCharArray()) {
            if(specialCharacters.indexOf(ch) >= 0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validates the format of the provided phone number.
     *
     * @param phoneNumber The phone number to validate.
     * @return            True if the phone number starts with '+62' followed by digits, else false.
     */
    private static boolean isValidPhoneNumber(String phoneNumber) {
        if(!phoneNumber.startsWith("+62")) {
            return false;
        }
        String digits = phoneNumber.substring(3);
        for(char ch : digits.toCharArray()) {
            if(!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Placeholder for account validation logic.
     * [DEPRECATED]
     *
     * @param username    The user's chosen username.
     * @param password    The user's password.
     * @param phoneNumber The user's phone number.
     * @param address     The user's address.
     * @return            Always returns null as the method is not implemented.
     */
    @Deprecated
    public static Response<User> validateAccount(String username, String password, String phoneNumber, String address) {
        // Method intentionally left unimplemented
        return null;
    }
}
