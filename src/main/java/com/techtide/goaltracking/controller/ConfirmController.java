package com.techtide.goaltracking.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

@Controller
public class ConfirmController {
    @FXML
    private Label messageLabel;
    private static boolean answer = false;

    public void onClick() {
        answer = true;
        closeDialog();
    }

    private void closeDialog() {
        messageLabel.getScene().getWindow().hide();
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
