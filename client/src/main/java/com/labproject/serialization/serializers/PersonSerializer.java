package com.labproject.serialization.serializers;

import com.labproject.models.Color;
import com.labproject.models.Location;
import com.labproject.models.Person;
import com.labproject.serialization.exceptions.NotValidatedYetException;
import com.labproject.serialization.fields.DateField;
import com.labproject.serialization.fields.EnumField;
import com.labproject.serialization.fields.ModelField;
import com.labproject.serialization.fields.StringField;
import com.labproject.utils.Localization;

import java.time.LocalDate;

public class PersonSerializer extends ModelSerializer<Person> {
    {
        fields.put("name", new StringField(Localization.localize("name"), false));
        fields.put("birthday", new DateField(Localization.localize("birthday"), true));
        fields.put("hairColor", new EnumField(Localization.localize("hairColor"), false, Color.values()));
        fields.put("location", new ModelField<>(Localization.localize("location"), new LocationSerializer()));
    }

    @Override
    public Person getValidatedValue() {
        if (!isValid())
            throw new NotValidatedYetException("You should run validate() method first.");
        return new Person((String) fields.get("name").getValidatedValue(),
                          (LocalDate) fields.get("birthday").getValidatedValue(),
                          (Color) fields.get("hairColor").getValidatedValue(),
                          (Location) fields.get("location").getValidatedValue());
    }
}
