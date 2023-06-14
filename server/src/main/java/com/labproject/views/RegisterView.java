package com.labproject.views;

import com.labproject.exceptions.ServerException;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.services.UserService;

public class RegisterView implements View {
    private final UserService service = UserService.getInstance();

    @Override
    public CollectionResponse generateResponse(CollectionRequest request) {
        try {
            service.register(request.getUser());
            return new CollectionResponse(true, "msg.registerSuccess");
        } catch (ServerException e) {
            return new CollectionResponse(false, e.getMessage());
        }
    }
}
