package com.labproject.controllers;

import com.labproject.app.*;
import com.labproject.client.Client;
import com.labproject.commands.*;
import com.labproject.models.StudyGroup;
import com.labproject.utils.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    @FXML
    private Button localeButton;
    @FXML
    private Label username;
    @FXML
    private Button logoutButton;
    @FXML
    private Button createButton;
    @FXML
    private TableView<StudyGroup> tableView;
    @FXML
    private Button removeButton;
    @FXML
    private Label collectionInfo;
    @FXML
    private Button resetFiltersButton;
    @FXML
    private Button reloadDataButton;
    @FXML
    private Button removeLowerButton;
    @FXML
    private Button removeGreaterButton;
    @FXML
    public ChoiceBox<String> filterFieldChoiceBox;
    @FXML
    public TextField filterField;
    @FXML
    public Pane visualizationPane;
    @FXML
    public TabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (resources.getLocale().toString().isEmpty()) return;
        username.setText(Client.getUser().getUsername());

        App.setTableManager(new TableManager(tableView));
        visualizationPane.maxWidthProperty().bind(tableView.widthProperty());
        visualizationPane.maxHeightProperty().bind(tableView.heightProperty());

        localeButton.setOnAction(e -> Localization.toggleLanguage());
        filterField.setOnKeyTyped(e -> App.getTableManager().fieldFilter(filterFieldChoiceBox.getValue(), filterField.getText()));
        filterFieldChoiceBox.getItems().setAll(Arrays.stream(StudyGroup.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList()));
        filterFieldChoiceBox.setValue(filterFieldChoiceBox.getItems().get(0));
        filterFieldChoiceBox.setOnAction(e -> App.getTableManager().fieldFilter(filterFieldChoiceBox.getValue(), filterField.getText()));
        resetFiltersButton.setOnAction(e -> {
            filterField.clear();
            App.getTableManager().resetFilters();
        });
        createButton.setOnAction(e -> ScreenManager.showStudyGroupCardScreen(null));
        removeButton.setOnAction(e -> {
            var selected = App.getTableManager().getSelected();
            if (selected != null) CommandExecutor.execute(new RemoveByIdCommand(selected.getId()));
        });
        removeLowerButton.setOnAction(e -> {
            var selected = App.getTableManager().getSelected();
            if (selected != null) CommandExecutor.execute(new RemoveLowerCommand(selected));
        });
        removeGreaterButton.setOnAction(e -> {
            var selected = App.getTableManager().getSelected();
            if (selected != null) CommandExecutor.execute(new RemoveGreaterCommand(selected));
        });
        reloadDataButton.setOnAction(e -> CommandExecutor.execute(new CollectCommand()));

        CommandExecutor.startUpdateLoop(collectionInfo.textProperty());

        var visualizationController = new VisualizationController(visualizationPane);
        visualizationController.initialize();
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        Client.setUser(null);
        ScreenManager.showAuthScreen();
    }
}