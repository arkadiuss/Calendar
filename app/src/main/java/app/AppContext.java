package app;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import logic.model.User;

import java.time.LocalDate;

public class AppContext {
    private BehaviorSubject<User> currentUser = BehaviorSubject.create();
    private BehaviorSubject<LocalDate> selectedDate = BehaviorSubject.create();

    public Observable<User> observeUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser.onNext(currentUser);
    }

    public Observable<LocalDate> observeSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.onNext(selectedDate);
    }
}
