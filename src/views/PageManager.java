package views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;
import views.buyer.BuyerDashboardView;
import views.buyer.PurchaseView;
import views.buyer.WishlistView;
import views.seller.SellerOfferView;
import views.seller.SellerDashboardView;
import views.admin.AdminDashboardView;
import views.LoginView;
import views.RegisterView;

/**
 * Manages navigation between different scenes/pages within the application.
 */
public class PageManager {

    private Stage primaryStage;
    private Scene loginScene;
    private Scene registerScene;

    // Save logged-in user
    private User loggedInUser;

    public PageManager(Stage stage) {
        this.primaryStage = stage;
        initializePages();
    }

    /**
     * Initializes static pages like Login and Register.
     */
    private void initializePages() {
        // Initialize Login and Register Pages
        LoginView loginPage = new LoginView(this);
        RegisterView registerPage = new RegisterView(this);

        loginScene = loginPage.getScene();
        registerScene = registerPage.getScene();
    }

    /**
     * Displays the Login Page.
     */
    public void displayLoginPage() {
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }

    /**
     * Displays the Registration Page.
     */
    public void displayRegisterPage() {
        primaryStage.setScene(registerScene);
        primaryStage.setTitle("User Registration");
        primaryStage.show();
    }

    /**
     * Displays the Admin Dashboard.
     */
    public void displayAdminDashboard() {
        AdminDashboardView adminDashboard = new AdminDashboardView(this);
        Scene adminScene = adminDashboard.getScene();
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }

    /**
     * Displays the Seller Dashboard.
     */
    public void displaySellerDashboard() {
        SellerDashboardView sellerDashboard = new SellerDashboardView(this);
        Scene sellerScene = sellerDashboard.getScene();
        primaryStage.setScene(sellerScene);
        primaryStage.setTitle("Seller Dashboard");
        primaryStage.show();
    }

    /**
     * Displays the Buyer Dashboard.
     */
    public void displayBuyerDashboard() {
        BuyerDashboardView buyerDashboard = new BuyerDashboardView(this);
        Scene buyerScene = buyerDashboard.getScene();
        primaryStage.setScene(buyerScene);
        primaryStage.setTitle("Buyer Dashboard");
        primaryStage.show();
    }

    /**
     * Displays the Wishlist Page for the logged-in user.
     */
    public void displayWishlistPage() {
        if (loggedInUser == null) {
            displayLoginPage();
            return;
        }
        WishlistView wishlistPage = new WishlistView(this);
        Scene wishlistScene = wishlistPage.getScene();
        primaryStage.setScene(wishlistScene);
        primaryStage.setTitle("Your Wishlist");
        primaryStage.show();
    }

    /**
     * Displays the Purchase History Page for the logged-in user.
     */
    public void displayPurchaseHistoryPage() {
        if (loggedInUser == null) {
            displayLoginPage();
            return;
        }
        PurchaseView purchaseHistoryPage = new PurchaseView(this);
        Scene purchaseScene = purchaseHistoryPage.getScene();
        primaryStage.setScene(purchaseScene);
        primaryStage.setTitle("Purchase History");
        primaryStage.show();
    }

    /**
     * Displays the Offered Items Page for the seller.
     */
    public void displayOfferedItemsPage() {
        SellerOfferView offeredItemsPage = new SellerOfferView(this);
        Scene offeredScene = offeredItemsPage.getScene();
        primaryStage.setScene(offeredScene);
        primaryStage.setTitle("Offered Items");
        primaryStage.show();
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user The user who has logged in.
     */
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The logged-in user.
     */
    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    /**
     * Clears the logged-in user information.
     */
    public void clearLoggedInUser() {
        this.loggedInUser = null;
    }
}
