package views.admin;

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
import services.Response;
import views.PageManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Represents the Admin Dashboard where admins can view, approve, or decline items.
 */
public class AdminDashboardView implements EventHandler<ActionEvent> {

    private Scene scene;
    private VBox mainContainer;
    private TableView<Item> tableView;
    private PageManager pageManager;
    private Label headerLabel;
    
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem logoutMenuItem;

    /**
     * Constructs the AdminHomePage with the provided PageManager.
     *
     * @param pageManager The PageManager instance for navigation.
     */
    public AdminDashboardView(PageManager pageManager) {
        this.pageManager = pageManager;
        System.out.println("loading UI");
        initializeUI();
        System.out.println("loading tableview");
        initializeMenu();
        initializeTableView();
        System.out.println("loading layoutcomponents");
        layoutComponents();
        System.out.println("loading pendingitems");
        loadPendingItems();
    }

    /**
     * Initializes the main UI components.
     */
    private void initializeUI() {
        mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(25));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.DARKGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        // Header Label
        headerLabel = new Label("Admin Control Panel");
        headerLabel.setTextFill(Color.WHITE);
        headerLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
    }
    
    private void initializeMenu() {
        menuBar = new MenuBar();
        menu = new Menu("Options");
        logoutMenuItem = new MenuItem("Logout");
        menu.getItems().addAll(logoutMenuItem);
        menuBar.getMenus().add(menu);

        logoutMenuItem.setOnAction(e -> pageManager.displayLoginPage());
    }

    /**
     * Initializes the TableView with necessary columns and styling.
     */
    private void initializeTableView() {
        tableView = new TableView<>();
        tableView.setPrefWidth(950);
        tableView.setPlaceholder(new Label("No pending items to display."));
        tableView.setStyle("-fx-background-color: #2F4F4F; -fx-text-fill: white;");

        // Define Columns
        TableColumn<Item, String> idColumn = new TableColumn<>("Item ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        idColumn.setPrefWidth(100);
        idColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Item, String> nameColumn = new TableColumn<>("Item Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        nameColumn.setPrefWidth(180);
        nameColumn.setStyle("-fx-alignment: CENTER_LEFT; -fx-text-fill: black;");

        TableColumn<Item, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("itemCategory"));
        categoryColumn.setPrefWidth(150);
        categoryColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Item, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("itemSize"));
        sizeColumn.setPrefWidth(100);
        sizeColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Item, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        priceColumn.setPrefWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        TableColumn<Item, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("itemStatus"));
        statusColumn.setPrefWidth(120);
        statusColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");

        // Action Column with Approve and Decline Buttons
        TableColumn<Item, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setPrefWidth(200);
        actionColumn.setStyle("-fx-alignment: CENTER;");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button approveBtn = new Button("Approve");
            private final Button declineBtn = new Button("Decline");
            private final HBox actionBox = new HBox(10, approveBtn, declineBtn);

            {
                actionBox.setAlignment(Pos.CENTER);
                approveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                declineBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                approveBtn.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    handleApprove(item);
                });

                declineBtn.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    handleDecline(item);
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

        tableView.getColumns().addAll(idColumn, nameColumn, categoryColumn, sizeColumn, priceColumn, statusColumn, actionColumn);
    }

    /**
     * Arranges all UI components within the main container.
     */
    private void layoutComponents() {
        // Header Section
        HBox headerBox = new HBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10, 0, 20, 0));

        // Assemble Main Container
        mainContainer.getChildren().addAll(menuBar, headerBox, tableView);

        // Scene Creation
        scene = new Scene(mainContainer, 1000, 700);
    }

    /**
     * Loads pending items from the database and populates the TableView.
     */
    private void loadPendingItems() {
    	System.out.println("creating response");
        Response<ArrayList<Item>> response = ItemController.ViewRequestItem("Pending");
        System.out.println("response created");
        if (response.isSuccess() && response.getData() != null) {
        	System.out.println("response success and data is not null");
            ObservableList<Item> items = FXCollections.observableArrayList(response.getData());
            System.out.println("setting items in tableView");
            tableView.setItems(items);
            System.out.println("items set");
        } else {
            showAlert(AlertType.ERROR, "Data Load Error", response.getMessage() != null ? response.getMessage() : "Unable to load pending items.");
        }
    }

    /**
     * Handles the approval of an item.
     *
     * @param item The item to approve.
     */
    private void handleApprove(Item item) {
        // Confirmation Dialog
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Approve Item");
        confirmationAlert.setHeaderText("Approve Item");
        confirmationAlert.setContentText("Are you sure you want to approve the item: " + item.getName() + "?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Approve the item via the controller
            Response<Item> res = ItemController.ApproveItem(item.getId());
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Approval Successful", "The item has been approved.");
                loadPendingItems(); // Refresh the table
            } else {
                showAlert(AlertType.ERROR, "Approval Failed", res.getMessage() != null ? res.getMessage() : "Unable to approve the item.");
            }
        }
    }

    /**
     * Handles the decline of an item.
     *
     * @param item The item to decline.
     */
    private void handleDecline(Item item) {
        // Input Dialog for Decline Reason
        Dialog<String> declineDialog = new Dialog<>();
        declineDialog.setTitle("Decline Item");
        declineDialog.setHeaderText("Provide a reason for declining the item: " + item.getName());

        // Set the button types
        ButtonType declineButtonType = new ButtonType("Decline", ButtonBar.ButtonData.OK_DONE);
        declineDialog.getDialogPane().getButtonTypes().addAll(declineButtonType, ButtonType.CANCEL);

        // Create the reason input field
        TextArea reasonTextArea = new TextArea();
        reasonTextArea.setPromptText("Enter reason here...");
        reasonTextArea.setWrapText(true);
        reasonTextArea.setPrefRowCount(4);

        VBox dialogContent = new VBox(10, new Label("Reason:"), reasonTextArea);
        dialogContent.setPadding(new Insets(20, 150, 10, 10));

        declineDialog.getDialogPane().setContent(dialogContent);

        // Enable/Disable decline button based on input
        Node declineButton = declineDialog.getDialogPane().lookupButton(declineButtonType);
        declineButton.setDisable(true);

        reasonTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            declineButton.setDisable(newValue.trim().isEmpty());
        });

        // Convert the result to a string when the decline button is clicked
        declineDialog.setResultConverter(dialogButton -> {
            if (dialogButton == declineButtonType) {
                return reasonTextArea.getText().trim();
            }
            return null;
        });

        Optional<String> result = declineDialog.showAndWait();

        result.ifPresent(reason -> {
            // Decline the item via the controller
            Response<Item> res = ItemController.DeclineItem(item.getId(), reason);
            if (res.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Decline Successful", "The item has been declined.");
                loadPendingItems(); // Refresh the table
            } else {
                showAlert(AlertType.ERROR, "Decline Failed", res.getMessage() != null ? res.getMessage() : "Unable to decline the item.");
            }
        });
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

    /**
     * Retrieves the scene associated with this page.
     *
     * @return The Scene object.
     */
    public Scene getScene() {
        return scene;
    }

    @Override
    public void handle(ActionEvent event) {
        // No specific actions handled here as buttons have their own event handlers
    }
}
