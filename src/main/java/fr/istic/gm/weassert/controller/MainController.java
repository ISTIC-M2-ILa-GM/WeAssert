package fr.istic.gm.weassert.controller;

import com.sun.javafx.collections.ObservableListWrapper;
import fr.istic.gm.weassert.test.application.WeAssertRunner;
import fr.istic.gm.weassert.test.application.impl.WeAssertRunnerImpl;
import fr.istic.gm.weassert.test.runner.TestRunnerListener;
import fr.istic.gm.weassert.test.utils.FileUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.scene.chart.PieChart.Data;

@Slf4j
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

    private File projectDirectory;

    private File mavenBinary;

    private int passedTest = 0;

    private int failedTests = 0;

    private WebEngine webEngine;

    @Setter(AccessLevel.PACKAGE)
    private WeAssertRunner weAssertRunner;

    public void initialize() {
        this.webEngine = this.webView.getEngine();
        TreeItem<String> rootItem = new TreeItem<>("[No project selected]");
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);

        this.testResultsChart.setStartAngle(45);

        ObservableList<Data> observableList = new ObservableListWrapper<>(Arrays.asList(new Data("Passed tests", this.passedTest), new Data("Failed tests", this.failedTests)));
        this.testResultsChart.setData(observableList);
    }

    public void browseAction() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        this.projectDirectory = directoryChooser.showDialog(null);
        if (this.projectDirectory == null) {
            this.selectedFile.setText("[No project selected]");
        } else {
            if (this.projectDirectory.isDirectory()) {
                this.selectedFile.setText(this.projectDirectory.getPath());

                this.weAssertRunner = new WeAssertRunnerImpl(
                        projectDirectory.getAbsolutePath(),
                        this.mavenBinary != null ? this.mavenBinary.getAbsolutePath() : "/usr/bin/mvn",
                        new TestRunnerListener() {
                            @Override
                            public void testRunFinished(Result result) throws Exception {
                                passedTest += result.getRunCount() - result.getFailureCount();
                                failedTests += result.getFailureCount();
                                Data passedTestData = new Data("Passed tests", passedTest);

                                Data failedTestData = new Data("Failed tests", failedTests);

                                ObservableList<Data> observableList = new ObservableListWrapper<>(Arrays.asList(passedTestData, failedTestData));
                                testResultsChart.setData(observableList);
                            }
                        }
                );

                showTestFiles(this.projectDirectory);
            }
        }
    }

    public void selectMavenAction() {
        FileChooser fileChooser = new FileChooser();
        this.mavenBinary = fileChooser.showOpenDialog(null);
        if (this.mavenBinary == null) {
            this.selectedMaven.setText("[System default]");
        } else {
            this.selectedMaven.setText(this.projectDirectory.getPath());
        }
    }

    public void generateAction() {
        this.weAssertRunner.generate();
    }

    public void testAction() {
        passedTest = 0;
        failedTests = 0;
        this.weAssertRunner.runTests();
    }

    public void itemClickedAction() {
        MultipleSelectionModel selectionModel = this.treeView.getSelectionModel();
        ObservableList<TreeItem> selectedItems = selectionModel.getSelectedItems();
        this.displaySourceCode(selectedItems.get(0).getValue().toString());
    }

    private void displaySourceCode(String s) {
        File sourceFile = new File(s);
        if (sourceFile.isFile() && !sourceFile.isDirectory()) {
            try {
                String sourceCode = new String(Files.readAllBytes(sourceFile.toPath()));

                this.webEngine.loadContent(String.format("<html><body><pre><code>\n" +
                        "%s\n" +
                        "</code></pre></body>", sourceCode));
            } catch (IOException e) {
                log.error("Can't read a source file", e);
            }
        }
    }

    private void showTestFiles(File directory) {
        this.treeView.getRoot().setValue(directory.getName());
        this.treeView.getRoot().getChildren().clear();

        List<TreeItem<String>> collect = FileUtils.findFilesFromFolder(directory)
                .stream().filter(f -> f.getName().endsWith("Test.java"))
                .map(f -> new TreeItem<>(f.getAbsolutePath()))
                .collect(Collectors.toList());

        this.treeView.getRoot().getChildren().addAll(collect);
    }
}
