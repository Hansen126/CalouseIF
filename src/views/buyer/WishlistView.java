package views.buyer;

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
import models.Product;
import models.Wishlist;
import services.Response;
import views.PageManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Represents the Wishlist Page where buyers can view and manage their wishlist items.
 */
public class WishlistView implements EventHandler<ActionEvent> {

    private Scene scene;
    private VBox mainContainer;
    private TableView<Wishlist> wishlistTable;
    private PageManager pageManager;
    private Label headerLabel;

    /**
     * Constructs the ViewWishlistPage with the provided PageManager.
     *
     * @param pageManager The PageManager instance for navigation.
     */
    public WishlistView(PageManager pageManager) {
        this.pageManager = pageManager;
        initializeUI();
        initializeMenu();
        initializeTableView();
        layoutComponents();
        loadWishlist();
    }

    /**
     * Initializes the main UI components.
     */
    private void initializeUI() {
        mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(25));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Header Label
        headerLabel = new Label("Your Wishlist");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2E8B57;");
    }

    /**
     * Initializes the MenuBar with necessary menu items.
     */
    private void initializeMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Options");
        MenuItem homepageMenuItem = new MenuItem("Homepage");
        MenuItem purchaseHistoryMenuItem = new MenuItem("Purchase History");
        MenuItem logoutMenuItem = new MenuItem("Logout");
        menu.getItems().addAll(homepageMenuItem, purchaseHistoryMenuItem, logoutMenuItem);
        menuBar.getMenus().add(menu);

        // Set menu item actions
        homepageMenuItem.setOnAction(e -> pageManager.displayBuyerDashboard());
        purchaseHistoryMenuItem.setOnAction(e -> pageManager.displayPurchaseHistoryPage());
        logoutMenuItem.setOnAction(e -> pageManager.displayLoginPage());

        mainContainer.getChildren().addAll(menuBar, headerLabel);
    }

    /**
     * Initializes the TableView with necessary columns and styling.
     */
    private void initializeTableView() {
        wishlistTable = new TableView<>();
        wishlistTable.setPrefWidth(950);
        wishlistTable.setPlaceholder(new Label("No items in your wishlist."));
        wishlistTable.setStyle("-fx-background-color: #FFF8DC; -fx-text-fill: black;");

        // Define Columns
        TableColumn<Wishlist, String> idColumn = new TableColumn<>("Wishlist ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("wishlistId"));
        idColumn.setPrefWidth(150);
        idColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Wishlist, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setCellValueFactory(param -> {
            String itemName = param.getValue().getProduct().getItem().getName();
            return new ReadOnlyObjectWrapper<>(itemName);
        });
        itemNameColumn.setPrefWidth(180);
        itemNameColumn.setStyle("-fx-alignment: CENTER_LEFT; -fx-text-fill: black;");

        TableColumn<Wishlist, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(param -> {
            String category = param.getValue().getProduct().getItem().getCategory();
            return new ReadOnlyObjectWrapper<>(category);
        });
        categoryColumn.setPrefWidth(150);
        categoryColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Wishlist, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(param -> {
            String size = param.getValue().getProduct().getItem().getSize();
            return new ReadOnlyObjectWrapper<>(size);
        });
        sizeColumn.setPrefWidth(100);
        sizeColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Wishlist, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(param -> {
            BigDecimal price = param.getValue().getProduct().getItem().getPrice();
            return new ReadOnlyObjectWrapper<>(price);
        });
        priceColumn.setPrefWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        // Action Column with Remove Button
        TableColumn<Wishlist, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setPrefWidth(150);
        actionColumn.setStyle("-fx-alignment: CENTER;");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button removeBtn = new Button("Remove");
            private final HBox actionBox = new HBox(removeBtn);

            {
                actionBox.setAlignment(Pos.CENTER);
                removeBtn.setStyle("-fx-background-color: #DC143C; -fx-text-fill: white;");
                removeBtn.setOnAction(event -> {
                    Wishlist w = getTableView().getItems().get(getIndex());
                    handleRemove(w);
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

        wishlistTable.getColumns().addAll(idColumn, itemNameColumn, categoryColumn, sizeColumn, priceColumn, actionColumn);
    }

    /**
     * Arranges all UI components within the main container.
     */
    private void layoutComponents() {
        // Assemble Main Container
        mainContainer.getChildren().addAll(wishlistTable);

        // Scene Creation
        scene = new Scene(mainContainer, 1000, 700);
    }

    /**
     * Loads the wishlist items from the database and populates the TableView.
     */
    private void loadWishlist() {
        String userId = pageManager.getLoggedInUser().getId();
        Response<ArrayList<Wishlist>> res = WishlistController.getUserWishlist(userId);
        if (res.isSuccess()) {
            ObservableList<Wishlist> wishlists = FXCollections.observableArrayList(res.getData());
            wishlistTable.setItems(wishlists);
        } else {
            showAlert(AlertType.ERROR, "Data Load Error", res.getMessage());
        }
    }

    /**
     * Handles the removal of an item from the wishlist.
     *
     * @param w The wishlist item to remove.
     */
    private void handleRemove(Wishlist w) {
        // Remove Confirmation Popup
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Remove Confirmation");
        confirmationAlert.setHeaderText("Remove Item");
        confirmationAlert.setContentText("Are you sure you want to remove this item from your wishlist?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Response<Wishlist> res = WishlistController.deleteWishlistItem(w.getId());
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Wishlist Updated", "Item removed from wishlist.");
                wishlistTable.getItems().remove(w);
            } else {
                showAlert(AlertType.ERROR, "Removal Failed", res.getMessage());
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
