package views.buyer;

import controllers.ItemController;
import controllers.TransactionController;
import controllers.WishlistController;
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
import models.Item;
import models.Offer;
import models.Product;
import models.Wishlist;
import services.Response;
import views.PageManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Represents the Buyer Dashboard where buyers can browse, purchase, offer prices, and add items to their wishlist.
 */
public class BuyerDashboardView implements EventHandler<ActionEvent> {

    private Scene scene;
    private VBox mainContainer;
    private TableView<Product> tableView;

    private Label header;

    private MenuBar menuBar;
    private Menu menu;
    private MenuItem wishlistMenuItem, purchaseHistoryMenuItem, logoutMenuItem;

    private PageManager pageManager;

    /**
     * Constructs the BuyerHomePage with the provided PageManager.
     *
     * @param pageManager The PageManager instance for navigation.
     */
    public BuyerDashboardView(PageManager pageManager) {
        this.pageManager = pageManager;
        System.out.println("BuyerHomePage");
        System.out.println("initializing UI");
        initializeUI();
        System.out.println("initializing Menu");
        initializeMenu();
        System.out.println("initializing TableView");
        initializeTableView();
        System.out.println("initializing layoutComponents");
        layoutComponents();
        System.out.println("initializing AllItems");
        loadAllItems();
        System.out.println("Success BuyerHomePage");
    }

    /**
     * Initializes the main UI components.
     */
    private void initializeUI() {
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        // Header Label
        header = new Label("Buyer Dashboard");
        header.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2E8B57;");
    }

    /**
     * Initializes the MenuBar with necessary menu items.
     */
    private void initializeMenu() {
        menuBar = new MenuBar();
        menu = new Menu("Options");
        wishlistMenuItem = new MenuItem("View Wishlist");
        purchaseHistoryMenuItem = new MenuItem("Purchase History");
        logoutMenuItem = new MenuItem("Logout");
        menu.getItems().addAll(wishlistMenuItem, purchaseHistoryMenuItem, logoutMenuItem);
        menuBar.getMenus().add(menu);

        // Set menu item actions
        wishlistMenuItem.setOnAction(e -> pageManager.displayWishlistPage());
        purchaseHistoryMenuItem.setOnAction(e -> pageManager.displayPurchaseHistoryPage());
        logoutMenuItem.setOnAction(e -> pageManager.displayLoginPage());
    }

    /**
     * Initializes the TableView with necessary columns and styling.
     */
    private void initializeTableView() {
        tableView = new TableView<>();
        tableView.setPrefWidth(950);
        tableView.setPlaceholder(new Label("No items available for browsing."));
        tableView.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black;");

        // Define Columns
        TableColumn<Product, String> idColumn = new TableColumn<>("Item ID");
        idColumn.setCellValueFactory(param -> {
            String id = param.getValue().getItem().getId();
            return new ReadOnlyObjectWrapper<>(id);
        });
        idColumn.setPrefWidth(100);
        idColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Product, String> nameColumn = new TableColumn<>("Item Name");
        nameColumn.setCellValueFactory(param -> {
            String itemName = param.getValue().getItem().getName();
            return new ReadOnlyObjectWrapper<>(itemName);
        });
        nameColumn.setPrefWidth(180);
        nameColumn.setStyle("-fx-alignment: CENTER_LEFT; -fx-text-fill: black;");

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(param -> {
            String category = param.getValue().getItem().getCategory();
            return new ReadOnlyObjectWrapper<>(category);
        });
        categoryColumn.setPrefWidth(150);
        categoryColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Product, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(param -> {
            String size = param.getValue().getItem().getSize();
            return new ReadOnlyObjectWrapper<>(size);
        });
        sizeColumn.setPrefWidth(100);
        sizeColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Product, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(param -> {
            BigDecimal price = param.getValue().getItem().getPrice();
            return new ReadOnlyObjectWrapper<>(price);
        });
        priceColumn.setPrefWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        // Action Column with Purchase, Offer Price, and Add to Wishlist Buttons
        TableColumn<Product, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setPrefWidth(300);
        actionColumn.setStyle("-fx-alignment: CENTER;");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button purchaseBtn = new Button("Purchase");
            private final Button offerBtn = new Button("Offer Price");
            private final Button wishlistBtn = new Button("Add to Wishlist");
            private final HBox actionBox = new HBox(10, purchaseBtn, offerBtn, wishlistBtn);

            {
                actionBox.setAlignment(Pos.CENTER);
                purchaseBtn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");
                offerBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white;");
                wishlistBtn.setStyle("-fx-background-color: #17A2B8; -fx-text-fill: white;");

                purchaseBtn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handlePurchase(product);
                });

                offerBtn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleOfferPrice(product);
                });

                wishlistBtn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleAddToWishlist(product);
                });
            }

            @Override
            protected void updateItem(Void unused, boolean empty) {
                super.updateItem(unused, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionBox);
                }
            }
        });

        tableView.getColumns().addAll(idColumn, nameColumn, categoryColumn, sizeColumn, priceColumn, actionColumn);
    }

    /**
     * Arranges all UI components within the main container.
     */
    private void layoutComponents() {
        // Assemble Main Container
        mainContainer.getChildren().addAll(menuBar, header, tableView);
        System.out.println("children assembled");

        // Scene Creation
        scene = new Scene(mainContainer, 1000, 700);
        System.out.println("scene created");
    }

    /**
     * Loads all available items from the database and populates the TableView.
     */
    private void loadAllItems() {
        Response<ArrayList<Product>> res = ItemController.ViewItem();
        if (res.isSuccess()) {
            ObservableList<Product> items = FXCollections.observableArrayList(res.getData());
            tableView.setItems(items);
        } else {
            showAlert(AlertType.ERROR, "Data Load Error", res.getMessage());
        }
    }


    /**
     * Handles the purchase of an item.
     *
     * @param product The product to purchase.
     */
    private void handlePurchase(Product product) {
        Item item = product.getItem();
        System.out.println("purchase 1");

        // Purchase Confirmation Alert
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Purchase Confirmation");
        confirmationAlert.setHeaderText("Confirm Purchase");
        confirmationAlert.setContentText("Are you sure you want to purchase " + item.getName() + "?");
        System.out.println("purchase 2");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        	System.out.println("purchase 3");
            // Get current authenticated user
            String userId = pageManager.getLoggedInUser().getId();

            // Handle transaction
            System.out.println("purchase 4");
            Response<models.Transaction> res = TransactionController.executePurchase(userId, product.getId());
            System.out.println("purchase 5");
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Purchase Successful", "You have successfully purchased: " + item.getName());
            } else {
                showAlert(AlertType.ERROR, "Purchase Failed", res.getMessage());
            }
        }
    }

    /**
     * Handles offering a price for an item.
     *
     * @param product The product to offer a price for.
     */
    private void handleOfferPrice(Product product) {
        // Offer price input form will pop up
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Offer Price");
        dialog.setHeaderText("Enter your offer price for " + product.getItem().getName());

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        TextField offerPriceField = new TextField();
        offerPriceField.setPromptText("Offer Price");

        VBox dialogContent = new VBox(10, new Label("Offer Price:"), offerPriceField);
        dialogContent.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(dialogContent);

        // Convert the result to a string when the submit button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return offerPriceField.getText().trim();
            }
            return null;
        });

        Optional<String> offerResult = dialog.showAndWait();
        if (offerResult.isPresent()) {
            String offerValue = offerResult.get();

            String buyerId = pageManager.getLoggedInUser().getId();
            Response<Offer> res = ItemController.OfferPrice(product.getId(), buyerId, offerValue);

            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Offer Submitted", "Your offer has been submitted successfully.");
            } else {
                showAlert(AlertType.ERROR, "Offer Failed", res.getMessage());
            }
        }
    }

    /**
     * Handles adding an item to the wishlist.
     *
     * @param product The product to add to the wishlist.
     */
    private void handleAddToWishlist(Product product) {
        // Wishlist Confirmation Popup
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Add to Wishlist");
        confirmationAlert.setHeaderText("Confirm Wishlist Addition");
        confirmationAlert.setContentText("Are you sure you want to add " + product.getItem().getName() + " to your wishlist?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String userId = pageManager.getLoggedInUser().getId();

            // Add item to wishlist
            Response<models.Wishlist> res = WishlistController.addItemToWishlist(product.getId(), userId);
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Wishlist Updated", product.getItem().getName() + " has been added to your wishlist.");
            } else {
                showAlert(AlertType.ERROR, "Wishlist Failed", res.getMessage());
            }
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
