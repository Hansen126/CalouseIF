package main;

import javafx.application.Application;

import javafx.stage.Stage;
import views.PageManager;
import views.RegisterView;
import views.buyer.BuyerDashboardView;
import views.seller.SellerDashboardView;

/**
 * Entry point for the CaLouseIF JavaFX application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the PageManager with the primary stage
            PageManager pageManager = new PageManager(primaryStage);
            // Display the Login Page
            pageManager.displayLoginPage();
            primaryStage.setTitle("CaLouseIF - Login");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
