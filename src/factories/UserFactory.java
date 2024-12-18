package factories;

import models.User;

/**
 * Factory class for creating instances of User.
 */
public class UserFactory {

    // Private constructor to prevent instantiation
    private UserFactory() {
        throw new UnsupportedOperationException("UserFactory is a utility class and cannot be instantiated.");
    }
    
    /**
     * Create a new empty User instance.
     *
     * @return A new User object.
     */
    public static User createUser() {
        return new User();
    }
    
    /**
     * Create a new User instance with the specified attributes.
     *
     * @param userId      The unique identifier for the user.
     * @param username    The username of the user.
     * @param password    The password of the user.
     * @param phoneNumber The phone number of the user.
     * @param address     The address of the user.
     * @param role        The role assigned to the user.
     * @return            A new User object populated with the provided details.
     */
    public static User createUser(String userId, String username, String password, String phoneNumber, String address, String role) {
        return new User(userId, username, password, phoneNumber, address, role);
    }
}
