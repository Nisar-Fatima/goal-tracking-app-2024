package com.techtide.goaltracking.controller;
import com.techtide.goaltracking.config.StageManager;
import com.techtide.goaltracking.entity.CurrentGoalEntity;
import com.techtide.goaltracking.entity.NewGoalEntity;
import com.techtide.goaltracking.enums.FxmlView;
import com.techtide.goaltracking.repository.CurrentGoalRepo;
import com.techtide.goaltracking.service.NewGoalService;
import com.techtide.goaltracking.service.RecordService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
@Controller
public class RecordController implements Initializable {
    private final StageManager stageManager;
    private final RecordService recordService;
    private final NewGoalService newGoalService;
    private final CurrentGoalRepo currentGoalRepo;
    @FXML
    private ChoiceBox<String> goalChoiceBox;
    @FXML
    private TableView<CurrentGoalEntity> combinedGoalTable;
    @FXML
    private TableColumn<CurrentGoalEntity, String> nameColumn;
    @FXML
    private TableColumn<CurrentGoalEntity, String> goalColumn;
    @FXML
    private TableColumn<CurrentGoalEntity, LocalDate> startDateColumn;
    @FXML
    private TableColumn<CurrentGoalEntity, LocalDate> endDateColumn;
    @FXML
    private TableColumn<CurrentGoalEntity, LocalDate> dateColumn;
    @FXML
    private TableColumn<CurrentGoalEntity, Duration> timeSpentColumn;
    @FXML
    private TableColumn<CurrentGoalEntity, Duration> totalTimeSpentColumn;
    @FXML
    private TableColumn<CurrentGoalEntity, Long> totalDaysColumn;
    @FXML
    private TableColumn<CurrentGoalEntity, String> taskListColumn;

    public RecordController(@Lazy StageManager stageManager, RecordService recordService, NewGoalService newGoalService, CurrentGoalRepo currentGoalRepo) {
        this.stageManager = stageManager;
        this.recordService = recordService;
        this.newGoalService = newGoalService;
        this.currentGoalRepo = currentGoalRepo;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goalChoiceBox.setItems(FXCollections.observableList(getAllGoals()));
        goalChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadData());
    }

    private List<NewGoalEntity> findAll() {
        return newGoalService.getAllGoals();
    }

    private List<String> getAllGoals() {
        List<String> goalNames = new ArrayList<>();
        final List<NewGoalEntity> goals = findAll();
        for (NewGoalEntity goal : goals) {
            goalNames.add(goal.getGoal());
        }
        return goalNames;
    }

    private void loadData() {
        String selectedGoal = goalChoiceBox.getValue();
        if (selectedGoal != null ) {
            List<CurrentGoalEntity> data = recordService.findAllDataForGoal(selectedGoal);
            setUpItemTable(data);
        }
    }
    private void setUpItemTable(List<CurrentGoalEntity> data) {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNewGoal().getName()));
        goalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurrentGoal()));
        startDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNewGoal().getStartDate()));
        endDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNewGoal().getEndDate()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        timeSpentColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTimeSpent()));
        timeSpentColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Duration duration, boolean empty) {
                super.updateItem(duration, empty);
                if (empty || duration == null) {
                    setText("");
                } else {
                    String formattedDuration = String.format("%02d:%02d",
                            duration.toHours(), duration.toMinutesPart());
                    setText(formattedDuration);
                }
            }
        });
        totalTimeSpentColumn.setCellValueFactory(cellData -> {
            List<Duration> durations = currentGoalRepo.findTimeSpentForGoal(cellData.getValue().getCurrentGoal());
            Duration totalDuration = durations.stream().reduce(Duration::plus).orElse(Duration.ZERO);
            if (cellData.getValue().equals(data.get(0))) {
                return new SimpleObjectProperty<>(totalDuration);
            } else {
                return new SimpleObjectProperty<>(Duration.ZERO);
            }
        });
        totalTimeSpentColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Duration duration, boolean empty) {
                super.updateItem(duration, empty);
                if (empty || duration == null || duration.isZero()) {
                    setText("");
                } else {
                    String formattedDuration = String.format("%02d:%02d",
                            duration.toHours(), duration.toMinutesPart());
                    setText(formattedDuration);
                }
            }
        });
        totalDaysColumn.setCellValueFactory(cellData -> {
            String selectedGoal = goalChoiceBox.getValue();
            if (selectedGoal != null && cellData.getValue().equals(data.get(0))) {
                Long totalDays = currentGoalRepo.findTotalDaysForGoal(selectedGoal);
                return new SimpleObjectProperty<>(totalDays);
            } else {
                return new SimpleObjectProperty<>();
            }
        });
        combinedGoalTable.setItems(FXCollections.observableList(data));
    }
    @FXML
    public void onBackIconPressed() {
        stageManager.switchScene(FxmlView.MENU);
    }
}


