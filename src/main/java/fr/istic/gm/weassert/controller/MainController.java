package fr.istic.gm.weassert.controller;

import com.sun.javafx.collections.ObservableListWrapper;
import fr.istic.gm.weassert.test.utils.FileUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.scene.chart.PieChart.Data;

public class MainController {
    @FXML
    public PieChart testResultsChart;

    @FXML
    public Label selectedMaven;

    @FXML
    public Label selectedFile;

    @FXML
    public Button browseButton;

    @FXML
    public WebView webView;

    @FXML
    public TreeView treeView;

    private File file;

    private File mavenBinary;

    private Data passedTest;

    private Data failedTests;

    private WebEngine webEngine;


    public void initialize() {
        this.webEngine = this.webView.getEngine();
        TreeItem<String> rootItem = new TreeItem<>("[No project selected]");
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);

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

            showTestFiles(this.file);
        }
    }

    public void displaySourceCode(String s) {
        File sourceFile = new File(s);
        if (sourceFile.isFile()) {
            try {
                String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));

                this.webEngine.loadContent(String.format("<html><body><pre><code>\n" +
                        "%s\n" +
                        "</code></pre></body>", sourceCode));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateAction(ActionEvent actionEvent) {
        // TODO: start assertions generation
    }

    public void testAction(ActionEvent actionEvent) {
        // TODO: start tests
        this.passedTest.setPieValue(8);
    }

    public void selectMavenAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        this.mavenBinary = fileChooser.showOpenDialog(null);
        if (this.mavenBinary == null) {
            this.selectedMaven.setText("[System default]");
        } else {
            this.selectedMaven.setText(this.file.getPath());
        }
    }

    public void showTestFiles(File directory) {
        this.treeView.getRoot().setValue(directory.getName());
        this.treeView.getRoot().getChildren().clear();

        List<TreeItem<String>> collect = FileUtils.findFilesFromFolder(directory)
                .stream().filter(f -> f.getName().endsWith("Test.java"))
                .map(f -> new TreeItem<>(f.getAbsolutePath()))
                .collect(Collectors.toList());

        this.treeView.getRoot().getChildren().addAll(collect);
    }

    public void addClassToTreeView(String className) {
        TreeItem root = this.treeView.getRoot();
        TreeItem<String> item = new TreeItem<> (className);
        root.getChildren().add(item);
    }

    public void itemClickedAction(MouseEvent mouseEvent) {
        MultipleSelectionModel selectionModel = this.treeView.getSelectionModel();
        ObservableList<TreeItem> selectedItems = selectionModel.getSelectedItems();
        this.displaySourceCode(selectedItems.get(0).getValue().toString());
    }
}
