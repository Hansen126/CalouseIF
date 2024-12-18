package views.buyer;

import controllers.TransactionController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Transaction;
import services.Response;
import views.PageManager;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Represents the Purchase History Page where buyers can view their past transactions.
 */
public class PurchaseView implements EventHandler<ActionEvent> {

    private Scene scene;
    private VBox mainContainer;
    private TableView<Transaction> transactionTable;
    private PageManager pageManager;
    private Label headerLabel;

    /**
     * Constructs the ViewPurchasePage with the provided PageManager.
     *
     * @param pageManager The PageManager instance for navigation.
     */
    public PurchaseView(PageManager pageManager) {
        this.pageManager = pageManager;
        initializeUI();
        initializeMenu();
        initializeTableView();
        layoutComponents();
        loadPurchaseHistory();
    }

    /**
     * Initializes the main UI components.
     */
    private void initializeUI() {
        mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(25));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Header Label
        headerLabel = new Label("Your Purchase History");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #8B0000;");
    }

    /**
     * Initializes the MenuBar with necessary menu items.
     */
    private void initializeMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Navigation");
        MenuItem homepageMenuItem = new MenuItem("Homepage");
        MenuItem wishlistMenuItem = new MenuItem("Wishlist");
        MenuItem logoutMenuItem = new MenuItem("Logout");
        menu.getItems().addAll(homepageMenuItem, wishlistMenuItem, logoutMenuItem);
        menuBar.getMenus().add(menu);

        // Set menu item actions
        homepageMenuItem.setOnAction(e -> pageManager.displayBuyerDashboard());
        wishlistMenuItem.setOnAction(e -> pageManager.displayWishlistPage());
        logoutMenuItem.setOnAction(e -> pageManager.displayLoginPage());

        mainContainer.getChildren().addAll(menuBar, headerLabel);
    }

    /**
     * Initializes the TableView with necessary columns and styling.
     */
    private void initializeTableView() {
        transactionTable = new TableView<>();
        transactionTable.setPrefWidth(950);
        transactionTable.setPlaceholder(new Label("No purchase history available."));
        transactionTable.setStyle("-fx-background-color: #FFF8DC; -fx-text-fill: black;");

        // Define Columns
        TableColumn<Transaction, String> idColumn = new TableColumn<>("Transaction ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        idColumn.setPrefWidth(150);
        idColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Transaction, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setCellValueFactory(param -> {
            String itemName = param.getValue().getProduct().getItem().getName();
            return new ReadOnlyObjectWrapper<>(itemName);
        });
        itemNameColumn.setPrefWidth(180);
        itemNameColumn.setStyle("-fx-alignment: CENTER_LEFT; -fx-text-fill: black;");

        TableColumn<Transaction, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(param -> {
            String category = param.getValue().getProduct().getItem().getCategory();
            return new ReadOnlyObjectWrapper<>(category);
        });
        categoryColumn.setPrefWidth(150);
        categoryColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Transaction, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(param -> {
            String size = param.getValue().getProduct().getItem().getSize();
            return new ReadOnlyObjectWrapper<>(size);
        });
        sizeColumn.setPrefWidth(100);
        sizeColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Transaction, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(param -> {
            BigDecimal price = param.getValue().getProduct().getItem().getPrice();
            return new ReadOnlyObjectWrapper<>(price);
        });
        priceColumn.setPrefWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        // Set column widths and styles
        idColumn.setMinWidth(150);
        itemNameColumn.setMinWidth(180);
        categoryColumn.setMinWidth(150);
        sizeColumn.setMinWidth(100);
        priceColumn.setMinWidth(100);

        transactionTable.getColumns().addAll(idColumn, itemNameColumn, categoryColumn, sizeColumn, priceColumn);
    }

    /**
     * Arranges all UI components within the main container.
     */
    private void layoutComponents() {
        // Assemble Main Container
        mainContainer.getChildren().addAll(transactionTable);

        // Scene Creation
        scene = new Scene(mainContainer, 1000, 700);
    }

    /**
     * Loads the purchase history from the database and populates the TableView.
     */
    private void loadPurchaseHistory() {
        String userId = pageManager.getLoggedInUser().getId();
        Response<ArrayList<Transaction>> res = TransactionController.getPurchaseHistory(userId);
        if (res.isSuccess()) {
            ObservableList<Transaction> transactions = FXCollections.observableArrayList(res.getData());
            transactionTable.setItems(transactions);
        } else {
            showAlert(AlertType.ERROR, "Data Load Error", res.getMessage());
        }
    }

    @Override
    public void handle(ActionEvent event) {
        // No specific actions handled here as buttons have their own event handlers
    }

    /**
     * Retrieves the scene associated with this page.
     *
     * @return The Scene object.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Displays an alert dialog to the user.
     *
     * @param type    The type of alert.
     * @param title   The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(mainContainer.getScene().getWindow());
        alert.showAndWait();
    }
}
