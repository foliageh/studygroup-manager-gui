package com.labproject.commands;

import com.labproject.app.App;
import com.labproject.client.Client;
import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.utils.AlertManager;

public class CreateCommand extends Command<StudyGroup> {
    private final StudyGroup studyGroup;

    public CreateCommand(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    @Override
    protected StudyGroup call() {
        CollectionRequest request = new CollectionRequest("add", studyGroup);
        CollectionResponse response = Client.getResponse(request);
        updateMessage(response.message);
        return response.success ? (StudyGroup) response.object : null;
    }

    @Override
    protected void succeeded() {
        if (getValue() != null) App.getTableManager().add(getValue());
        else AlertManager.showErrorAlert(getMessage());
    }
}
