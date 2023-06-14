package com.labproject.views;

import com.labproject.exceptions.ServerException;
import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.services.StudyGroupService;

import java.io.Serializable;
import java.util.Collection;

public class RemoveGreaterView implements View {
    private final StudyGroupService service = StudyGroupService.getInstance();

    @Override
    public CollectionResponse generateResponse(CollectionRequest request) {
        try {
            Collection<StudyGroup> studyGroups = service.removeGreater((StudyGroup) request.getObject(), request.getUser());
            return new CollectionResponse(true, "msg.success", (Serializable) studyGroups);
        } catch (ServerException e) {
            return new CollectionResponse(false, e.getMessage());
        }
    }
}
