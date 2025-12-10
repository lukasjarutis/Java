package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.foodapp.User;
import org.foodapp.UserRole;

import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private Stage dialogStage;
    private User loggedInUser;

    private final UserDAO userDAO = new UserDAO();

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank()) {
            errorLabel.setText("Įveskite vartotojo vardą.");
            return;
        }

        if (password == null || password.isBlank()) {
            errorLabel.setText("Įveskite slaptažodį.");
            return;
        }

        try {
            User user = userDAO.authenticate(username, password);
            if (user == null) {
                errorLabel.setText("Neteisingas vartotojo vardas arba slaptažodis.");
                return;
            }

            if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.OWNER) {
                errorLabel.setText("Ši programa skirta tik administratoriams ir restoranams.");
                return;
            }

            this.loggedInUser = user;
            dialogStage.close();

        } catch (SQLException e) {
            errorLabel.setText("DB klaida: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        this.loggedInUser = null;
        dialogStage.close();
    }
}
