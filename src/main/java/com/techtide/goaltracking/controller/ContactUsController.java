package com.techtide.goaltracking.controller;
import javafx.fxml.Initializable;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.enums.FxmlView;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ContactUsController implements  Initializable {
    private final StageManager stageManager;


    public ContactUsController(@Lazy StageManager stageManager) {
        this.stageManager = stageManager;
    }
    @Override
    public void initialize (URL url , ResourceBundle resourceBundle){
    }
    @FXML
    public void onBackIconClicked() throws Exception{
        stageManager.switchScene(FxmlView.MENU);
    }
}
