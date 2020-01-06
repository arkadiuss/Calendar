package logic.service;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import logic.dao.UserDao;
import logic.exceptions.UserAlreadyExistException;
import logic.model.User;

import java.util.List;
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

    private Single<Boolean> checkIfUserExists(String name) {
        return Single.fromCallable(() -> userDao.getUser(name))
                .map(Optional::isPresent)
                .subscribeOn(Schedulers.io());
    }

    public Single<List<User>> getUsersList() {
        return Single.fromCallable(() -> userDao.getUsers())
                .subscribeOn(Schedulers.io());
    }

    public Completable addUser(User user) {
        Completable addUserCompletable = Completable.fromAction(() -> userDao.addUser(user))
                .subscribeOn(Schedulers.io());

        return checkIfUserExists(user.getUsername())
                .flatMapCompletable(exists -> {
                    if(exists) throw new UserAlreadyExistException();
                    return addUserCompletable;
                });
    }
}
