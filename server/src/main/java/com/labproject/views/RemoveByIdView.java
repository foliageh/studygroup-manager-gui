package com.labproject.views;

import com.labproject.exceptions.ServerException;
import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.services.StudyGroupService;

public class RemoveByIdView implements View {
    private final StudyGroupService service = StudyGroupService.getInstance();

    @Override
    public CollectionResponse generateResponse(CollectionRequest request) {
        try {
            StudyGroup studyGroup = service.removeById((long) request.getObject(), request.getUser());
            return new CollectionResponse(true, "msg.success", studyGroup);
        } catch (ServerException e) {
            return new CollectionResponse(false, e.getMessage());
        }
    }
}
