package com.labproject.services;

import com.labproject.repositories.UserRepository;
import com.labproject.exceptions.*;
import com.labproject.models.User;
import com.labproject.utils.PasswordEncoder;

/** Класс, осуществляющий пользовательскую бизнес-логику.
 * Вызывается View, валидирует переданные параметры и кидает исключения. */
public class UserService {
    private final UserRepository repository = UserRepository.getInstance();

    private static final UserService instance = new UserService();
    private UserService() {}
    public static UserService getInstance() {
        return instance;
    }

    public void register(User user) throws ServerException {
        if (repository.findByUsername(user.getUsername()) != null) {
            throw new ValidationFailed("msg.usernameExists");
        }
        repository.create(user);
    }

    public User login(User u) throws ServerException {
        User user = repository.findByUsername(u.getUsername());
        if (user == null) {
            throw new NotFound("msg.usernameNotFound");
        }
        if (!PasswordEncoder.encode(u.getPassword()).equals(user.getPassword())) {
            throw new ValidationFailed("msg.wrongPassword");
        }
        return user;
    }
}
