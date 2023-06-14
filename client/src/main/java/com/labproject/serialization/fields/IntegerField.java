package com.labproject.serialization.fields;

import com.labproject.serialization.exceptions.FieldNullValueException;
import com.labproject.serialization.exceptions.FieldValidationException;
import com.labproject.serialization.exceptions.InvalidFieldFormatException;
import com.labproject.utils.Localization;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class IntegerField extends Field<Integer> {
    private final TextField inputField = new TextField();
    private Integer lowerBound;

    public IntegerField(String verboseName, boolean allowNull) {
        super(verboseName, allowNull);
        addProperty("integer");
        setTooltip();
    }

    public IntegerField(String verboseName, boolean allowNull, Integer lowerBound) {
        this(verboseName, allowNull);
        this.lowerBound = lowerBound;
        addProperty("must be greater than " + lowerBound);
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
        String _value = inputField.getText();
        _value = _value.isBlank() ? null : _value;
        if (_value == null) {
            if (allowNull) {
                isValid = true;
                validatedValue = null;
                return;
            }
            throw new FieldNullValueException(verboseName()+" "+Localization.localize("msg.cantBeEmpty"));
        }
        try {
            validatedValue = Integer.parseInt(_value);
        } catch (NumberFormatException e) {
            throw new InvalidFieldFormatException(verboseName()+" "+Localization.localize("msg.invalidFieldFormat"));
        }
        if (lowerBound != null && validatedValue <= lowerBound) {
            throw new InvalidFieldFormatException(verboseName()+" "+Localization.localize("msg.mustBeGreaterThan")+" "+lowerBound);
        }
        isValid = true;
    }
}
