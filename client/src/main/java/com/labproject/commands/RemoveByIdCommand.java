package com.labproject.commands;

import com.labproject.app.App;
import com.labproject.client.Client;
import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.utils.AlertManager;

public class RemoveByIdCommand extends Command<StudyGroup> {
    private final long id;

    public RemoveByIdCommand(long id) {
        this.id = id;
    }

    @Override
    protected StudyGroup call() {
        CollectionRequest request = new CollectionRequest("remove_by_id", id);
        CollectionResponse response = Client.getResponse(request);
        updateMessage(response.message);
        return response.success ? (StudyGroup) response.object : null;
    }

    @Override
    protected void succeeded() {
        if (getValue() != null) App.getTableManager().remove(getValue());
        else AlertManager.showErrorAlert(getMessage());
    }
}
