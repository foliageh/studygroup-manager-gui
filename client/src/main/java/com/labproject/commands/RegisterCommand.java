package com.labproject.commands;

import com.labproject.models.User;
import com.labproject.client.Client;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.utils.AlertManager;
import com.labproject.utils.ScreenManager;

public class RegisterCommand extends Command<Boolean> {
    private final User user;

    public RegisterCommand(User user) {
        this.user = user;
    }

    @Override
    protected Boolean call() {
        Client.setUser(user);
        CollectionRequest request = new CollectionRequest("register");
        CollectionResponse response = Client.getResponse(request);
        updateMessage(response.message);
        return response.success;
    }

    @Override
    protected void succeeded() {
        if (getValue()) ScreenManager.showMainScreen();
        else AlertManager.showErrorAlert(getMessage());
    }
}
