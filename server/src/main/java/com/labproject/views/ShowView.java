package com.labproject.views;

import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.services.StudyGroupService;

import java.io.Serializable;
import java.util.Collection;

public class ShowView implements View {
    private final StudyGroupService service = StudyGroupService.getInstance();

    @Override
    public CollectionResponse generateResponse(CollectionRequest request) {
        Collection<StudyGroup> studyGroups = service.show();
        return new CollectionResponse(true, "msg.success", (Serializable) studyGroups);
    }
}