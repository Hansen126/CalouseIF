package models;

import java.util.ArrayList;

import factories.UserFactory;
import services.Response;
import utilities.IdGenerator;

public class User extends Model {
    
    private String userId;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private String role;
    
    private final String TABLE_NAME = "users";
    private final String PRIMARY_KEY = "userId";
    
    public User() {
        // Default constructor for future use
    }
    
    public User(String id, String username, String password, String phoneNumber, String address, String role) {
        super();
        this.userId = id;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }
    
    /**
     * Authenticate a user with the provided credentials.
     * [GUEST]
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user.
     * @return         A Response object containing the authenticated User or an error message.
     */
    public static Response<User> login(String username, String password) {
    	System.out.println("login3");
        Response<User> response = new Response<>();
        
        try {
            // Retrieve users matching the provided username
            ArrayList<User> users = UserFactory.createUser().findUsers("username", "=", username);
            if (users.isEmpty()) {
                response.setMessage("Error: User not found.");
                response.setSuccess(false);
                response.setData(null);
                System.out.println("login empty");
                return response;
            }
            
            User foundUser = users.get(0);
            // Authenticate user's password
            if (!foundUser.getPassword().equals(password)) {
                response.setMessage("Error: Incorrect password.");
                response.setSuccess(false);
                response.setData(null);
                System.out.println("password");
                return response;
            }
            
            response.setMessage("Success: User authenticated.");
            response.setSuccess(true);
            response.setData(foundUser);
            System.out.println("login success");
            return response;
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setData(null);
            System.out.println("login fail");
            return response;
        }
    }

    /**
     * Register a new user with the provided details.
     * [GUEST]
     *
     * @param username     The desired username for the new user.
     * @param password     The desired password for the new user.
     * @param phoneNumber  The phone number of the new user.
     * @param address      The address of the new user.
     * @param role         The role assigned to the new user.
     * @return             A Response object containing the newly registered User or an error message.
     */
    public static Response<User> register(String username, String password, String phoneNumber, String address, String role) {
        Response<User> response = new Response<>();
        
        try {
            // Generate a new user ID
            String newUserId = IdGenerator.generateNewId(UserFactory.createUser().getLatestUser().getId(), "USER");
            
            // Create and insert the new user into the database
            User user = UserFactory.createUser(newUserId, username, password, phoneNumber, address, role);
            user.insertUser();
            
            response.setMessage("Success: User registered successfully.");
            response.setSuccess(true);
            response.setData(user);
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
     * Validate the existence of a user account based on provided details.
     * [GUEST]
     *
     * @param username    The username of the user.
     * @param password    The password of the user.
     * @param phoneNumber The phone number of the user.
     * @param address     The address of the user.
     * @return            A Response object indicating whether the user account is valid.
     */
    public static Response<User> validateUserAccount(String username, String password, String phoneNumber, String address) {
        Response<User> response = new Response<>();
        try {
            ArrayList<User> users = UserFactory.createUser().findUsers("username", "=", username);
            if (users.isEmpty()) {
                response.setMessage("Error: User not found.");
                response.setSuccess(false);
                response.setData(null);
                return response;
            }
            
            User foundUser = users.get(0);
            response.setMessage("Success: User account is valid.");
            response.setSuccess(true);
            response.setData(foundUser);
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
        return userId;
    }
    
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public String getPRIMARY_KEY() {
		return PRIMARY_KEY;
	}

	public void setId(String id) {
        this.userId = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
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
     * Retrieve all Products associated with this User.
     *
     * @return A list of Products sold by this User.
     */
    public ArrayList<Product> getProducts() {
        return this.hasMany(Product.class, "products", this.getId(), "sellerId");
    }
    
    /**
     * Retrieve all Wishlists associated with this User.
     *
     * @return A list of Wishlists created by this User.
     */
    public ArrayList<Wishlist> getWishlists() {
        return this.hasMany(Wishlist.class, "wishlists", this.getId(), "userId");
    }
    
    /**
     * Retrieve all Transactions associated with this User.
     *
     * @return A list of Transactions made by this User.
     */
    public ArrayList<Transaction> getTransactions() {
        return this.hasMany(Transaction.class, "transactions", this.getId(), "userId");
    }
    
    /**
     * Retrieve all Offers made by this User.
     *
     * @return A list of Offers made by this User.
     */
    public ArrayList<Offer> getOffers() {
        return this.hasMany(Offer.class, "offers", this.getId(), "buyerId");
    }
    
    /**
     * Retrieve all Users.
     *
     * @return A list of all Users.
     */
    public ArrayList<User> getAllUsers() {
        Response<ArrayList<User>> response = super.fetchAll(User.class);
        return response.getData();
    }
    
    /**
     * Retrieve Users based on a specific condition.
     *
     * @param columnName The column to filter by.
     * @param operator   The operator for comparison (e.g., '=', '>', '<').
     * @param key        The value to compare against.
     * @return           A list of Users matching the condition.
     */
    public ArrayList<User> findUsers(String columnName, String operator, String key) {
        Response<ArrayList<User>> response = super.findWhere(User.class, columnName, operator, key);
        return response.getData();
    }
    
    /**
     * Update an existing User.
     *
     * @param fromKey The primary key value identifying the User to update.
     * @return        The updated User.
     */
    public User updateUser(String fromKey) {
        Response<User> response = super.updateRecord(User.class, fromKey);
        return response.getData();
    }
    
    /**
     * Insert a new User into the database.
     *
     * @return The inserted User.
     */
    public User insertUser() {
        Response<User> response = super.insertRecord(User.class);
        return response.getData();
    }
    
    /**
     * Find a User by its primary key.
     *
     * @param fromKey The primary key value of the User.
     * @return        The found User.
     */
    public User findUser(String fromKey) {
        Response<User> response = super.findByPrimaryKey(User.class, fromKey);
        return response.getData();
    }
    
    /**
     * Retrieve the most recently added User.
     *
     * @return The latest User.
     */
    public User getLatestUser() {
        Response<User> response = super.fetchLatest(User.class);
        return response.getData();
    }
    
    /**
     * Delete a User from the database.
     *
     * @param fromKey The primary key value of the User to delete.
     * @return        True if deletion was successful, else False.
     */
    public Boolean deleteUser(String fromKey) {
        Response<Boolean> response = super.deleteRecord(User.class, fromKey);
        return response.getData();
    }
    
    /**
     * Retrieve Users where the specified column's value is within the provided list.
     *
     * @param columnName The column to filter by.
     * @param listValues The list of values to include in the IN clause.
     * @return           A list of Users matching the criteria.
     */
    public ArrayList<User> findUsersIn(String columnName, ArrayList<String> listValues) {
        Response<ArrayList<User>> response = super.findWhereIn(User.class, columnName, listValues);
        return response.getData();
    }
    
}
