package com.labproject.commands;

import com.labproject.app.App;
import com.labproject.client.Client;
import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.utils.AlertManager;

import java.util.Collection;

public class RemoveLowerCommand extends Command<Collection<StudyGroup>> {
    private final StudyGroup studyGroup;

    public RemoveLowerCommand(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    @Override
    protected Collection<StudyGroup> call() {
        CollectionRequest request = new CollectionRequest("remove_lower", studyGroup);
        CollectionResponse response = Client.getResponse(request);
        updateMessage(response.message);
        return response.success ? (Collection<StudyGroup>) response.object : null;
    }

    @Override
    protected void succeeded() {
        if (getValue() != null) App.getTableManager().removeAll(getValue());
        else AlertManager.showErrorAlert(getMessage());
    }
}
