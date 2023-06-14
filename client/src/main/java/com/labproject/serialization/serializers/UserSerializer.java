package com.labproject.serialization.serializers;

import com.labproject.models.User;
import com.labproject.serialization.exceptions.*;
import com.labproject.serialization.fields.*;
import com.labproject.utils.Localization;

public class UserSerializer extends ModelSerializer<User> {
    {
        fields.put("username", new StringField(Localization.localize("username"), false));
        fields.put("password", new StringField(Localization.localize("password"), false));
    }

    public UserSerializer() {
        value = new User();
    }

    @Override
    public User getValidatedValue() {
        if (!isValid())
            throw new NotValidatedYetException("You should run validate() method first.");
        value.setUsername((String) fields.get("username").getValidatedValue());
        value.setPassword((String) fields.get("password").getValidatedValue());
        return value;
    }
}
