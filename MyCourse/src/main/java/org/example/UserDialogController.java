package org.example;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.foodapp.AppUserRow;

public class UserDialogController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private PasswordField passwordField;

    private Stage dialogStage;
    private AppUserRow user;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUser(AppUserRow user) {
        this.user = user;

        usernameField.setText(user.getUsername());
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        roleComboBox.setValue(user.getRole());
    }

    public AppUserRow getUser() {
        return user;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList(
                "ADMIN", "OWNER", "DRIVER", "CUSTOMER"
        ));
    }

    @FXML
    private void onSave() {
        String username = usernameField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String role = roleComboBox.getValue();
        String newPassword = passwordField.getText();

        if (username == null || username.isBlank()) {
            usernameField.requestFocus();
            return;
        }

        if (fullName == null || fullName.isBlank()) {
            fullNameField.requestFocus();
            return;
        }

        if (role == null || role.isBlank()) {
            roleComboBox.requestFocus();
            return;
        }

        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);

        if (newPassword != null && !newPassword.isBlank()) {
            String hash = PasswordUtil.hashPassword(newPassword);
            user.setPasswordHash(hash);
        }

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        okClicked = false;
        dialogStage.close();
    }
}
