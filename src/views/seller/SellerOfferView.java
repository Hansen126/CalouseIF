package views.seller;

import controllers.ItemController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Item;
import models.Offer;
import models.User;
import services.Response;
import views.PageManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Represents the Offered Items Page where sellers can view and manage offers made on their items.
 */
public class SellerOfferView implements EventHandler<ActionEvent> {

    private Scene scene;
    private VBox mainContainer;
    private TableView<Offer> offeredItemsTable;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem dashboardMenuItem, logoutMenuItem;
    private Label headerLabel;

    private PageManager pageManager;

    /**
     * Constructs the OfferedItemPage with the provided PageManager.
     *
     * @param pageManager The PageManager instance for navigation.
     */
    public SellerOfferView(PageManager pageManager) {
        this.pageManager = pageManager;
        initializeUI();
        initializeTableView();
        initializeMenu();
        layoutComponents();
        loadOfferedItems();
    }

    /**
     * Initializes the main UI components.
     */
    private void initializeUI() {
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(25));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Header Label
        headerLabel = new Label("Offered Items");
        headerLabel.setTextFill(Color.DARKBLUE);
        headerLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
    }

    /**
     * Initializes the MenuBar with necessary menu items.
     */
    private void initializeMenu() {
        menuBar = new MenuBar();
        menu = new Menu("Navigation");
        dashboardMenuItem = new MenuItem("Dashboard");
        logoutMenuItem = new MenuItem("Logout");

        menu.getItems().addAll(dashboardMenuItem, logoutMenuItem);
        menuBar.getMenus().add(menu);

        // Set menu item actions
        dashboardMenuItem.setOnAction(event -> pageManager.displaySellerDashboard());
        logoutMenuItem.setOnAction(e -> pageManager.displayLoginPage());
    }

    /**
     * Initializes the TableView with necessary columns and styling.
     */
    private void initializeTableView() {
        offeredItemsTable = new TableView<>();
        offeredItemsTable.setPrefWidth(950);
        offeredItemsTable.setPlaceholder(new Label("No offers available."));
        offeredItemsTable.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black;");

        // Define Columns
        TableColumn<Offer, String> offerIdColumn = new TableColumn<>("Offer ID");
        offerIdColumn.setCellValueFactory(new PropertyValueFactory<>("offerId"));
        offerIdColumn.setPrefWidth(150);
        offerIdColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Offer, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setCellValueFactory(param -> {
            String itemName = param.getValue().getProduct().getItem().getName();
            return new ReadOnlyObjectWrapper<>(itemName);
        });
        itemNameColumn.setPrefWidth(200);
        itemNameColumn.setStyle("-fx-alignment: CENTER_LEFT; -fx-text-fill: black;");

        TableColumn<Offer, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(param -> {
            String category = param.getValue().getProduct().getItem().getCategory();
            return new ReadOnlyObjectWrapper<>(category);
        });
        categoryColumn.setPrefWidth(150);
        categoryColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Offer, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(param -> {
            String size = param.getValue().getProduct().getItem().getSize();
            return new ReadOnlyObjectWrapper<>(size);
        });
        sizeColumn.setPrefWidth(100);
        sizeColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Offer, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(param -> {
            BigDecimal price = param.getValue().getProduct().getItem().getPrice();
            return new ReadOnlyObjectWrapper<>(price);
        });
        priceColumn.setPrefWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Offer, BigDecimal> offerPriceColumn = new TableColumn<>("Offered Price");
        offerPriceColumn.setCellValueFactory(new PropertyValueFactory<>("offerPrice"));
        offerPriceColumn.setPrefWidth(150);
        offerPriceColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        // Action Column with Accept and Decline Buttons
        TableColumn<Offer, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setPrefWidth(200);
        actionColumn.setStyle("-fx-alignment: CENTER;");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button acceptBtn = new Button("Accept");
            private final Button declineBtn = new Button("Decline");
            private final HBox actionBox = new HBox(10, acceptBtn, declineBtn);

            {
                actionBox.setAlignment(Pos.CENTER);
                acceptBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                declineBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                acceptBtn.setOnAction(event -> {
                    Offer offer = getTableView().getItems().get(getIndex());
                    handleAccept(offer);
                });

                declineBtn.setOnAction(event -> {
                    Offer offer = getTableView().getItems().get(getIndex());
                    handleDecline(offer);
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

        // Add columns to table
        offeredItemsTable.getColumns().addAll(
                offerIdColumn,
                itemNameColumn,
                categoryColumn,
                sizeColumn,
                priceColumn,
                offerPriceColumn,
                actionColumn
        );
    }

    /**
     * Arranges all UI components within the main container.
     */
    private void layoutComponents() {
        // Assemble Main Container
        mainContainer.getChildren().addAll(menuBar, headerLabel, offeredItemsTable);

        // Scene Creation
        scene = new Scene(mainContainer, 1000, 700);
    }

    /**
     * Loads offered items from the database and populates the TableView.
     */
    private void loadOfferedItems() {
    	System.out.println("offered: load");
        // Fetch offered items from the database
        User currentUser = pageManager.getLoggedInUser();
        String sellerId = currentUser.getId();
        System.out.println("sellerid: " + sellerId);
        Response<ArrayList<Offer>> res = ItemController.ViewOfferItem(sellerId);
        System.out.println("offered: res created");
        if (res.isSuccess() && res.getData() != null) {
            ObservableList<Offer> offers = FXCollections.observableArrayList(res.getData());
            offeredItemsTable.setItems(offers);
        } else {
            showAlert(AlertType.ERROR, "Data Load Error", res.getMessage() != null ? res.getMessage() : "Unable to load offered items.");
        }
    }

    /**
     * Handles the acceptance of an offer.
     *
     * @param offer The offer to accept.
     */
    private void handleAccept(Offer offer) {
        // Confirmation Dialog
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Acceptance");
        confirmationAlert.setHeaderText("Accept Offer");
        confirmationAlert.setContentText("Are you sure you want to accept this offer?");
        
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Call controller to accept the offer
            Response<Offer> res = ItemController.AcceptOffer(offer.getId());
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Success", "Offer accepted successfully.");
                loadOfferedItems(); // Refresh the table
            } else {
                showAlert(AlertType.ERROR, "Error", res.getMessage() != null ? res.getMessage() : "Failed to accept offer.");
            }
        }
    }

    /**
     * Handles the declination of an offer.
     *
     * @param offer The offer to decline.
     */
    private void handleDecline(Offer offer) {
        // Create a popup input form to enter the reason for declining
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Decline Offer");
        dialog.setHeaderText("Enter Reason for Declining the Offer");

        // Set the button types
        ButtonType declineButtonType = new ButtonType("Decline", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(declineButtonType, ButtonType.CANCEL);

        // Create the reason input field
        TextArea reasonTextArea = new TextArea();
        reasonTextArea.setPromptText("Enter reason for declining the offer...");
        reasonTextArea.setWrapText(true);
        reasonTextArea.setPrefRowCount(4);

        VBox dialogContent = new VBox(10, new Label("Reason:"), reasonTextArea);
        dialogContent.setPadding(new Insets(20, 150, 10, 10));

        dialog.getDialogPane().setContent(dialogContent);

        // Enable/Disable Decline button based on input
        Node declineButton = dialog.getDialogPane().lookupButton(declineButtonType);
        declineButton.setDisable(true);

        reasonTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            declineButton.setDisable(newValue.trim().isEmpty());
        });

        // Convert the result to the reason string when Decline button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == declineButtonType) {
                return reasonTextArea.getText().trim();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(reason -> {
            // Call controller to decline the offer with the provided reason
            Response<Offer> res = ItemController.DeclineOffer(offer.getId(), reason);
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Success", "Offer declined successfully.");
                loadOfferedItems(); // Refresh the table
            } else {
                showAlert(AlertType.ERROR, "Error", res.getMessage() != null ? res.getMessage() : "Failed to decline offer.");
            }
        });
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
