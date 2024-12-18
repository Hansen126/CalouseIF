package views;

import controllers.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.User;
import services.Response;

/**
 * Represents the user registration page where new users can create an account.
 */
public class RegisterView implements EventHandler<ActionEvent> {

    private Scene scene;
    private VBox mainContainer;

    private Label titleLbl;
    private Label usernameLbl, passwordLbl, phoneNumberLbl, addressLbl, roleLbl;
    private TextField usernameTF, phoneNumberTF, addressTF;
    private PasswordField passwordPF;
    private Button registerBtn;
    private RadioButton buyerRB, sellerRB;
    private ToggleGroup roleGroup;
    private Hyperlink loginLink; 

    private PageManager pageManager;

    public RegisterView(PageManager pageManager) {
        this.pageManager = pageManager;
        initialize();
        addComponents();
        setEventHandlers();
    }

    private void initialize() {
        mainContainer = new VBox(25);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Title Label
        titleLbl = new Label("Create Your Account");
        titleLbl.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: darkgreen;");
    }

    private void addComponents() {
        // Form GridPane
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.CENTER);

        // Define Column Constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        col1.setPrefWidth(130);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.LEFT);
        col2.setPrefWidth(250);

        formGrid.getColumnConstraints().addAll(col1, col2);

        // Username
        usernameLbl = new Label("Username:");
        usernameLbl.setStyle("-fx-font-size: 14px;");
        usernameTF = new TextField();
        usernameTF.setPromptText("Choose a username");
        usernameTF.setPrefWidth(250);

        // Password
        passwordLbl = new Label("Password:");
        passwordLbl.setStyle("-fx-font-size: 14px;");
        passwordPF = new PasswordField();
        passwordPF.setPromptText("Create a password");
        passwordPF.setPrefWidth(250);

        // Phone Number
        phoneNumberLbl = new Label("Phone Number:");
        phoneNumberLbl.setStyle("-fx-font-size: 14px;");
        phoneNumberTF = new TextField();
        phoneNumberTF.setPromptText("e.g., +628123456789");
        phoneNumberTF.setPrefWidth(250);

        // Address
        addressLbl = new Label("Address:");
        addressLbl.setStyle("-fx-font-size: 14px;");
        addressTF = new TextField();
        addressTF.setPromptText("Enter your address");
        addressTF.setPrefWidth(250);

        // Role
        roleLbl = new Label("Role:");
        roleLbl.setStyle("-fx-font-size: 14px;");
        buyerRB = new RadioButton("Buyer");
        sellerRB = new RadioButton("Seller");
        roleGroup = new ToggleGroup();
        buyerRB.setToggleGroup(roleGroup);
        sellerRB.setToggleGroup(roleGroup);
        buyerRB.setSelected(true); 

        HBox roleBox = new HBox(20, buyerRB, sellerRB);
        roleBox.setAlignment(Pos.CENTER_LEFT);

        // Add to GridPane
        formGrid.add(usernameLbl, 0, 0);
        formGrid.add(usernameTF, 1, 0);

        formGrid.add(passwordLbl, 0, 1);
        formGrid.add(passwordPF, 1, 1);

        formGrid.add(phoneNumberLbl, 0, 2);
        formGrid.add(phoneNumberTF, 1, 2);

        formGrid.add(addressLbl, 0, 3);
        formGrid.add(addressTF, 1, 3);

        formGrid.add(roleLbl, 0, 4);
        formGrid.add(roleBox, 1, 4);

        // Register Button
        registerBtn = new Button("Sign Up");
        registerBtn.setStyle("-fx-background-color: darkgreen; -fx-text-fill: white;");
        registerBtn.setPrefWidth(120);

        // Login Hyperlink
        loginLink = new Hyperlink("Already have an account? Login here");
        loginLink.setStyle("-fx-text-fill: darkgreen; -fx-font-size: 12px;");

        // HBox for Button and Link
        HBox actionBox = new HBox(25, loginLink, registerBtn);
        actionBox.setAlignment(Pos.CENTER);

        // Assemble Main Container
        mainContainer.getChildren().addAll(titleLbl, formGrid, actionBox);

        // Scene Creation
        scene = new Scene(mainContainer, 550, 500);
    }

    /**
     * Sets event handlers for interactive components.
     */
    private void setEventHandlers() {
        registerBtn.setOnAction(this);

        loginLink.setOnAction(event -> {
            // Navigate back to login page
            pageManager.displayLoginPage();
        });
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == registerBtn) {
            String username = usernameTF.getText().trim();
            String password = passwordPF.getText().trim();
            String phone = phoneNumberTF.getText().trim();
            String address = addressTF.getText().trim();
            String role = buyerRB.isSelected() ? "buyer" : "seller";

            // Basic input validation
            if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Required", "Please fill in all fields.");
                return;
            }

            // Additional validations can be added here (e.g., password strength, phone number format)

            // Register Controller
            Response<User> res = UserController.registerUser(username, password, phone, address, role);
            if (res.isSuccess()) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Your account has been created. Please login.");
                pageManager.displayLoginPage(); 
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", res.getMessage() != null ? res.getMessage() : "An error occurred during registration.");
            }
        }
    }

    /**
     * Displays an alert dialog to the user.
     *
     * @param type    The type of alert.
     * @param title   The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(mainContainer.getScene().getWindow()); // Set the owner to current window
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
