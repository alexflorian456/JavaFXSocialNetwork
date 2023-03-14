package com.example.labgui2;

import DTO.MessageDTO;
import Domain.User;
import Exceptions.RepoException;
import Exceptions.ValidationException;
import Service.Service;
import Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MessageScreenController implements Observer {
    @FXML
    private TableColumn<MessageDTO,String> usernameTableColumn;
    @FXML
    private TableColumn<MessageDTO,String> contentTableColumn;
    @FXML
    private TextField messageTF;
    @FXML
    private Button sendBTN;

    private ObservableList<MessageDTO> model = FXCollections.observableArrayList();

    @FXML
    private Button homeBTN;
    @FXML
    private Button friendsBTN;
    @FXML
    private Button requestsBTN;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button settingsBTN;
    @FXML
    private TableView<MessageDTO> messageTable;

    private User loggedInUser;

    private Service service;
    private int messagedId;

    @FXML
    public void initialize() {
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<MessageDTO,String>("username"));
        contentTableColumn.setCellValueFactory(new PropertyValueFactory<MessageDTO,String>("content"));
        messageTable.setItems(model);
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
        service.removeObserver(this);
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
        service.removeObserver(this);
    }

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
        service.removeObserver(this);
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
        service.removeObserver(this);
    }

    public void onSend(ActionEvent actionEvent) {
        String text = messageTF.getText();
        if(!text.isBlank()){
            try {
                service.addMessage(loggedInUser.getID(),messagedId,text);
                messageTF.setText("");
            } catch (ValidationException | RepoException e) {
                e.print();
            }
        }
        initModel();
    }

    @Override
    public void update() {
        initModel();
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser=user;
    }

    public void setService(Service service) {
        this.service=service;
        this.service.addObserver(this);
        initModel();
    }

    private void initModel() {
        List<MessageDTO> messageDTOS = service.getMessagesOf(loggedInUser.getID(),messagedId);
        model.setAll(messageDTOS);
    }

    public void updateUsernameLabel() {
        usernameLabel.setText(loggedInUser.getUsername());
    }

    public void setMessagedUser(int messagedId) {
        this.messagedId=messagedId;
    }
}
