package com.labproject.repositories;

import com.labproject.models.User;
import com.labproject.dao.UserDao;

/** Класс для взаимодействия с Dao. Пока бесполезен, но, как дополнительная абстракция,
 * может пригодится в дальнейшем при расширении функционала. */
public class UserRepository {
    private final UserDao dao = new UserDao();

    private static final UserRepository instance = new UserRepository();
    private UserRepository() {}
    public static UserRepository getInstance() {
        return instance;
    }

    public void create(User user) {
        dao.create(user);
    }

    public User findByUsername(String username) {
        return dao.findByUsername(username);
    }
}
