package com.labproject.controllers;

import com.labproject.utils.AlertManager;
import com.labproject.utils.CommandExecutor;
import com.labproject.commands.LoginCommand;
import com.labproject.commands.RegisterCommand;
import com.labproject.models.User;
import com.labproject.serialization.exceptions.FieldValidationException;
import com.labproject.serialization.fields.Field;
import com.labproject.serialization.serializers.UserSerializer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AuthController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private User getValidatedUser() {
        try {
            UserSerializer userSerializer = new UserSerializer();
            var uf = (TextInputControl) ((Field<?>) userSerializer.fields.get("username")).getInputField();
            uf.setText(usernameField.getText());
            var pf = (TextInputControl) ((Field<?>) userSerializer.fields.get("password")).getInputField();
            pf.setText(passwordField.getText());
            userSerializer.validate();
            return userSerializer.getValidatedValue();
        } catch (FieldValidationException e) {
            AlertManager.showErrorAlert(e.getMessage(), false);
            return null;
        }
    }

    @FXML
    public void login(ActionEvent actionEvent) {
        User user = getValidatedUser();
        if (user == null) return;
        CommandExecutor.execute(new LoginCommand(user));
    }

    @FXML
    public void register(ActionEvent actionEvent) {
        User user = getValidatedUser();
        if (user == null) return;
        CommandExecutor.execute(new RegisterCommand(user));
    }
}
