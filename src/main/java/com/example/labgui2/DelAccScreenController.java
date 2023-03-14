package com.example.labgui2;

import Domain.User;
import Exceptions.RepoException;
import Exceptions.ValidationException;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DelAccScreenController {
    @FXML
    private Button noBTN;
    @FXML
    private Button yesBTN;
    @FXML
    private Button settingsBTN;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button homeBTN;
    @FXML
    private Button friendsBTN;
    @FXML
    private Button requestsBTN;

    private User loggedInUser;
    private Service service;

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

    public void onLogout() {
        Stage thisStage = (Stage) yesBTN.getScene().getWindow();

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

    public void openSettingsScreen(ActionEvent actionEvent) {
        Stage thisStage = (Stage) homeBTN.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("settingsScreen.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 533);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thisStage.setScene(scene);

        SettingsScreenController controller = fxmlLoader.getController();


        controller.setLoggedInUser(loggedInUser);
        controller.setService(service);
        controller.updateUsernameLabel();
    }

    public void onYes(ActionEvent actionEvent) {
        try {
            service.removeUser(loggedInUser);
        } catch (ValidationException | RepoException e) {
            e.print();
            return;
        }
        onLogout();
    }

    public void onNo(ActionEvent actionEvent) {
        openSettingsScreen(null);
    }
}
