package com.labproject.commands;

import com.labproject.app.App;
import com.labproject.client.Client;
import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.utils.AlertManager;

public class UpdateCommand extends Command<StudyGroup> {
    private final StudyGroup studyGroup;

    public UpdateCommand(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    @Override
    protected StudyGroup call() {
        CollectionRequest request = new CollectionRequest("update", studyGroup);
        CollectionResponse response = Client.getResponse(request);
        updateMessage(response.message);
        return response.success ? (StudyGroup) response.object : null;
    }

    @Override
    protected void succeeded() {
        if (getValue() != null) App.getTableManager().update(getValue());
        else AlertManager.showErrorAlert(getMessage());
    }
}