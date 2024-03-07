package com.techtide.goaltracking.controller;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.entity.NewGoalEntity;
import com.techtide.goaltracking.service.NewGoalService;
import com.techtide.goaltracking.util.FXUtils;
import com.techtide.goaltracking.enums.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class NewGoalController implements Initializable {
    private final StageManager stageManager;
    private final NewGoalService newGoalService;

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField goalTextField;
    @FXML
    private DatePicker startDateTextField;
    @FXML
    private DatePicker endDateTextField;
    @FXML
    private ChoiceBox<String> deleteChoiceBox;



    public NewGoalController(@Lazy StageManager stageManager, NewGoalService newGoalService) {
        this.stageManager = stageManager;
        this.newGoalService = newGoalService;
    }


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startDateTextField.getEditor().setDisable(true);
        endDateTextField.getEditor().setDisable(true);

        loadGoals();
        startDateTextField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        endDateTextField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        deleteChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                NewGoalEntity selectedGoal = newGoalService.getGoalByGoalName(newValue);

                nameTextField.setText(selectedGoal.getName());
                goalTextField.setText(selectedGoal.getGoal());
                startDateTextField.setValue(selectedGoal.getStartDate());
                endDateTextField.setValue(selectedGoal.getEndDate());
            }
        });
    }


    private void loadGoals() {
        List<NewGoalEntity> goals = newGoalService.getAllGoals();

        deleteChoiceBox.getItems().clear();

        for (NewGoalEntity goal : goals) {
            deleteChoiceBox.getItems().add(goal.getGoal());
        }
    }

    @FXML
    public void onUpdateConfirmButtonPressed() {
        String selectedGoal = deleteChoiceBox.getValue();
        if (selectedGoal == null || selectedGoal.isEmpty()) {
            FXUtils.showMessage(Alert.AlertType.WARNING, "Please select a goal to update.");
            return;
        }

        try {
            NewGoalEntity goalEntity = newGoalService.getGoalByGoalName(selectedGoal);

            String currentName = goalEntity.getName();
            String currentGoal = goalEntity.getGoal();
            LocalDate currentStartDate = goalEntity.getStartDate();
            LocalDate currentEndDate = goalEntity.getEndDate();

            String newName = nameTextField.getText().trim();
            String newGoal = goalTextField.getText().trim();
            LocalDate newStartDate = startDateTextField.getValue();
            LocalDate newEndDate = endDateTextField.getValue();

            boolean valuesChanged = !newName.equals(currentName) || !newGoal.equals(currentGoal) ||
                    !Objects.equals(newStartDate, currentStartDate) ||
                    !Objects.equals(newEndDate, currentEndDate);

            if (!valuesChanged) {
                FXUtils.showMessage(Alert.AlertType.WARNING, "No changes were made.");
                return;
            }

            if (newName.isBlank() || newGoal.isBlank() || newStartDate == null || newEndDate == null) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "All fields must be entered");
                return;
            }

            if (!selectedGoal.equals(newGoal)) {
                NewGoalEntity existingGoal = newGoalService.getGoalByGoalName(newGoal);
                if (existingGoal != null) {
                    FXUtils.showMessage(Alert.AlertType.ERROR, "Goal name already exists. Please choose a different name.");
                    return;
                }
            }

            if (newEndDate.isBefore(newStartDate)) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "End date must be after or equal to start date");
                return;
            }
            if (isNameAlreadyExists(newGoal)) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Goal name already exists. Please choose a different name.");
                return;
            }

            goalEntity.setName(newName);
            goalEntity.setGoal(newGoal);
            goalEntity.setStartDate(newStartDate);
            goalEntity.setEndDate(newEndDate);

            newGoalService.updateGoal(goalEntity);
            nameTextField.clear();
            goalTextField.clear();
            startDateTextField.setValue(null);
            endDateTextField.setValue(null);

            loadGoals();

            FXUtils.showMessage(Alert.AlertType.INFORMATION, "Goal details updated successfully!");
        } catch (Exception e) {
            FXUtils.showMessage(Alert.AlertType.ERROR, "Failed to update goal details: " + e.getMessage());
        }
    }


    @FXML
    public void onDeleteButtonPressed() {
        String selectedGoal = deleteChoiceBox.getValue();
        if (selectedGoal == null || selectedGoal.isEmpty()) {
            FXUtils.showMessage(Alert.AlertType.WARNING, "Please select a goal to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected goal?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                newGoalService.delete(selectedGoal);

                loadGoals();

                FXUtils.showMessage(Alert.AlertType.INFORMATION, "Goal deleted successfully!");
            } catch (Exception e) {
                FXUtils.showMessage(Alert.AlertType.ERROR, "Failed to delete goal: " + e.getMessage());
            }
        }
    }


    @FXML
    public void onSaveButtonPressed() {
        String name = nameTextField.getText();
        String goal = goalTextField.getText();
        LocalDate startDate = startDateTextField.getValue();
        LocalDate endDate = endDateTextField.getValue();

        if (name.isBlank() || goal.isBlank() || startDate == null || endDate == null) {
            FXUtils.showMessage(Alert.AlertType.ERROR, "All fields must be entered");
            return;
        }

        if (endDate.isBefore(startDate)) {
            FXUtils.showMessage(Alert.AlertType.ERROR, "End date must be after or equal to start date");
            return;
        }
        if (isNameAlreadyExists(goal)) {
            FXUtils.showMessage(Alert.AlertType.ERROR, "Goal name already exists. Please choose a different name.");
            return;
        }

        try {
            NewGoalEntity entity = new NewGoalEntity();
            entity.setName(name);
            entity.setGoal(goal);
            entity.setStartDate(startDate);
            entity.setEndDate(endDate);

            newGoalService.save(entity);

            nameTextField.clear();
            goalTextField.clear();
            startDateTextField.setValue(null);
            endDateTextField.setValue(null);

            loadGoals();

            FXUtils.showMessage(Alert.AlertType.INFORMATION, "Goal saved successfully!");
        } catch (Exception e) {
            String error = e.getMessage();
            if (error.contains("newgoal_uk1")) {
                error = "Goal Name already exists";
            }
            FXUtils.showMessage(Alert.AlertType.ERROR, error);
        }
    }

    private boolean isNameAlreadyExists(String name) {
        String newNameLowerCase = name.toLowerCase();
        List<NewGoalEntity> goals = newGoalService.getAllGoals();
        for (NewGoalEntity existingGoal : goals) {
            if (existingGoal.getGoal().toLowerCase().equals(newNameLowerCase)) {
                return true;
            }
        }
        return false;
    }
    @FXML
    public void onBackIconClicked() {
        stageManager.switchScene(FxmlView.MENU);
    }
}


















