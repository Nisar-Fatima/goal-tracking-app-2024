package com.techtide.goaltracking.controller;
import com.techtide.goaltracking.config.SpringFXMLLoader;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.enums.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.util.ResourceBundle;
@Controller
public class MenuController implements Initializable {
    private final StageManager stageManager;
    public MenuController(@Lazy StageManager stageManager) {
        this.stageManager = stageManager;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    public void onNewGoalMenuItemSelected() throws Exception {
        stageManager.switchScene(FxmlView.NEWGOAL);
    }
    @FXML
    public void onCurrentGoalMenuItemSelected() throws Exception {
        stageManager.switchScene(FxmlView.CURRENTGOAL);
    }
        @FXML
    public void onRecordsMenuItemSelected() throws Exception {
        stageManager.switchScene(FxmlView.RECORDS);    }
//  @FXML
//  public void onStopWatchMenuItemSelected() throws Exception {
//        stageManager.switchScene(FxmlView.STOPWATCH);
//  }
//    @FXML
//    public void onContactUsMenuItemSelected() throws Exception {      // Ye 0 code k saath exit kr de ga
//        stageManager.switchScene(FxmlView.CONTACTUS);
//    }

}


