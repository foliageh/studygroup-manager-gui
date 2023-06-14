package com.labproject.commands;

import com.labproject.client.Client;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.utils.Localization;

import java.util.Date;
import java.util.Map;

public class InfoCommand extends Command<String> {
    @Override
    public String call() {
        CollectionRequest request = new CollectionRequest("info");
        CollectionResponse response = Client.getResponse(request);
        updateMessage(response.message);
        var map = (Map<String, Object>) response.object;
        String info = Localization.localize("collectionSize") + ": \n  " + map.get("collectionSize") + "\n";
        info += Localization.localize("lastSaveTime") + ": \n  " + (map.get("lastSaveTime")!=null ? Localization.localize((Date) map.get("lastSaveTime")) : Localization.localize("no"));
        return response.success ? info : null;
    }
}
