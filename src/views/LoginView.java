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

public class LoginView implements EventHandler<ActionEvent> {

    private Scene scene;
    private VBox mainContainer;

    private Label titleLbl;
    private Label usernameLbl, passwordLbl;
    private TextField usernameTF;
    private PasswordField passwordPF;
    private Button loginBtn;
    private Hyperlink registerLink; 
    private Label nameLbl;
    
    private PageManager pageManager;

    public LoginView(PageManager pageManager) {
        this.pageManager = pageManager;
        initialize();
        addComponents();
        setEventHandler();
    }

    private void initialize() {
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Title Label
        titleLbl = new Label("Welcome to CalouseIF!");
        titleLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: darkblue;");
        
        nameLbl = new Label("Created by\nHansen - 2602096760\nKenneth Lie - 2602078423");
        nameLbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: darkblue; -fx-text-alignment: center;");
    }

    private void addComponents() {
        // Form GridPane
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);

        // Define Column Constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        col1.setPrefWidth(100);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.LEFT);
        col2.setPrefWidth(200);

        formGrid.getColumnConstraints().addAll(col1, col2);

        // Username
        usernameLbl = new Label("Username:");
        usernameLbl.setStyle("-fx-font-size: 14px;");
        usernameTF = new TextField();
        usernameTF.setPromptText("Enter username");
        usernameTF.setPrefWidth(200);

        // Password
        passwordLbl = new Label("Password:");
        passwordLbl.setStyle("-fx-font-size: 14px;");
        passwordPF = new PasswordField();
        passwordPF.setPromptText("Enter password");
        passwordPF.setPrefWidth(200);

        // Add to GridPane
        formGrid.add(usernameLbl, 0, 0);
        formGrid.add(usernameTF, 1, 0);
        formGrid.add(passwordLbl, 0, 1);
        formGrid.add(passwordPF, 1, 1);

        // Login Button
        loginBtn = new Button("Sign In");
        loginBtn.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
        loginBtn.setPrefWidth(120);

        // Register Hyperlink
        registerLink = new Hyperlink("Don't have an account? Sign Up");
        registerLink.setStyle("-fx-text-fill: darkblue; -fx-font-size: 12px;");

        // HBox for Button and Link
        HBox actionBox = new HBox(20, registerLink, loginBtn);
        actionBox.setAlignment(Pos.CENTER);

        // Assemble Main Container
        mainContainer.getChildren().addAll(titleLbl, formGrid, actionBox, nameLbl);

        // Scene Creation
        scene = new Scene(mainContainer, 450, 350);
    }

    private void setEventHandler() {
        loginBtn.setOnAction(this);

        // Navigate to registration page
        registerLink.setOnAction(event -> {
            pageManager.displayRegisterPage();
        });
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == loginBtn) {
            // Retrieve entered username and password
            String username = usernameTF.getText().toString().trim();
            String password = passwordPF.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter both username and password.");
                return;
            }

            // Navigate to admin dashboard
            if (username.equalsIgnoreCase("admin") && password.equals("admin")) {
            	System.out.println("Login as Admin");
                pageManager.displayAdminDashboard();
                return;
            }

            // Authenticate user with UserController
            Response<User> res = UserController.authenticateUser(username, password);
            if (res.isSuccess() && res.getData() != null) {
                User loggedInUser = res.getData();
                pageManager.setLoggedInUser(loggedInUser);
                String role = loggedInUser.getRole();

                switch (role.toLowerCase()) {
                    case "admin":
//                    	System.out.println("admin");
                        pageManager.displayAdminDashboard();
                        break;
                    case "seller":
                        pageManager.displaySellerDashboard();
                        break;
                    case "buyer":
                        pageManager.displayBuyerDashboard();
                        break;
                    default:
                        showAlert(Alert.AlertType.ERROR, "Login Failed", "Unknown user role: " + role);
                        break;
                }
            } else {
                // Error message if login fails
                showAlert(Alert.AlertType.ERROR, "Login Failed", res.getMessage() != null ? res.getMessage() : "Invalid username or password.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(mainContainer.getScene().getWindow()); // Set the owner to current window
        alert.showAndWait();
    }
}
