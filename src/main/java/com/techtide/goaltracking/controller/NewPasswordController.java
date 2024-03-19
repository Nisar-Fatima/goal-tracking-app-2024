package com.techtide.goaltracking.controller;
import com.techtide.goaltracking.service.SignUpService;
import com.techtide.goaltracking.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class NewPasswordController implements Initializable {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmNewPasswordField;
    @FXML
    private Label passwordFeedbackLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label passwordFeedbackLabel2;
    @FXML
    private Label confirmPasswordFeedbackLabel;
    private final SignUpService signUpService;

    public NewPasswordController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newPasswordField.textProperty().addListener((observableValue, oldValue, newValue) -> onConfirmPassswordFieldChanged());

    }
    @FXML
    public void onPasswordTextFieldChanged() {
        String passwordFeedback = getPasswordFeedback(newPasswordField.getText());
        passwordFeedbackLabel.setText(passwordFeedback);
        String passwordFeedback2 = getPasswordFeedback2(newPasswordField.getText());
        passwordFeedbackLabel2.setText(passwordFeedback2);
    }
    private String getPasswordFeedback(String password) {
        if (!password.matches(".*[a-zA-Z]+.*")) {
            return "Password must contain at least one letter.";
        }
        if (!password.matches(".*[0-9]+.*")) {
            return "Password must contain at least one number.";
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            return "Password must contain at least one special character.";
        }
        return "";
    }

    private String getPasswordFeedback2(String password) {
        if (password.matches(".*[a-zA-Z]+.*") &&
                password.matches(".*[0-9]+.*") &&
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            return "Strong password";
        }
        return "";
    }
    @FXML
    public void onConfirmPassswordFieldChanged() {
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (!newPassword.isEmpty() && !confirmNewPassword.isEmpty()) {
            String confirmPasswordFeedback = getConfirmPasswordFeedback(confirmNewPassword);
            confirmPasswordFeedbackLabel.setText(confirmPasswordFeedback);
        } else {
            confirmPasswordFeedbackLabel.setText("");
        }
    }


    private String getConfirmPasswordFeedback(String confirmNewPassword) {
        String newPassword = newPasswordField.getText();
        if (newPassword.equals(confirmNewPassword)) {
            return "Passwords match.";
        }
        return "";
    }
    @FXML
    public void onSubmitButtonPressed() {
        String username = usernameTextField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (username.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
            FXUtils.showMessage(AlertType.ERROR, "Must enter all information..");
            return;
        }
        if (!signUpService.existsByUsername(username)) {
            FXUtils.showMessage(AlertType.ERROR, "Username does not exist.");
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            FXUtils.showMessage(AlertType.ERROR, "Passwords don't matched");
            return;
        }
        if(!(newPassword.matches(".*[a-zA-Z]+.*") &&
                newPassword.matches(".*[0-9]+.*") &&
                newPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*"))){
            FXUtils.showMessage(AlertType.INFORMATION, "Please keep your password strong.");
            return;
        }
        boolean updateSuccess = signUpService.updatePassword(username, newPassword);
        if (updateSuccess) {
            FXUtils.showMessage(AlertType.INFORMATION, "Password successfully updated.");
           Stage stage=(Stage) saveButton.getScene().getWindow();
           stage.close();
        } else {
            FXUtils.showMessage(AlertType.ERROR, "Failed to update the password.");
        }
    }
    @FXML
    public void onCancelButtonPressed() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
