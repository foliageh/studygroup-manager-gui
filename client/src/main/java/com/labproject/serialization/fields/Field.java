package com.labproject.serialization.fields;

import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;

public abstract class Field<T> extends BaseField<T> {
    private final ArrayList<String> properties = new ArrayList<>();
    protected final boolean allowNull;

    public Field(String verboseName, boolean allowNull) {
        super(verboseName);
        this.allowNull = allowNull;
        addProperty(allowNull ? "can be empty" : "can't be empty");
    }

    protected final void addProperty(String property) {
        properties.add(property);
    }

    protected final void setTooltip() {
        getInputField().setTooltip(new Tooltip(properties()));
    }

    public final String properties() {
        return String.join(", ", properties);
    }

    public abstract Control getInputField();

    public abstract void setEditable(boolean e);
}
