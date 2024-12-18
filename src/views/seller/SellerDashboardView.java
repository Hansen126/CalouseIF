package views.seller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import controllers.ItemController;
import javafx.application.Platform;
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
import models.User;
import services.Response;
import views.PageManager;

/**
 * Represents the Seller Dashboard where sellers can add, view, edit, and delete their items.
 */
public class SellerDashboardView implements EventHandler<ActionEvent> {

    private Scene scene;
    private VBox mainContainer;
    private GridPane formPane;
    private TableView<Item> table;
    private TextField priceField;
    private TextField itemNameField, itemCategoryField, itemSizeField;
    private Label header, itemNameLabel, itemCategoryLabel, itemSizeLabel, itemPriceLabel;
    private HBox buttonContainer;
    private Button submitButton;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem offeredItemsMenuItem, logoutMenuItem;
    private Label headerLabel;

    private String tempId;
    private PageManager pageManager;

    /**
     * Constructs the SellerHomePage with the provided PageManager.
     *
     * @param pageManager The PageManager instance for navigation.
     */
    public SellerDashboardView(PageManager pageManager) {
        this.pageManager = pageManager;
        
        initializeUI();
        initializeTableView();
        initializeMenu();
        System.out.println("initializing 1");
        layoutComponents();
        System.out.println("initializing 2");
        setEventHandler();
        System.out.println("initializing 3");
        loadSellerItems();
        System.out.println("complete");
    }

    /**
     * Initializes the main UI components.
     */
    private void initializeUI() {
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(25));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Header Label
        headerLabel = new Label("SELLER DASHBOARD");
        headerLabel.setTextFill(Color.DARKGREEN);
        headerLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
    }

    /**
     * Initializes the MenuBar with necessary menu items.
     */
    private void initializeMenu() {
        menuBar = new MenuBar();
        menu = new Menu("Options");
        offeredItemsMenuItem = new MenuItem("Offered Items");
        logoutMenuItem = new MenuItem("Logout");

        menu.getItems().addAll(offeredItemsMenuItem, logoutMenuItem);
        menuBar.getMenus().add(menu);

        // Set menu item actions
        offeredItemsMenuItem.setOnAction(event -> pageManager.displayOfferedItemsPage());
        logoutMenuItem.setOnAction(e -> pageManager.displayLoginPage());
    }

    /**
     * Initializes the input form and TableView.
     */
    private void initializeTableView() {
        // Form Labels
        itemNameLabel = new Label("Item Name:");
        itemCategoryLabel = new Label("Item Category:");
        itemSizeLabel = new Label("Item Size:");
        itemPriceLabel = new Label("Item Price:");

        // Input Fields
        itemNameField = new TextField();
        itemNameField.setPromptText("Enter item name");
        itemCategoryField = new TextField();
        itemCategoryField.setPromptText("Enter item category");
        itemSizeField = new TextField();
        itemSizeField.setPromptText("Enter item size");
        priceField = new TextField();
        priceField.setPromptText("Enter item price");

        // Buttons
        submitButton = new Button("Save");
        submitButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold;");
        buttonContainer = new HBox(10, submitButton);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);

        // Form Layout
        formPane = new GridPane();
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setPadding(new Insets(10));

        formPane.add(itemNameLabel, 0, 0);
        formPane.add(itemNameField, 1, 0);
        formPane.add(itemCategoryLabel, 0, 1);
        formPane.add(itemCategoryField, 1, 1);
        formPane.add(itemSizeLabel, 0, 2);
        formPane.add(itemSizeField, 1, 2);
        formPane.add(itemPriceLabel, 0, 3);
        formPane.add(priceField, 1, 3);
        formPane.add(buttonContainer, 1, 4);

        // TableView Initialization
        table = new TableView<>();
        table.setPrefWidth(800);
        table.setPlaceholder(new Label("No items available."));
        table.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black;");

        // Define Columns
        TableColumn<Item, String> itemIDColumn = new TableColumn<>("Item ID");
        itemIDColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemIDColumn.setPrefWidth(150);
        itemIDColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Item, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemNameColumn.setPrefWidth(200);
        itemNameColumn.setStyle("-fx-alignment: CENTER_LEFT; -fx-text-fill: black;");

        TableColumn<Item, String> itemCategoryColumn = new TableColumn<>("Category");
        itemCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("itemCategory"));
        itemCategoryColumn.setPrefWidth(150);
        itemCategoryColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Item, String> itemSizeColumn = new TableColumn<>("Size");
        itemSizeColumn.setCellValueFactory(new PropertyValueFactory<>("itemSize"));
        itemSizeColumn.setPrefWidth(100);
        itemSizeColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Item, BigDecimal> itemPriceColumn = new TableColumn<>("Price");
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        itemPriceColumn.setPrefWidth(100);
        itemPriceColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        // Action Column with Edit and Delete Buttons
        TableColumn<Item, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setPrefWidth(150);
        actionColumn.setStyle("-fx-alignment: CENTER;");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actionBox = new HBox(10, editButton, deleteButton);

            {
                actionBox.setAlignment(Pos.CENTER);
                editButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
                deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold;");

                editButton.setOnAction(event -> {
                    Item selectedItem = getTableView().getItems().get(getIndex());
                    handleEdit(selectedItem);
                });

                deleteButton.setOnAction(event -> {
                    Item selectedItem = getTableView().getItems().get(getIndex());
                    handleDelete(selectedItem);
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
        table.getColumns().addAll(itemIDColumn, itemNameColumn, itemCategoryColumn, itemSizeColumn, itemPriceColumn, actionColumn);
    }


    /**
     * Handles the submission of a new item.
     *
     * @param event The action event triggered by the submit button.
     */
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == submitButton) {
            // Gather necessary values
            String itemName = itemNameField.getText().trim();
            String itemCategory = itemCategoryField.getText().trim();
            String itemSize = itemSizeField.getText().trim();
            String itemPrice = priceField.getText().trim();

            // Basic input validation
            if (itemName.isEmpty() || itemCategory.isEmpty() || itemSize.isEmpty() || itemPrice.isEmpty()) {
                showAlert(AlertType.WARNING, "Input Required", "Please fill in all fields.");
                return;
            }

            // Validate price format
            try {
                new BigDecimal(itemPrice);
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid number for the price.");
                return;
            }

            // Get current authenticated user
            User currentUser = pageManager.getLoggedInUser();
            String sellerId = currentUser.getId();

            // Call controller to upload the item
            Response<Item> res = ItemController.UploadItem(sellerId, itemName, itemCategory, itemSize, itemPrice);
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Success", "Item added successfully.");
                // Clear form fields
                itemNameField.clear();
                itemCategoryField.clear();
                itemSizeField.clear();
                priceField.clear();
                // Refresh table
                loadSellerItems();
            } else {
                showAlert(AlertType.ERROR, "Error", res.getMessage() != null ? res.getMessage() : "Failed to add item.");
            }
        }
    }

    /**
     * Handles the editing of an existing item.
     *
     * @param item The item to edit.
     */
    private void handleEdit(Item item) {
        // Edit form popup
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Item");
        dialog.setHeaderText("Edit Item Details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create edit form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField itemNameEdit = new TextField();
        itemNameEdit.setText(item.getName());
        TextField itemCategoryEdit = new TextField();
        itemCategoryEdit.setText(item.getCategory());
        TextField itemSizeEdit = new TextField();
        itemSizeEdit.setText(item.getSize());
        TextField priceEditField = new TextField();
        priceEditField.setText(item.getPrice().toString());

        grid.add(new Label("Item Name:"), 0, 0);
        grid.add(itemNameEdit, 1, 0);
        grid.add(new Label("Item Category:"), 0, 1);
        grid.add(itemCategoryEdit, 1, 1);
        grid.add(new Label("Item Size:"), 0, 2);
        grid.add(itemSizeEdit, 1, 2);
        grid.add(new Label("Item Price:"), 0, 3);
        grid.add(priceEditField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> itemNameEdit.requestFocus());

        // Convert the result to the updated item details when Save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return saveButtonType;
            }
            return null;
        });

        Optional<ButtonType> result = dialog.showAndWait();

        // Execute updated values
        if (result.isPresent() && result.get() == saveButtonType) {
            String editedName = itemNameEdit.getText().trim();
            String editedCategory = itemCategoryEdit.getText().trim();
            String editedSize = itemSizeEdit.getText().trim();
            String editedPrice = priceEditField.getText().trim();

            // Basic input validation
            if (editedName.isEmpty() || editedCategory.isEmpty() || editedSize.isEmpty() || editedPrice.isEmpty()) {
                showAlert(AlertType.WARNING, "Input Required", "Please fill in all fields.");
                return;
            }

            // Validate price format
            try {
                new BigDecimal(editedPrice);
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid number for the price.");
                return;
            }

            // Call controller to edit the item
            Response<Item> res = ItemController.EditItem(item.getId(), editedName, editedCategory, editedSize, editedPrice);
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Success", "Item updated successfully.");
                loadSellerItems(); // Refresh the table
            } else {
                showAlert(AlertType.ERROR, "Error", res.getMessage() != null ? res.getMessage() : "Failed to update item.");
            }
        }
    }

    /**
     * Handles the deletion of an existing item.
     *
     * @param item The item to delete.
     */
    private void handleDelete(Item item) {
        // Delete confirmation popup
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Delete Item");
        confirmationAlert.setContentText("Are you sure you want to delete the item: " + item.getName() + "?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Execute deletion of item
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Response<Item> res = ItemController.DeleteItem(item.getId());
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Success", "Item deleted successfully.");
                loadSellerItems(); // Refresh the table
            } else {
                showAlert(AlertType.ERROR, "Error", res.getMessage() != null ? res.getMessage() : "Failed to delete item.");
            }
        }
    }

    /**
     * Arranges all UI components within the main container.
     */
    private void layoutComponents() {
    	System.out.println("seller layout component");
        // Assemble Main Container
        mainContainer.getChildren().addAll(menuBar, headerLabel, formPane, table);

        // Scene Creation
        scene = new Scene(mainContainer, 1000, 700);
    }

    /**
     * Sets event handlers for interactive components.
     */
    private void setEventHandler() {
        submitButton.setOnAction(this);
    }

    /**
     * Loads the seller's items from the database and populates the TableView.
     */
    private void loadSellerItems() {
        // Fetch seller items from the database
    	System.out.println("leadseller items 1");
        User currentUser = pageManager.getLoggedInUser();
        System.out.println("leadseller items 1.1");
        String sellerId = currentUser.getId();
        System.out.println("leadseller items 1.2");
        Response<ArrayList<Item>> res = ItemController.ViewSellerItem(sellerId);
        System.out.println("leadseller items 2");
        if (res.isSuccess() && res.getData() != null) {
        	System.out.println("leadseller items 3");
            ObservableList<Item> items = FXCollections.observableArrayList(res.getData());
            table.setItems(items);
            System.out.println("leadseller items 4");
        } else {
        	System.out.println("leadseller items 5");
            showAlert(AlertType.ERROR, "Data Load Error", res.getMessage() != null ? res.getMessage() : "Unable to load seller items.");
        }
    }

    /**
     * Handles displaying an alert dialog to the user.
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

    /**
     * Retrieves the scene associated with this page.
     *
     * @return The Scene object.
     */
    public Scene getScene() {
        return scene;
    }
}
