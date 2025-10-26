import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Hyperlink forgotLink;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginLabel;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button signupButton;

    @FXML
    private TextField usernameField;

    @FXML
    private Label usernameLabel;

    // When user clicks Login
    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // You can add proper authentication here (e.g., check against DB or file)
        if (username.equals("admin") && password.equals("1234")) {
            // Load the dashboard
            Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml")); // Replace with your actual dashboard FXML file name
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
        } else {
            loginLabel.setText("Invalid username or password!");
        }
    }

    @FXML
    void handlePassword(ActionEvent event) {}

    @FXML
    void handleSignup(ActionEvent event) {}

    @FXML
    void handleUsername(ActionEvent event) {}

    @FXML
    void openReset(ActionEvent event) {}
}
