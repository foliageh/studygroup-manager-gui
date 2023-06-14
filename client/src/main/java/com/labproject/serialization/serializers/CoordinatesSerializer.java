package com.labproject.serialization.serializers;

import com.labproject.models.Coordinates;
import com.labproject.serialization.exceptions.NotValidatedYetException;
import com.labproject.serialization.fields.DoubleField;
import com.labproject.serialization.fields.IntegerField;
import com.labproject.utils.Localization;

public class CoordinatesSerializer extends ModelSerializer<Coordinates> {
    {
        fields.put("x", new IntegerField(Localization.localize("xCoordinate"), false, -261));
        fields.put("y", new DoubleField(Localization.localize("yCoordinate"), false, -162D));
    }

    @Override
    public Coordinates getValidatedValue() {
        if (!isValid())
            throw new NotValidatedYetException("You should run validate() method first.");
        return new Coordinates((int) fields.get("x").getValidatedValue(),
                               (double) fields.get("y").getValidatedValue());
    }
}
