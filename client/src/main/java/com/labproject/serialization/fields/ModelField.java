package com.labproject.serialization.fields;

import com.labproject.models.Model;
import com.labproject.serialization.exceptions.FieldValidationException;
import com.labproject.serialization.serializers.ModelSerializer;

public class ModelField<T extends Model> extends BaseField<T> {
    private final ModelSerializer<T> serializer;

    public ModelField(String verboseName, ModelSerializer<T> serializer) {
        super(verboseName);
        this.serializer = serializer;
    }

    public ModelSerializer<T> getSerializer() {
        return serializer;
    }

    @Override
    public void setInputValue(Object value) {
        serializer.setInputValue(value);
    }

    @Override
    public void validate() throws FieldValidationException {
        isValid = false;
        serializer.validate();
        validatedValue = serializer.getValidatedValue();
        isValid = true;
    }
}
