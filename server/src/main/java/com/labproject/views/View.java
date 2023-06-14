package com.labproject.views;

import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;

public interface View {
    CollectionResponse generateResponse(CollectionRequest request);
}
