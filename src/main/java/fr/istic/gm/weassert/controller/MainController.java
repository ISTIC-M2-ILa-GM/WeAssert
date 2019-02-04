package fr.istic.gm.weassert.controller;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javafx.scene.chart.PieChart.*;

public class MainController {
    private File file;

    @FXML
    public PieChart testResultsChart;

    @FXML
    public Label selectedFile;

    @FXML
    public Button browseButton;

    private Data passedTest;

    private Data failedTests;


    public void initialize() {
        this.testResultsChart.setStartAngle(45);

        this.passedTest = new Data("Passed tests", 1);
        this.failedTests = new Data("Failed tests", 1);

        ObservableList<Data> observableList = new ObservableListWrapper<>(Arrays.asList(failedTests, passedTest));
        this.testResultsChart.setData(observableList);
    }

    public void browseAction(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        this.file = directoryChooser.showDialog(null);
        if (this.file == null) {
            this.selectedFile.setText("[No project selected]");
        } else {
            this.selectedFile.setText(this.file.getPath());
        }
    }

    public void generateAction(ActionEvent actionEvent) {
        // TODO: start assertions generation
    }

    public void testAction(ActionEvent actionEvent) {
        // TODO: start tests
        this.passedTest.setPieValue(8);
    }
}
