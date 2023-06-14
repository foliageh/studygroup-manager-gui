package com.labproject.views;

import com.labproject.models.StudyGroup;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.services.StudyGroupService;

public class AddView implements View {
    private final StudyGroupService service = StudyGroupService.getInstance();

    @Override
    public CollectionResponse generateResponse(CollectionRequest request) {
        StudyGroup studyGroup = service.create((StudyGroup) request.getObject(), request.getUser());
        return new CollectionResponse(true, "msg.success", studyGroup);
    }
}
