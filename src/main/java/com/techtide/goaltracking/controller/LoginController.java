package com.techtide.goaltracking.controller;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.enums.FxmlView;
import com.techtide.goaltracking.service.SignUpService;
import com.techtide.goaltracking.util.FXUtils;
import com.techtide.goaltracking.util.PreferencesUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoginController implements Initializable {
    @FXML
    private CheckBox showPasswordCheckBox;
    @FXML
    private CheckBox rememberMeCheckBox;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    private final StageManager stageManager;
    private final SignUpService signUpService;
    private boolean dialogShown = false;

    public LoginController(@Lazy StageManager stageManager, SignUpService signUpService) {
        this.stageManager = stageManager;
        this.signUpService = signUpService;
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordText.setVisible(false);
        onSetUpSuggestion();
    }
    private void showDialog()  {
        if (!dialogShown) {
            final String rememberUsername = PreferencesUtil.getUsername();
            final String rememberPassword = PreferencesUtil.getPassword();
            final boolean dialog = FXUtils.showConfirmDialog("Are you want to login with " + rememberUsername + " ?");
            dialogShown = true;
            if (dialog) {
                usernameTextField.setText(rememberUsername);
                passwordField.setText(rememberPassword);
                passwordText.setText(rememberPassword);
                showPasswordCheckBox.setDisable(true);
            }
        }
    }
    private void onSetUpSuggestion() {
        usernameTextField.setOnMouseClicked(event -> {
            if (usernameTextField.getText().isEmpty()) {
                    showDialog();
            }
        });
    }

        @FXML
    public void onSignUpButtonPressed() {
        stageManager.switchScene(FxmlView.SIGNUP);
    }

    @FXML
    public void onLoginButtonPressed() {
        try {
            String username = usernameTextField.getText();
            String password = getPasswordForLogin();
            if (signUpService.validateLogin(username, password)) {
                if (rememberMeCheckBox.isSelected()) {
                    PreferencesUtil.getUsername();
                    PreferencesUtil.clearSavedLoginDetails();
                    PreferencesUtil.saveLoginDetails(username, password);
                }
                FXUtils.showMessage(AlertType.INFORMATION, "Login Successful");
                stageManager.switchScene(FxmlView.MENU);
            } else {
                FXUtils.showMessage(AlertType.ERROR, "Invalid username or password");
                usernameTextField.requestFocus();
            }
        } catch (Exception e) {
            FXUtils.showMessage(Alert.AlertType.ERROR, e.getMessage());
        }
    }
    @FXML
    public void onForgotButtonPressed() {
        stageManager.dialogScene(FxmlView.NEW_PASSWORD);
    }

    private String getPasswordForLogin() {
        if (showPasswordCheckBox.isSelected()) {
            return passwordText.getText();
        } else {
            return passwordField.getText();
        }
    }
    @FXML
    public void changeVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordText.setText(passwordField.getText());
            passwordText.setVisible(true);
            passwordField.setVisible(false);
        } else {
            if (passwordText.isVisible()) {
                passwordField.setText(passwordText.getText());
            }
            passwordField.setVisible(true);
            passwordText.setVisible(false);
        }
    }


}

