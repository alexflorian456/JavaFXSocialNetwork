package com.example.labgui2;

import Domain.User;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreenController {

    private Service service;

    @FXML
    private Label loginStatusLabel;

    @FXML
    private TextField usernameTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Button loginBTN;

    @FXML
    private Hyperlink registerHL;

    public void onLogin(ActionEvent actionEvent) {
        String username;
        String password;

        username=usernameTF.getText();
        password=passwordTF.getText();

        User find = service.findUserPass(username,password);

        if(find==null){
            loginStatusLabel.setText("Incorrect username or password");
            return;
        }

        Stage thisStage = (Stage) loginBTN.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homeScreen.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 533);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thisStage.setScene(scene);

        HomeScreenController controller = fxmlLoader.getController();

        controller.setLoggedInUser(find);
        controller.setService(service);
        controller.updateUsernameLabel();
    }

    public void setService(Service service) {
        this.service=service;
    }

    public void openRegisterScreen(ActionEvent actionEvent) {
        Stage thisStage = (Stage) loginBTN.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("registerScreen.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 533);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thisStage.setScene(scene);

        RegisterScreenController controller = fxmlLoader.getController();

        controller.setService(service);
    }
}