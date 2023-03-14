package com.example.labgui2;

import Domain.User;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsScreenController {
    @FXML
    private Button deleteAccBTN;
    @FXML
    private Button logoutBTN;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button friendsBTN;
    @FXML
    private Button requestsBTN;
    @FXML
    private Button homeBTN;

    private Service service;
    private User loggedInUser;

    public void openRequestsScreen(ActionEvent actionEvent) {
        Stage thisStage = (Stage) homeBTN.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("requestScreen.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 533);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thisStage.setScene(scene);

        RequestsScreenController controller = fxmlLoader.getController();


        controller.setLoggedInUser(loggedInUser);
        controller.setService(service);
        controller.updateUsernameLabel();
    }

    public void openFriendsScreen(ActionEvent actionEvent) {
        Stage thisStage = (Stage) homeBTN.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("friendScreen.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 533);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thisStage.setScene(scene);

        FriendsScreenController controller = fxmlLoader.getController();


        controller.setLoggedInUser(loggedInUser);
        controller.setService(service);
        controller.updateUsernameLabel();
    }

    public void openMainScreen(ActionEvent actionEvent) {
        Stage thisStage = (Stage) homeBTN.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homeScreen.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 533);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thisStage.setScene(scene);

        HomeScreenController controller = fxmlLoader.getController();


        controller.setLoggedInUser(loggedInUser);
        controller.setService(service);
        controller.updateUsernameLabel();
    }

    public void setService(Service service) {
        this.service=service;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser=user;
    }


    public void updateUsernameLabel() {
        usernameLabel.setText(loggedInUser.getUsername());
    }

    public void onLogout(ActionEvent actionEvent) {
        Stage thisStage = (Stage) logoutBTN.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginScreen.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 533);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thisStage.setScene(scene);

        LoginScreenController controller = fxmlLoader.getController();

        controller.setService(service);
    }

    public void onDeleteAccount(ActionEvent actionEvent) {
        Stage thisStage = (Stage) logoutBTN.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("delAccScreen.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 533);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thisStage.setScene(scene);

        DelAccScreenController controller = fxmlLoader.getController();


        controller.setLoggedInUser(loggedInUser);
        controller.setService(service);
        controller.updateUsernameLabel();
    }
}
