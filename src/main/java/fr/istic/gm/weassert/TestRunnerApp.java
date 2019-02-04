package fr.istic.gm.weassert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestRunnerApp extends Application {
    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/rootPane.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("WeAssert");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}
