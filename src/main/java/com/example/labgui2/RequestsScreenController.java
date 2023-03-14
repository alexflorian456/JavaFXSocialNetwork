package com.example.labgui2;

import DTO.UserFriendshipDTO;
import Domain.FriendshipStatus;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestsScreenController implements Observer {
    @FXML
    private Label acceptLabel;
    @FXML
    private Button denyFriendBTN;
    ObservableList<UserFriendshipDTO> model = FXCollections.observableArrayList();
    @FXML
    private TableView<UserFriendshipDTO> friendshipTableView;
    @FXML
    private TableColumn<UserFriendshipDTO, String> usernameTableColumn;
    @FXML
    private TableColumn<UserFriendshipDTO, LocalDateTime> requestSentTableColumn;
    @FXML
    private TableColumn<UserFriendshipDTO, FriendshipStatus> statusTableColumn;
    @FXML
    private Button accFriendBTN;
    @FXML
    private Button settingsBTN;
    @FXML
    private TextField searchTF;
    @FXML
    private Button searchBTN;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button logoutBTN;
    @FXML
    private Button requestsBTN;
    @FXML
    private Button friendsBTN;
    @FXML
    private Button homeBTN;

    private Service service;

    private User loggedInUser;

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
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

    public void updateUsernameLabel() {
        usernameLabel.setText(loggedInUser.getUsername());
    }

    public void onSearch(ActionEvent actionEvent) {
        update();
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

    public void onAccFriend(ActionEvent actionEvent) {
        UserFriendshipDTO friendshipDTO = (UserFriendshipDTO) friendshipTableView.getSelectionModel().getSelectedItem();
        if (friendshipDTO != null) {
            try {
                if (friendshipDTO.getSenderID() != loggedInUser.getID()) {
                    service.acceptFriend(loggedInUser.getUsername(), friendshipDTO.getUsername());
                    acceptLabel.setText("");
                }else{
                    acceptLabel.setText("You can't accept a request that you have sent");
                }
            } catch (ServiceException e) {
                e.print();
            }
        }
    }

    @Override
    public void update() {
        initModel();
    }

    private void initModel() {
        String searchKey = searchTF.getText();
        Predicate<UserFriendshipDTO> searchPredicate;
        if (!searchKey.isBlank()) {
            searchPredicate = x -> x.getUsername().contains(searchKey) && x.getStatus() == FriendshipStatus.PENDING;//&&x.getSenderID()!=loggedInUser.getID();
        } else {
            searchPredicate = x -> x.getStatus() == FriendshipStatus.PENDING;//&&x.getSenderID()!=loggedInUser.getID();
        }

        Iterable<UserFriendshipDTO> users = service.getFriendsOf(loggedInUser);
        List<UserFriendshipDTO> userList = StreamSupport.stream(users.spliterator(), false)
                .filter(searchPredicate)
                .collect(Collectors.toList());
        model.setAll(userList);
    }

    @FXML
    public void initialize() {
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<UserFriendshipDTO, String>("username"));
        requestSentTableColumn.setCellValueFactory(new PropertyValueFactory<UserFriendshipDTO, LocalDateTime>("friendsFrom"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<UserFriendshipDTO, FriendshipStatus>("status"));
        friendshipTableView.setItems(model);
    }

    public void onDenyFriend(ActionEvent actionEvent) {
        UserFriendshipDTO friendshipDTO = (UserFriendshipDTO) friendshipTableView.getSelectionModel().getSelectedItem();
        if (friendshipDTO != null) {
            try {
                service.removeFriend(loggedInUser.getUsername(), friendshipDTO.getUsername());
            } catch (ServiceException e) {
                e.print();
            }
        }
    }
}
