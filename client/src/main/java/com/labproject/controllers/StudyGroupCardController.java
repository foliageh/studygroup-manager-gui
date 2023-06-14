package com.labproject.controllers;

import com.labproject.utils.AlertManager;
import com.labproject.utils.CommandExecutor;
import com.labproject.utils.Localization;
import com.labproject.utils.ScreenManager;
import com.labproject.client.Client;
import com.labproject.commands.*;
import com.labproject.models.StudyGroup;
import com.labproject.serialization.exceptions.FieldValidationException;
import com.labproject.serialization.fields.BaseField;
import com.labproject.serialization.fields.Field;
import com.labproject.serialization.fields.ModelField;
import com.labproject.serialization.serializers.ModelSerializer;
import com.labproject.serialization.serializers.StudyGroupSerializer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StudyGroupCardController implements Initializable {
    @FXML
    private Label titleLabel;
    @FXML
    private GridPane fieldPane;
    @FXML
    private GridPane buttonPane;
    @FXML
    private Button createButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeButton;

    private StudyGroup studyGroup;
    private final StudyGroupSerializer studyGroupSerializer;
    private final boolean isNewGroup;
    private final boolean isOurGroup;

    public StudyGroupCardController(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
        studyGroupSerializer = new StudyGroupSerializer(studyGroup);
        isNewGroup = studyGroup == null;
        isOurGroup = isNewGroup || studyGroup.getCreator().equals(Client.getUser());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (isNewGroup) {
            titleLabel.setText(Localization.localize("newStudyGroup"));
        } else {
            titleLabel.setText(Localization.localize("studyGroup")+" #"+studyGroup.getId());
            buttonPane.getChildren().remove(createButton);
        }
        if (isNewGroup || !isOurGroup) {
            buttonPane.getChildren().remove(updateButton);
            buttonPane.getChildren().remove(removeButton);
        }
        placeInputFields(studyGroupSerializer, 0);

        createButton.setOnAction(this::create);
        updateButton.setOnAction(this::update);
        removeButton.setOnAction(this::remove);
    }

    private void placeInputFields(ModelSerializer<?> serializer, int inputFieldLevel) {
        for (BaseField<?> field : serializer.fields.values()) {
            fieldPane.add(new Label("  ".repeat(inputFieldLevel) + field.verboseName() + ":"), 0, fieldPane.getRowCount());
            if (field instanceof Field<?> f) {
                if (!isOurGroup) f.setEditable(false);
                fieldPane.add(f.getInputField(), 1, fieldPane.getRowCount() - 1);
            } else {
                placeInputFields(((ModelField<?>) field).getSerializer(), inputFieldLevel + 1);
            }
        }
    }

    private StudyGroup getValidatedStudyGroup() {
        try {
            studyGroupSerializer.validate();
            return studyGroupSerializer.getValidatedValue();
        } catch (FieldValidationException e) {
            AlertManager.showErrorAlert(e.getMessage(), false);
            return null;
        }
    }

    @FXML
    public void create(ActionEvent actionEvent) {
        studyGroup = getValidatedStudyGroup();
        if (studyGroup == null) return;

        var command = new CreateCommand(studyGroup);
        command.setOnSucceeded(event -> {
            if (command.getValue() != null) {
                ((Stage) titleLabel.getScene().getWindow()).close();
                ScreenManager.showStudyGroupCardScreen(command.getValue());
            }
        });
        CommandExecutor.execute(command);
    }

    @FXML
    public void update(ActionEvent actionEvent) {
        studyGroup = getValidatedStudyGroup();
        if (studyGroup == null) return;

        var command = new UpdateCommand(studyGroup);
        command.setOnSucceeded(event -> {
            if (command.getValue() != null) {
                ((Stage) titleLabel.getScene().getWindow()).close();
                ScreenManager.showStudyGroupCardScreen(command.getValue());
            }
        });
        CommandExecutor.execute(command);
    }

    @FXML
    public void remove(ActionEvent actionEvent) {
        var command = new RemoveByIdCommand(studyGroup.getId());
        command.setOnSucceeded(event -> {
            if (command.getValue() != null) {
                ((Stage) titleLabel.getScene().getWindow()).close();
            }
        });
        CommandExecutor.execute(command);
    }
}
