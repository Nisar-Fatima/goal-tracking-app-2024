package com.techtide.goaltracking.controller;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.entity.CurrentGoalEntity;
import com.techtide.goaltracking.entity.NewGoalEntity;
import com.techtide.goaltracking.enums.FxmlView;
import com.techtide.goaltracking.service.CurrentGoalService;
import com.techtide.goaltracking.service.NewGoalService;
import com.techtide.goaltracking.util.FXUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
@Controller
public class CurrentGoalController implements Initializable {
    private final NewGoalService newGoalService;
    private final CurrentGoalService currentGoalService;
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
    private boolean dataSaved = false;

    public CurrentGoalController(NewGoalService newGoalService, CurrentGoalService currentGoalService, @Lazy StageManager stageManager) {
        this.newGoalService = newGoalService;
        this.currentGoalService = currentGoalService;
        this.stageManager = stageManager;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goalChoiceBox.setItems(FXCollections.observableList(getUncompletedGoals()));
        goalChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                onGoalSelected(newVal);
                resetData();
            }
        });
        TextFormatter<Integer> hoursFormatter = createIntegerTextFormatter(0);
        hoursSpinner.getEditor().setTextFormatter(hoursFormatter);
        hoursSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            int enteredValue = newValue;
            if (enteredValue < 0 || enteredValue > 24) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Invalid value for hours. Please enter value between 0 and 24.");
                hoursSpinner.getValueFactory().setValue(24);
            }
        });

        TextFormatter<Integer> minutesFormatter = createIntegerTextFormatter(0);
        minutesSpinner.getEditor().setTextFormatter(minutesFormatter);
        minutesSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            int enteredValue = newValue;
            if (enteredValue < 0 || enteredValue > 59) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Invalid value for minutes. Please enter value between 0 and 59.");
                minutesSpinner.getValueFactory().setValue(59);
            }
        });
        datePicker.getEditor().setDisable(true);
        datePicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (goalChoiceBox.getSelectionModel().getSelectedItem() == null && newValue != null && !isErrorMessageShown) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Please select a goal before setting a date.");
                isErrorMessageShown = true;
                datePicker.setValue(null);
            } else{
                isErrorMessageShown = false;
            }
        });
    }
    private TextFormatter<Integer> createIntegerTextFormatter(int defaultValue) {
        return new TextFormatter<>(
                new IntegerStringConverter(),
                defaultValue,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.isEmpty()) {
                        change.setText(Integer.toString(defaultValue));
                        return change;
                    }
                    if (newText.matches("-?\\d*")) {
                        return change;
                    }
                    return null;
                }
        );
    }
    private void resetData() {
        datePicker.setValue(null);
        hoursSpinner.getValueFactory().setValue(0);
        minutesSpinner.getValueFactory().setValue(0);
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
    public void onSaveButtonPressed() {
        try {
            String selectedGoal = goalChoiceBox.getValue();
            int hours = hoursSpinner.getValue();
            int minutes = minutesSpinner.getValue();
            LocalDate date = datePicker.getValue();

            if (selectedGoal == null || selectedGoal.isEmpty() || date == null) {
                FXUtils.showMessage(Alert.AlertType.WARNING, "Please select a goal  and enter a valid date.");
                return;
            }
            if (currentGoalService.existsEntry(selectedGoal, date)) {
                FXUtils.showMessage(Alert.AlertType.WARNING, "An entry already exists.Please choose different date ");
                return;
            }
            if (hours == 24 && minutes > 0) {
                FXUtils.showMessage(Alert.AlertType.WARNING, "The total hours in a day cannot exceed 24.");
                return;
            }
            final NewGoalEntity newGoalEntity = newGoalService.getGoalByGoalName(selectedGoal);
            if (newGoalEntity != null) {
                CurrentGoalEntity currentGoalEntity = new CurrentGoalEntity();
                currentGoalEntity.setNewGoal(newGoalEntity);
                currentGoalEntity.setCurrentGoal(selectedGoal);
                currentGoalEntity.setDate(date);
                currentGoalEntity.setTimeSpent(Duration.ofHours(hours).plusMinutes(minutes));
                currentGoalService.save(currentGoalEntity);
                FXUtils.showMessage(Alert.AlertType.INFORMATION, "Goal details saved successfully");
                dataSaved = true;
                resetFields();
            }
        }catch(Exception e){
            FXUtils.showMessage(Alert.AlertType.ERROR, e.getMessage());
        }
    }
    @FXML
    private void resetFields() {
        goalChoiceBox.getSelectionModel().clearSelection();
        hoursSpinner.getValueFactory().setValue(0);
        minutesSpinner.getValueFactory().setValue(0);
        datePicker.setValue(null);
    }

    @FXML
    public void onTaskListpressed()  {
        if (!dataSaved) {
            FXUtils.showMessage(Alert.AlertType.ERROR, "Please save your current goal before switching to the Task page.");
        } else {
            stageManager.switchScene(FxmlView.TASK);
        }
    }
    @FXML
    public void onBackIconPressed()  {
        stageManager.switchScene(FxmlView.MENU);
    }
}
