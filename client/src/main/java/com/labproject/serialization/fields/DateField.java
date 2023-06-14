package com.labproject.serialization.fields;

import com.labproject.serialization.exceptions.FieldNullValueException;
import com.labproject.utils.Localization;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class DateField extends Field<LocalDate> {
    private final DatePicker inputField = new DatePicker();
    { inputField.setEditable(false); }

    public DateField(String verboseName, boolean allowNull) {
        super(verboseName, allowNull);
        setTooltip();
    }

    @Override
    public Control getInputField() {
        return inputField;
    }

    @Override
    public void setEditable(boolean e) {
        inputField.setDisable(!e);
    }

    @Override
    public void setInputValue(Object value) {
        inputField.setValue((LocalDate) value);
    }

    @Override
    public void validate() throws FieldNullValueException {
        LocalDate value = inputField.getValue();
        if (value == null && !allowNull)
            throw new FieldNullValueException(verboseName()+" "+Localization.localize("msg.cantBeEmpty"));
        isValid = true;
        validatedValue = value;
    }
}
