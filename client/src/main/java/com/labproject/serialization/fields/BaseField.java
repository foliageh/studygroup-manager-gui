package com.labproject.serialization.fields;

import com.labproject.serialization.exceptions.FieldValidationException;
import com.labproject.serialization.exceptions.NotValidatedYetException;

public abstract class BaseField<T> {
    private final String verboseName;
    protected T validatedValue;
    protected boolean isValid;

    public BaseField(String verboseName) {
        this.verboseName = verboseName;
    }

    public final String verboseName() {
        return verboseName;
    }

    public final boolean isValid() {
        return isValid;
    }

    /**
     * Возвращает провалидированное значение поля.
     * Если поле не было провалидировано методом validate(), выбрасывается исключение.
     * @throws NotValidatedYetException
     */
    public final T getValidatedValue() {
        if (!isValid) throw new NotValidatedYetException("You should run validate() method first.");
        return validatedValue;
    }

    public abstract void setInputValue(Object value);

    /**
     * Валидирует данные, введенные в поле.
     * Если они верны, устанавливается флаг isValid и данные записываются в validatedValue,
     * иначе выбрасывается исключение.
     * @throws FieldValidationException
     */
    public abstract void validate() throws FieldValidationException;
}
