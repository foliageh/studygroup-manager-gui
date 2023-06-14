package com.labproject.serialization.fields;

import com.labproject.serialization.exceptions.FieldNullValueException;
import com.labproject.serialization.exceptions.FieldValidationException;
import com.labproject.utils.Localization;
import javafx.scene.control.*;

public class StringField extends Field<String> {
    private final TextField inputField = new TextField();

    public StringField(String verboseName, boolean allowNull) {
        super(verboseName, allowNull);
        setTooltip();
    }

    @Override
    public Control getInputField() {
        return inputField;
    }

    @Override
    public void setEditable(boolean e) {
        inputField.setEditable(e);
    }

    @Override
    public void setInputValue(Object value) {
        inputField.setText(value.toString());
    }

    @Override
    public void validate() throws FieldValidationException {
        String value = inputField.getText();
        value = value.isBlank() ? null : value;
        if (value == null && !allowNull)
            throw new FieldNullValueException(verboseName()+" "+Localization.localize("msg.cantBeEmpty"));
        isValid = true;
        validatedValue = value;
    }
}
