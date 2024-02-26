package com.techtide.goaltracking.util;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class FXUtils {
    public static void showMessage(AlertType alertType, String message ){
        final Alert alert = new Alert(alertType);
        alert.setTitle(alertType.name());
        alert.setContentText(message);
        alert.showAndWait();
    }
}
