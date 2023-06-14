package com.labproject.utils;

import com.labproject.commands.RemoveByIdCommand;
import com.labproject.models.StudyGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.lang.reflect.Field;
import java.util.*;

public class TableManager {
    private final ObservableList<StudyGroup> collection = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private final TableView<StudyGroup> tableView;

    public TableManager(TableView<StudyGroup> tableView) {
        this.tableView = tableView;

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setRowFactory(tv -> {
            TableRow<StudyGroup> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty())
                    ScreenManager.showStudyGroupCardScreen(row.getItem());
            });
            return row;
        });
        tableView.setOnKeyPressed(event -> {
            var studyGroup = getSelected();
            if (event.getCode() == KeyCode.DELETE && studyGroup != null)
                CommandExecutor.execute(new RemoveByIdCommand(studyGroup.getId()));
        });

        for (var field : StudyGroup.class.getDeclaredFields()) {
            TableColumn<StudyGroup, Integer> col = new TableColumn<>(field.getName());
            col.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            tableView.getColumns().add(col);
        }

        showAll();
    }

    private void showAll() {
        tableView.setItems(collection);
    }

    public void fieldFilter(String fieldName, String query) {
        try {
            Field field = StudyGroup.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            tableView.setItems(collection.filtered(studyGroup -> {
                try {
                    var fieldValue = field.get(studyGroup);
                    return fieldValue == null || fieldValue.toString().toLowerCase().contains(query.toLowerCase());
                } catch (IllegalAccessException ignored) { return false; }
            }));
        } catch (ReflectiveOperationException ignored) {}
    }

    public void resetFilters() {
        showAll();
    }

    public StudyGroup getSelected() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    public void setAll(Collection<StudyGroup> studyGroups) {
        collection.setAll(studyGroups);
    }

    public void add(StudyGroup studyGroup) {
        collection.add(studyGroup);
    }

    public void update(StudyGroup studyGroup) {
        if (collection.contains(studyGroup)) collection.set(collection.indexOf(studyGroup), studyGroup);
        else add(studyGroup);
    }

    public void remove(StudyGroup studyGroup) {
        collection.remove(studyGroup);
    }

    public void removeAll(Collection<StudyGroup> studyGroups) {
        collection.removeAll(studyGroups);
    }

    public ObservableList<StudyGroup> getCollection() {
        return collection;
    }
}
