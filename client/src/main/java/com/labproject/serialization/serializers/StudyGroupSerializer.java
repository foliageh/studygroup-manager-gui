package com.labproject.serialization.serializers;

import com.labproject.models.*;
import com.labproject.serialization.fields.*;
import com.labproject.serialization.exceptions.NotValidatedYetException;
import com.labproject.utils.Localization;

public class StudyGroupSerializer extends ModelSerializer<StudyGroup> {
    {
        fields.put("name", new StringField(Localization.localize("name"), false));
        fields.put("coordinates", new ModelField<>(Localization.localize("coordinates"), new CoordinatesSerializer()));
        fields.put("studentsCount", new LongField(Localization.localize("studentsCount"), true, -1L));
        fields.put("formOfEducation", new EnumField(Localization.localize("formOfEducation"), false, FormOfEducation.values()));
        fields.put("semester", new EnumField(Localization.localize("semester"), false, Semester.values()));
        fields.put("groupAdmin", new ModelField<>(Localization.localize("groupAdmin"), new PersonSerializer()));
    }

    public StudyGroupSerializer() {
        value = new StudyGroup();
    }
    public StudyGroupSerializer(StudyGroup initial) {
        if (initial == null) {
            value = new StudyGroup();
        } else {
            value = initial;
            setInputValue(initial);
        }
    }

    @Override
    public StudyGroup getValidatedValue() {
        if (!isValid)
            throw new NotValidatedYetException("You should run validate() method first.");
        value.setName((String) fields.get("name").getValidatedValue());
        value.setCoordinates((Coordinates) fields.get("coordinates").getValidatedValue());
        value.setStudentsCount((Long) fields.get("studentsCount").getValidatedValue());
        value.setFormOfEducation((FormOfEducation) fields.get("formOfEducation").getValidatedValue());
        value.setSemester((Semester) fields.get("semester").getValidatedValue());
        value.setGroupAdmin((Person) fields.get("groupAdmin").getValidatedValue());
        return value;
    }
}
