package com.labproject.commands;

import com.labproject.app.App;
import com.labproject.client.Client;
import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.utils.AlertManager;

import java.util.Collection;

public class CollectCommand extends Command<Collection<StudyGroup>> {
    @Override
    public Collection<StudyGroup> call() {
        CollectionRequest request = new CollectionRequest("show");
        CollectionResponse response = Client.getResponse(request);
        updateMessage(response.message);
        return response.success ? (Collection<StudyGroup>) response.object : null;
    }

    @Override
    protected void succeeded() {
        if (getValue() != null) getValue().forEach(studyGroup -> App.getTableManager().update(studyGroup));
        else AlertManager.showErrorAlert(getMessage());
    }
}
