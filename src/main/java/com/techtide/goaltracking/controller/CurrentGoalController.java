package com.techtide.goaltracking.controller;

import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.entity.NewGoalEntity;
import com.techtide.goaltracking.enums.FxmlView;
import com.techtide.goaltracking.service.NewGoalService;
import com.techtide.goaltracking.util.FXUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
@Controller
public class CurrentGoalController implements Initializable {
    private final NewGoalService newGoalService;
    private final StageManager stageManager;
    @FXML
    private Spinner<Integer> hoursSpinner;

    @FXML
    private Spinner<Integer> minutesSpinner;

    @FXML
    private ChoiceBox<String> goalChoiceBox;
    @FXML
    private DatePicker datePicker;
    private boolean isErrorMessageShown = false;

    public CurrentGoalController(NewGoalService newGoalService, @Lazy StageManager stageManager) {
        this.newGoalService = newGoalService;
        this.stageManager = stageManager;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goalChoiceBox.setItems(FXCollections.observableList(getUncompletedGoals()));
        goalChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                onGoalSelected(newVal);
            }
        });
        hoursSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Integer enteredValue = hoursSpinner.getValue();
                if (enteredValue < 0 || enteredValue > 23) {
                    FXUtils.showMessage(Alert.AlertType.ERROR, "Invalid value for hours. Please enter a value between 0 and 23.");
                    hoursSpinner.getValueFactory().setValue(Math.min(Math.max(enteredValue, 0), 23));
                }
            }
        });
        minutesSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Integer enteredValue = minutesSpinner.getValue();
                if (enteredValue < 0 || enteredValue > 59) {
                    FXUtils.showMessage(Alert.AlertType.ERROR, "Invalid value for minutes. Please enter a value between 0 and 59..");
                    minutesSpinner.getValueFactory().setValue(Math.min(Math.max(enteredValue, 0), 59));
                }
            }
        });
        datePicker.getEditor().setDisable(true);
        datePicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (goalChoiceBox.getSelectionModel().getSelectedItem() == null && !isErrorMessageShown) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Please select a goal before setting a date.");
                isErrorMessageShown = true;
                datePicker.setValue(null);
            } else{
                isErrorMessageShown = false;
            }
        });
    }
    private void onGoalSelected(String goalName) {
        NewGoalEntity selectedGoal = newGoalService.getGoalByGoalName(goalName);
        if (selectedGoal != null) {
            LocalDate startDate = selectedGoal.getStartDate();
            LocalDate endDate = selectedGoal.getEndDate();
            configureDatePicker(startDate, endDate);
        }
    }
    private void configureDatePicker(LocalDate startDate, LocalDate endDate) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(startDate) || date.isAfter(endDate));
            }
        });
        datePicker.getEditor().setDisable(true);
    }
    private List<String> getUncompletedGoals() {
        List<String> goalNames = new ArrayList<>();
        final List<NewGoalEntity> goals = findAll();
        LocalDate today = LocalDate.now();
        for (NewGoalEntity goal : goals) {
            if (goal.getEndDate() == null || goal.getEndDate().isEqual(today) || goal.getEndDate().isAfter(today)) {
                goalNames.add(goal.getGoal());
            }
        }
        return goalNames;
    }

    private List<NewGoalEntity> findAll() {
        return newGoalService.getAllGoals();
    }
    @FXML
    public void onTaskListpressed()  {
        stageManager.switchScene(FxmlView.TASK);
    }
    @FXML
    public void onBackIconPressed()  {
        stageManager.switchScene(FxmlView.MENU);
    }

}
