package com.techtide.goaltracking.controller;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.entity.SignUpEntity;
import com.techtide.goaltracking.enums.FxmlView;
import com.techtide.goaltracking.service.SignUpService;
import com.techtide.goaltracking.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.util.ResourceBundle;
@Controller
public class SignUpController implements Initializable {
    private final SignUpService signUpService;
    private final StageManager stageManager;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label passwordFeedbackLabel;
    @FXML
    private Label passwordFeedbackLabel2;
    @FXML
    private Label usernameFeedbacklabel;

    public SignUpController(SignUpService signUpService, @Lazy StageManager stageManager) {
        this.signUpService = signUpService;
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void onUsernameTextFieldChanged() {
        String usernameFeedback = getusernameFeedback(usernameTextField.getText());
        usernameFeedbacklabel.setText(usernameFeedback);
    }

    private String getusernameFeedback(String username) {
        if (signUpService.existsByUsername(username)) {
            return "Choose a different username";
        }
        return "";
    }
    @FXML
    public void onPasswordTextFieldChanged() {
        String passwordFeedback = getPasswordFeedback(passwordTextField.getText());
        passwordFeedbackLabel.setText(passwordFeedback);
        String passwordFeedback2 = getPasswordFeedback2(passwordTextField.getText());
        passwordFeedbackLabel2.setText(passwordFeedback2);
    }

    private String getPasswordFeedback(String password) {
        if (!password.matches(".*[a-zA-Z]+.*")) {
            return "Password contain at least one letter.";
        }
        if (!password.matches(".*[0-9]+.*")) {
            return "Password contain at least one number.";
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            return "Password contain at least one special character.";
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
    public void onSignUpButtonPressed() {
        try {
            String username = usernameTextField.getText();
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Must enter all information..");
                return;
            }
            if (signUpService.existsByUsername(username)) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Username already exists");
                return;
            }
            if (!isValidEmail(email) || signUpService.existsByEmail(email)) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Invalid email or email already exist.");
                return;
            }
            final SignUpEntity entity = new SignUpEntity();
            entity.setUsername(username);
            entity.setEmail(email);
            entity.setPassword(password);
            signUpService.save(entity);
            FXUtils.showMessage(Alert.AlertType.INFORMATION, "SignUp Successfully");
            stageManager.switchScene(FxmlView.MENU);
        } catch (Exception e) {
            FXUtils.showMessage(Alert.AlertType.ERROR, e.getMessage());
        }
    }
    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@gmail\\.com");
    }
}
