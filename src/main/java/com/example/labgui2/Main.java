package com.example.labgui2;

import Exceptions.CorruptionException;
import Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginScreen.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 300, 533);
        stage.setTitle("Soychan");
        stage.setScene(scene);

        Service service;

        try {
            service = new Service("jdbc:postgresql://localhost:5432/lab8","postgres","postgres");
        } catch (CorruptionException e) {
            e.print();
            return;
        }

        LoginScreenController controller = fxmlLoader.getController();
        controller.setService(service);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}