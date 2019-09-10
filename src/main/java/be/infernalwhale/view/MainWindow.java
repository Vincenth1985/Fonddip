package be.infernalwhale.view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;


public class MainWindow extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        File file = Paths.get("src/main/java/be/infernalwhale/view/fondip.fxml").toFile();
        Parent parent = FXMLLoader.load(file.toURI().toURL());

        stage.setTitle("Tutoriel Java FX");

        stage.setScene(new Scene(parent, 800, 600));
        stage.setResizable(false);
        stage.show();

    }

    public void startGui(java.lang.String[] args) {


        launch(args);

    }
}
