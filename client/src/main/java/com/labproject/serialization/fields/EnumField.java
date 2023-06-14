package com.labproject.serialization.fields;

import com.labproject.serialization.exceptions.FieldNullValueException;
import com.labproject.utils.Localization;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;

public class EnumField extends Field<Enum<?>> {
    private final ChoiceBox<Enum<?>> inputField = new ChoiceBox<>();

    public EnumField(String verboseName, boolean allowNull, Enum<?>[] choices) {
        super(verboseName, allowNull);
        inputField.getItems().setAll(choices);
        if (allowNull) inputField.getItems().add(0, null);
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
        inputField.setValue((Enum<?>) value);
    }

    @Override
    public void validate() throws FieldNullValueException {
        var value = inputField.getValue();
        if (value == null && !allowNull)
            throw new FieldNullValueException(verboseName()+" "+Localization.localize("msg.cantBeEmpty"));
        isValid = true;
        validatedValue = value;
    }
}
