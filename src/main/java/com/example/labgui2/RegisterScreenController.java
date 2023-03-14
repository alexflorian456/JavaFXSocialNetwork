package com.example.labgui2;

import Domain.User;
import Exceptions.RepoException;
import Exceptions.ValidationException;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterScreenController {
    @FXML
    private Label registerStatusLabel;
    @FXML
    private Button registerBTN;
    @FXML
    private Hyperlink logInHL;
    @FXML
    private TextField usernameTF;
    @FXML
    private TextField firstnameTF;
    @FXML
    private TextField lastnameTF;
    @FXML
    private PasswordField passwordTF;
    @FXML
    private PasswordField confirmTF;
    private Service service;

    public void setService(Service service){
        this.service=service;
    }

    public void openLoginScreen(ActionEvent actionEvent) {
        Stage thisStage = (Stage) registerBTN.getScene().getWindow();

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

    public void onRegister(ActionEvent actionEvent) {
        String username = usernameTF.getText();
        String firstname = firstnameTF.getText();
        String lastname = lastnameTF.getText();
        String password = passwordTF.getText();
        String confirm = confirmTF.getText();

        if(username.isBlank()||firstname.isBlank()||lastname.isBlank()||password.isBlank()||confirm.isBlank()){
            registerStatusLabel.setTextFill(Paint.valueOf("#ff3f3f"));
            registerStatusLabel.setText("All fields must be filled");
            return;
        }

        if(!password.equals(confirm)){
            registerStatusLabel.setTextFill(Paint.valueOf("#ff3f3f"));
            registerStatusLabel.setText("Confirmed password does not match password");
            return;
        }

        try {
            service.addUser(username,firstname,lastname,password);
            registerStatusLabel.setTextFill(Paint.valueOf("#7def51"));
            registerStatusLabel.setText("Account successfully registered");
        } catch (ValidationException | RepoException e) {
            registerStatusLabel.setTextFill(Paint.valueOf("#ff3f3f"));
            registerStatusLabel.setText(e.getMessage());
        }
    }
}
