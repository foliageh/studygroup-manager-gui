package com.labproject.services;

import com.labproject.exceptions.NotFound;
import com.labproject.exceptions.PermissionDenied;
import com.labproject.exceptions.ServerException;
import com.labproject.models.StudyGroup;
import com.labproject.models.User;
import com.labproject.repositories.StudyGroupRepository;

import java.util.Collection;
import java.util.HashMap;

/** Класс, осуществляющий бизнес-логику команд.
 * Вызывается View, валидирует переданные параметры и кидает исключения. */
public class StudyGroupService {
    private final StudyGroupRepository repository = StudyGroupRepository.getInstance();

    private static final StudyGroupService instance = new StudyGroupService();
    private StudyGroupService() {}
    public static StudyGroupService getInstance() {
        return instance;
    }

    public StudyGroup create(StudyGroup studyGroup, User user) {
        studyGroup.setCreator(user);
        return repository.create(studyGroup);
    }

    public StudyGroup update(StudyGroup studyGroup, User user) throws ServerException {
        StudyGroup oldStudyGroup = repository.findById(studyGroup.getId());
        if (oldStudyGroup == null)
            throw new NotFound("msg.groupNotExists");
        if (!oldStudyGroup.getCreator().equals(user))
            throw new PermissionDenied("msg.updatePermissionDenied");
        return repository.update(studyGroup);
    }

    public StudyGroup removeById(long id, User user) throws ServerException {
        StudyGroup studyGroup = repository.findById(id);
        if (studyGroup == null)
            throw new NotFound("msg.groupNotExists");
        if (!studyGroup.getCreator().equals(user))
            throw new PermissionDenied("msg.removePermissionDenied");
        return repository.remove(studyGroup);
    }

    public Collection<StudyGroup> removeGreater(StudyGroup studyGroup, User user) throws ServerException {
        if (repository.findById(studyGroup.getId()) == null)
            throw new NotFound("msg.groupNotExists");
        return repository.filterByCreator(user).filter(sg -> sg.compareTo(studyGroup) > 0).map(repository::remove).toList();
    }

    public Collection<StudyGroup> removeLower(StudyGroup studyGroup, User user) throws ServerException {
        if (repository.findById(studyGroup.getId()) == null)
            throw new NotFound("msg.groupNotExists");
        return repository.filterByCreator(user).filter(sg -> sg.compareTo(studyGroup) < 0).map(repository::remove).toList();
    }

    public void removeByUser(User user) {
        repository.filterByCreator(user).forEach(repository::remove);
    }

    public Collection<StudyGroup> show() {
        return repository.findAll().toList();
    }

    public HashMap<String, Object> info() {
        var map = new HashMap<String, Object>();
        map.put("collectionSize", repository.getCollectionSize());
        map.put("lastSaveTime", repository.getLastSaveTime());
        return map;
    }
}
