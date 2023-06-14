package com.labproject.serialization.serializers;

import com.labproject.models.Location;
import com.labproject.serialization.exceptions.NotValidatedYetException;
import com.labproject.serialization.fields.DoubleField;
import com.labproject.serialization.fields.LongField;
import com.labproject.serialization.fields.StringField;
import com.labproject.utils.Localization;

public class LocationSerializer extends ModelSerializer<Location> {
    {
        fields.put("x", new DoubleField(Localization.localize("xCoordinate"), false));
        fields.put("y", new DoubleField(Localization.localize("yCoordinate"), false));
        fields.put("z", new LongField(Localization.localize("zCoordinate"), false));
        fields.put("name", new StringField(Localization.localize("locationName"), true));
    }

    @Override
    public Location getValidatedValue() {
        if (!isValid())
            throw new NotValidatedYetException("You should run validate() method first.");
        return new Location((double) fields.get("x").getValidatedValue(),
                            (double) fields.get("y").getValidatedValue(),
                            (Long) fields.get("z").getValidatedValue(),
                            (String) fields.get("name").getValidatedValue());
    }
}
