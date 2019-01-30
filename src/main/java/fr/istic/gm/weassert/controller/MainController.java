package fr.istic.gm.weassert.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController {
    private File file;

    @FXML
    public Label selectedFile;

    @FXML
    public Button browseButton;


    public void initialize() {

    }

    public void browseAction(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        this.file = directoryChooser.showDialog(null);
        this.selectedFile.setText(this.file.getPath());
    }
}
