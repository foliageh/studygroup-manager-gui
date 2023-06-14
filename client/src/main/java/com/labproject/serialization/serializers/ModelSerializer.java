package com.labproject.serialization.serializers;

import com.labproject.models.Model;
import com.labproject.serialization.exceptions.FieldValidationException;
import com.labproject.serialization.fields.BaseField;

import java.util.LinkedHashMap;

public abstract class ModelSerializer<T extends Model> {
    public final LinkedHashMap<String, BaseField<?>> fields = new LinkedHashMap<>();
    protected T value;
    protected boolean isValid;

    public final boolean isValid() {
        return isValid;
    }

    public final void setInputValue(Object value) {
        if (value == null) return;
        for (String objFieldName : fields.keySet()) {
            BaseField<?> field = fields.get(objFieldName);
            try {
                var objField = value.getClass().getDeclaredField(objFieldName);
                objField.setAccessible(true);
                var objFieldValue = objField.get(value);
                if (objFieldValue != null)
                    field.setInputValue(objFieldValue);
            } catch (Exception e) { throw new RuntimeException(e); }
        }
    }

    public final void validate() throws FieldValidationException {
        isValid = false;
        for (var field : fields.values()) field.validate();
        isValid = true;
    }

    public abstract T getValidatedValue();
}
