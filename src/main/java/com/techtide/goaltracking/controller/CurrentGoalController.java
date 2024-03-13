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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.techtide.goaltracking.util.FXUtils.showMessage;

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
    public Button updateButton;
    @FXML
    public ChoiceBox savedCurrentGoals;
    @FXML
    public Button deleteButoon;
    @FXML
    private TextArea taskTextArea;

    @FXML
    private ChoiceBox<String> goalChoiceBox;
    @FXML
    private DatePicker datePicker;
    private boolean isErrorMessageShown = false;
    public CurrentGoalController(NewGoalService newGoalService, CurrentGoalService currentGoalService, @Lazy StageManager stageManager) {
        this.newGoalService = newGoalService;
        this.currentGoalService = currentGoalService;
        this.stageManager = stageManager;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateDropdown();
        goalChoiceBox.setItems(FXCollections.observableList(getUncompletedGoals()));
        goalChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                savedCurrentGoals.getSelectionModel().clearSelection();
                onGoalSelected(newVal);
                datePicker.setValue(null);
            }
        });
        savedCurrentGoals.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                goalChoiceBox.getSelectionModel().clearSelection();
                onUpdateGoalSelected(String.valueOf(newVal));
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
            if (newValue != null && goalChoiceBox.getSelectionModel().isEmpty() && savedCurrentGoals.getSelectionModel().isEmpty() && !isErrorMessageShown) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Please select a goal before setting a date.");
                isErrorMessageShown = true;
                datePicker.setValue(null);
            } else{
                isErrorMessageShown = false;
            }
            if (newValue != null && savedCurrentGoals.getValue() != null) {
                updateUIWithDateAndGoal(savedCurrentGoals.getValue().toString(), newValue);
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
        taskTextArea.setText(" ");
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
            String taskText = taskTextArea.getText().trim();
            if ( taskText== null) {
               taskText = "";
            }
            if (!savedCurrentGoals.getSelectionModel().isEmpty()) {
                // If a goal is selected from savedCurrentGoals, show an error message
                showMessage(Alert.AlertType.WARNING, " Please use the appropriate action (Update or Delete)");
                return;
            }

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
                currentGoalEntity.setCurrentTask(taskText);
                currentGoalService.save(currentGoalEntity);
                FXUtils.showMessage(Alert.AlertType.INFORMATION, "Goal details saved successfully");
                resetFields();
                populateDropdown();
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
        taskTextArea.setText("");
        savedCurrentGoals.getSelectionModel().clearSelection();

    }
    private void populateDropdown() {

        List<String> uniqueGoals = new ArrayList<>();
        final List<CurrentGoalEntity> goals = currentGoalService.findAllCurrentGoals();
        Set<String> gouls = new HashSet<>();
        goals.stream().map(goul ->
                gouls.add(goul.getCurrentGoal())
        ).collect(Collectors.toList());
        uniqueGoals = gouls.stream().toList();
        savedCurrentGoals.setItems(FXCollections.observableList(uniqueGoals));
    }
    private void onUpdateGoalSelected(String savedCurrentGoals) {
        List<CurrentGoalEntity> goalDates = currentGoalService.getDatesForGoal(savedCurrentGoals);
        if (!goalDates.isEmpty()) {
            NewGoalEntity selectedGoal = newGoalService.getGoalByGoalName(savedCurrentGoals);
            if (selectedGoal != null) {
                LocalDate startDate = selectedGoal.getStartDate();
                LocalDate endDate = selectedGoal.getEndDate();
                configureDatePickerToUpdate(goalDates, startDate, endDate);
            }
        } else {
            showMessage(Alert.AlertType.INFORMATION,"No dates found for the selected goal");
        }
    }
    private void configureDatePickerToUpdate(List<CurrentGoalEntity> goalDates, LocalDate startDate, LocalDate endDate) {
        if (datePicker != null) {
            datePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(date.format(DateTimeFormatter.ofPattern("d")));
                        if (containsDate(goalDates, date)) {
                            setStyle("-fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                    setDisable(empty || date.isBefore(startDate) || date.isAfter(endDate));
                }
            });
        } else {
            System.out.println("DatePicker is null. Make sure it's initialized.");
        }
    }

    private boolean containsDate(List<CurrentGoalEntity> goalDates, LocalDate date) {
        for (CurrentGoalEntity goalDate : goalDates) {
            if (goalDate.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }
    private void updateUIWithDateAndGoal(String selectedGoal, LocalDate selectedDate) {
        try {
            Optional<CurrentGoalEntity> currentGoalOptional = currentGoalService.getCurrentGoalForDate(selectedGoal, selectedDate);
            if (currentGoalOptional.isPresent()) {
                CurrentGoalEntity currentGoalEntity = currentGoalOptional.get();
                Duration duration = currentGoalEntity.getTimeSpent();

                int hours = (int) (duration.toMinutes() / 60);
                int minutes = (int) (duration.toMinutes() % 60);

                hoursSpinner.getValueFactory().setValue(hours);
                minutesSpinner.getValueFactory().setValue(minutes);

                String taskData = currentGoalEntity.getCurrentTask();
                if (taskData != null) {
                    taskTextArea.setText(taskData);
                }
            }
        } catch (Exception e) {
            showMessage(Alert.AlertType.ERROR, e.getMessage());
        }
    }
    @FXML
    private void onUpdateButtonPressed() {
        if (!goalChoiceBox.getSelectionModel().isEmpty()) {
            showMessage(Alert.AlertType.WARNING, " Please use the Save Button to save goal details.");
            return;
        }
        String selectedGoal2;
        if (savedCurrentGoals.getValue() != null) {
            selectedGoal2 = savedCurrentGoals.getValue().toString();
        } else {
            selectedGoal2 = null;
        }
        if (selectedGoal2 == null || selectedGoal2.isEmpty()) {
            showMessage(Alert.AlertType.WARNING, "Please select a goal and bold date to update.");
            return;
        }
        String selectedGoal = savedCurrentGoals.getValue().toString();
        LocalDate selectedDate1 = datePicker.getValue();
        if (selectedDate1 == null) {
            showMessage(Alert.AlertType.INFORMATION, "Please select a bold date.");
            return;
        }
        if (selectedGoal == null || selectedGoal.isEmpty()) {
            showMessage(Alert.AlertType.WARNING, "Please select a goal.");
            return;
        }
        try {
            NewGoalEntity newGoalEntity = newGoalService.getGoalByGoalName(selectedGoal);
            LocalDate selectedDate = datePicker.getValue();
            int hours = hoursSpinner.getValue();
            int minutes = minutesSpinner.getValue();
            String taskText = taskTextArea.getText().trim();
            Optional<CurrentGoalEntity> existingGoalEntityOptional = currentGoalService.getCurrentGoalForDate(selectedGoal, selectedDate);
            if (existingGoalEntityOptional.isPresent()) {
                CurrentGoalEntity existingGoalEntity = existingGoalEntityOptional.get();
                existingGoalEntity.setNewGoal(newGoalEntity);
                existingGoalEntity.setCurrentGoal(selectedGoal);
                existingGoalEntity.setDate(selectedDate);
                existingGoalEntity.setTimeSpent(Duration.ofHours(hours).plusMinutes(minutes));
                existingGoalEntity.setCurrentTask(taskText);
                currentGoalService.save(existingGoalEntity);
                showMessage(Alert.AlertType.INFORMATION, "Goal details updated successfully.");
            } else {
                showMessage(Alert.AlertType.INFORMATION, "Please select a bold date to update goal details");
            }
            resetFields();
        } catch (Exception e) {
            showMessage(Alert.AlertType.ERROR, "Error occurred while updating goal details: " + e.getMessage());
        }
    }
    @FXML
    private void onDeleteButtonPressed() {
        if (!goalChoiceBox.getSelectionModel().isEmpty()) {
            showMessage(Alert.AlertType.INFORMATION, " Please use the Save Button to save goal details.");
            return;
        }
        if (savedCurrentGoals == null) {
            showMessage(Alert.AlertType.ERROR, "Error occurred while retrieving selected goal.");
            return;
        }
        String selectedGoal;
        if (savedCurrentGoals.getValue() != null) {
            selectedGoal = savedCurrentGoals.getValue().toString();
        } else {
            selectedGoal = null;
        }
        if (selectedGoal == null || selectedGoal.isEmpty()) {
            showMessage(Alert.AlertType.INFORMATION, "Please select a goal and bold date to delete.");
            return;
        }
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            showMessage(Alert.AlertType.INFORMATION, "Please select a bold date.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete the goal details for the selected date?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Optional<CurrentGoalEntity> existingGoalEntityOptional = currentGoalService.getCurrentGoalForDate(selectedGoal, selectedDate);
                if (existingGoalEntityOptional.isPresent()) {
                    currentGoalService.delete(existingGoalEntityOptional.get());
                    refreshDatePicker();
                    showMessage(Alert.AlertType.INFORMATION, "Goal details deleted successfully.");
                } else {
                    showMessage(Alert.AlertType.INFORMATION, "Please select a bold date to delete goal details.");
                }
                resetFields();
                populateDropdown();
            } catch (Exception e) {
                showMessage(Alert.AlertType.ERROR, "Error occurred while deleting goal details: " + e.getMessage());
            }
        }
    }

    private void refreshDatePicker () {
        String selectedGoal = savedCurrentGoals.getValue().toString();
        List<CurrentGoalEntity> goalDates = currentGoalService.getDatesForGoal(selectedGoal);
        NewGoalEntity selectedGoalEntity = newGoalService.getGoalByGoalName(selectedGoal);
        LocalDate startDate = selectedGoalEntity.getStartDate();
        LocalDate endDate = selectedGoalEntity.getEndDate();
        configureDatePickerToUpdate(goalDates, startDate, endDate);
    }
    @FXML
    public void onBackIconPressed()  {
        stageManager.switchScene(FxmlView.MENU);
    }
}
