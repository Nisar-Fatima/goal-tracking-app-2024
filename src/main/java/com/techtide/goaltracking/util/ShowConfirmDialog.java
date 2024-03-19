package com.techtide.goaltracking.util;
import com.techtide.goaltracking.controller.ConfirmController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
public class ShowConfirmDialog {
  private boolean message;

  public boolean show(String message) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/confirm.fxml"));
      Parent root = loader.load();
      ConfirmController controller = loader.getController();
      controller.setMessage(message);

      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.initStyle(StageStyle.DECORATED);
      dialogStage.setResizable(false);
      dialogStage.setTitle("Confirmation!");
      dialogStage.setScene(new Scene(root));
      dialogStage.setAlwaysOnTop(true);

      Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
      double preferredStageWidth = 320.0;
      double preferredStageHeight = 138.0;
      double xCoordinate = screenBounds.getMinX() + (screenBounds.getWidth() - preferredStageWidth) / 2;
      double yCoordinate = screenBounds.getMinY() + (screenBounds.getHeight() - preferredStageHeight) / 4;
      xCoordinate -= 70;
      yCoordinate -= 60;
      dialogStage.setX(xCoordinate);
      dialogStage.setY(yCoordinate);

      PauseTransition delay = new PauseTransition();
      delay.setDuration(Duration.seconds(4));
      delay.setOnFinished(event -> {
        dialogStage.close();
        dialogStage.hide();
      });
      delay.play();
      dialogStage.showAndWait();
      this.message = controller.getAnswer();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return this.message;
  }
}
