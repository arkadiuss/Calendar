package logic.service;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import logic.dao.UserDao;
import logic.model.User;

import java.util.Optional;

public class UserService {
    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public Single<User> getUser(String name, String password) {
        return Single.fromCallable(() -> userDao.getUser(name, password))
                .map(Optional::orElseThrow)
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> checkIfUserExists(String name, String password) {
        return Single.fromCallable(() -> userDao.getUser(name, password))
                .map(Optional::isPresent)
                .subscribeOn(Schedulers.io());
    }

    public Completable addUser(User user) {
        return Completable.fromAction(() -> userDao.addUser(user))
                .subscribeOn(Schedulers.io());
    }
}
