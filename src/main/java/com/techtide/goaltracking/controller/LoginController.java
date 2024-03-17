package com.techtide.goaltracking.controller;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.enums.FxmlView;
import com.techtide.goaltracking.service.SignUpService;
import com.techtide.goaltracking.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.util.ResourceBundle;
@Controller
public class LoginController implements Initializable {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    private final StageManager stageManager;
    private final SignUpService signUpService;

    public LoginController(@Lazy StageManager stageManager, SignUpService signUpService) {
        this.stageManager = stageManager;
        this.signUpService = signUpService;
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    public void onSignUpButtonPressed() {
        stageManager.switchScene(FxmlView.SIGNUP);
    }

    @FXML
    public void onLoginButtonPressed() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (signUpService.validateLogin(username, password)) {
            FXUtils.showMessage(AlertType.INFORMATION, "Login Successful");
            stageManager.switchScene(FxmlView.MENU);
        } else {
            FXUtils.showMessage(AlertType.ERROR, "Invalid username or password");
            usernameTextField.requestFocus();
        }
    }
    @FXML
    public void onForgotButtonPressed() {
        stageManager.dialogScene(FxmlView.NEW_PASSWORD);
    }

}

