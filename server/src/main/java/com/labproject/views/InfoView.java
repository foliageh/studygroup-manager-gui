package com.labproject.views;

import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.services.StudyGroupService;

public class InfoView implements View {
    private final StudyGroupService service = StudyGroupService.getInstance();

    @Override
    public CollectionResponse generateResponse(CollectionRequest request) {
        return new CollectionResponse(true, "msg.success", service.info());
    }
}
