package com.labproject.repositories;

import com.labproject.dao.StudyGroupDao;
import com.labproject.models.StudyGroup;
import com.labproject.models.User;

import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

/** Класс для взаимодействия с коллекцией через Dao, предоставляющий дополнительный функционал
 * для коллекции, согласно заданию лабораторной работы. */
public class StudyGroupRepository {
    private final ConcurrentSkipListSet<StudyGroup> collection;
    private Date lastSaveTime;

    private final StudyGroupDao studyGroupDao = new StudyGroupDao();

    private static final StudyGroupRepository instance = new StudyGroupRepository();
    private StudyGroupRepository() {
        collection = new ConcurrentSkipListSet<>(studyGroupDao.findAll());
    }
    public static StudyGroupRepository getInstance() {
        return instance;
    }

    public int getCollectionSize() {
        return collection.size();
    }

    public Date getLastSaveTime() {
        return lastSaveTime;
    }

    public Stream<StudyGroup> findAll() {
        return collection.stream().sorted(Comparator.comparing(StudyGroup::getCoordinates));
    }

    public StudyGroup findById(long id) {
        return findAll().filter(sg -> sg.getId() == id).findAny().orElse(null);
    }

    public Stream<StudyGroup> filterByCreator(User user) {
        return findAll().filter(sg -> sg.getCreator().equals(user));
    }

    public StudyGroup create(StudyGroup studyGroup) {
        studyGroupDao.create(studyGroup);
        collection.add(studyGroup);
        lastSaveTime = new Date();
        return studyGroup;
    }

    public StudyGroup update(StudyGroup studyGroup) {
        studyGroupDao.update(studyGroup);
        collection.remove(studyGroup);
        collection.add(studyGroup);
        lastSaveTime = new Date();
        return studyGroup;
    }

    public StudyGroup remove(StudyGroup studyGroup) {
        studyGroupDao.remove(studyGroup);
        collection.remove(studyGroup);
        lastSaveTime = new Date();
        return studyGroup;
    }
}
