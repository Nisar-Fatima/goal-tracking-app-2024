package com.techtide.goaltracking.controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class StopwatchController implements Initializable {

    @FXML
    private Label timeLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button lapButton;

    @FXML
    private TextArea lapsTextArea;

    @FXML
    private ScrollPane scrollPane;

    private Stopwatch stopwatch;


    @FXML
    void handleStart(ActionEvent event) {
        initializeStopwatchIfNeeded();
        stopwatch.handleStart();
    }

    @FXML
    void handleReset(ActionEvent event) {
        initializeStopwatchIfNeeded();
        stopwatch.handleReset();
    }

    @FXML
    void handleLap(ActionEvent event) {
        initializeStopwatchIfNeeded();
        stopwatch.handleLap();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stopwatch = new Stopwatch(timeLabel, startButton, resetButton, lapButton, lapsTextArea, scrollPane);
        initializeStopwatchIfNeeded();
    }

    private void initializeStopwatchIfNeeded() {
        if (stopwatch == null) {
            stopwatch = new Stopwatch(timeLabel, startButton, resetButton, lapButton, lapsTextArea, scrollPane);
        }
    }

    public class Stopwatch {
        private Label timeLabel;
        private Button startButton;
        private Button resetButton;
        private Button lapButton;
        private TextArea lapsTextArea;
        private ScrollPane scrollPane;

        private int elapsedTime = 0;
        private int lapTime = 0;
        private int seconds = 0;
        private int millis = 0;
        private int minutes = 0;
        private int hours = 0;
        private int lapCounter = 0;
        private boolean started = false;
        private boolean lapStarted = false;

        private String secondsString, minutesString, hoursString, millisString;

        private Timeline timeline;

        public Stopwatch(Label timeLabel, Button startButton, Button resetButton,
                         Button lapButton, TextArea lapsTextArea, ScrollPane scrollPane) {
            this.timeLabel = timeLabel;
            this.startButton = startButton;
            this.resetButton = resetButton;
            this.lapButton = lapButton;
            this.lapsTextArea = lapsTextArea;
            this.scrollPane = scrollPane;

            configureComponents();
        }

        private void configureComponents() {
            // ... (Same configuration as your original Java code)

            // Set up the timeline for updating the time label
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> actionPerformed()));
            timeline.setCycleCount(Animation.INDEFINITE);
        }

        private void actionPerformed() {
            elapsedTime += 10;
            lapTime += 10;

            millis = (elapsedTime % 1000) / 10;
            seconds = (elapsedTime / 1000) % 60;
            minutes = (elapsedTime / 60000) % 60;
            hours = (elapsedTime / 3600000);

            millisString = String.format("%02d", millis);
            secondsString = String.format("%02d", seconds);
            minutesString = String.format("%02d", minutes);
            hoursString = String.format("%02d", hours);

            timeLabel.setText(hoursString + ":" + minutesString + ":"
                    + secondsString + "." + millisString);
        }

        public void handleStart() {
            if (!started) {
                started = true;
                lapStarted = true;
                startButton.setText("Stop");
                lapButton.setDisable(false); // Enable lap button when the stopwatch is started
                start();
            } else {
                started = false;
                lapStarted = false;
                startButton.setText("Start");
                lapButton.setDisable(true); // Disable lap button when the stopwatch is stopped
                stop();
            }
        }

        public void handleReset() {
            started = false;
            lapStarted = false;
            startButton.setText("Start");
            lapButton.setDisable(true); // Disable lap button when the stopwatch is reset
            reset();
        }

        public void handleLap() {
            if (lapStarted) {
                recordLap();
            }
        }

        private void start() {
            timeline.play();
        }

        private void stop() {
            timeline.pause();
        }

        private void reset() {
            timeline.stop();
            elapsedTime = 0;
            lapTime = 0;
            seconds = 0;
            minutes = 0;
            hours = 0;
            lapCounter = 0;
            timeLabel.setText("00:00:0:00");
            lapsTextArea.clear();
        }

        private void recordLap() {
            lapCounter++;
            lapsTextArea.appendText("Lap " + lapCounter + ":                                   " + lapTime / 1000 + "."
                    + String.format("%02d", lapTime % 1000 / 10) + "s\n");
            lapTime = 0;
        }


    }
}
