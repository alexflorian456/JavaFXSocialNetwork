package com.example.labgui2;

import Domain.User;
import Exceptions.ServiceException;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
//TODO: set loggedinuser, apoi set service!!!

public class HomeScreenController implements Observer {
    ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    public TableView<User> userTableView;
    @FXML
    public TableColumn<User, String> usernameTableColumn;
    @FXML
    public TableColumn<User, String> firstnameTableColumn;
    @FXML
    public TableColumn<User, String> lastnameTableColumn;
    @FXML
    private Button settingsBTN;
    @FXML
    private Button addFriendBTN;
    @FXML
    private Button searchBTN;
    @FXML
    private TextField searchTF;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button logoutBTN;
    @FXML
    private Button homeBTN;
    @FXML
    private Button friendsBTN;
    @FXML
    private Button requestsBTN;
    private Service service;
    private User loggedInUser;

    public void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        firstnameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));
        lastnameTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));
        userTableView.setItems(model);
    }

    private void initModel() {
        String searchKey = searchTF.getText();
        Predicate<User> searchPredicate;
        if (!searchKey.isBlank()) {
            searchPredicate = x -> (
                        x.getUsername().contains(searchKey)
                        || x.getFirstname().contains(searchKey)
                        || x.getLastname().contains(searchKey)
                    )
                    && !x.getUsername().equals(loggedInUser.getUsername());
        } else if (loggedInUser != null) {
            searchPredicate = x -> !x.getUsername().equals(loggedInUser.getUsername());
        } else {
            searchPredicate = x -> true;
        }

        Iterable<User> users = service.getAllUsers();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .filter(searchPredicate)
                .collect(Collectors.toList());
        model.setAll(userList);
    }

    public void setLoggedInUser(User find) {
        this.loggedInUser = find;
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

    public void updateUsernameLabel() {
        usernameLabel.setText(loggedInUser.getUsername());
    }

    public void onSearch(ActionEvent actionEvent) {
        update();
    }

    public void onAddFriend(ActionEvent actionEvent) {
        User user = (User) userTableView.getSelectionModel().getSelectedItem();
        if(user!=null){
            try {
                service.addFriend(loggedInUser.getID(),user.getID());
            } catch (ServiceException e) {
                e.print();
            }
        }
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

    @Override
    public void update() {
        initModel();
    }
}
