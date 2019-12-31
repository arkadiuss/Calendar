package logic.service;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import logic.dao.CalendarDao;
import logic.model.Calendar;
import logic.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarService {
    private CalendarDao calendarDao;
    private Map<User, BehaviorSubject<List<Calendar>>> userCalendars = new HashMap<>();

    public CalendarService() {
        this.calendarDao = new CalendarDao();
    }

    public Observable<List<Calendar>> getCalendars(User user) {
        if(userCalendars.containsKey(user)) return userCalendars.get(user);

        BehaviorSubject<List<Calendar>> subject = BehaviorSubject.create();
        userCalendars.put(user, subject);
        updateUserCalendars(user);
        return subject;
    }

    public Completable addCalendar(Calendar calendar) {
        return Completable.fromAction(() -> calendarDao.addCalendar(calendar))
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> updateUserCalendars(calendar.getUser()));
    }

    public Completable deleteCalendar(Calendar calendar) {
        return Completable.fromAction(() -> calendarDao.deleteCalendar(calendar))
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> updateUserCalendars(calendar.getUser()));
    }


    public Completable updateCalendar(Calendar calendar){
        return Completable.fromAction(() -> calendarDao.updateCalendar(calendar))
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> updateUserCalendars(calendar.getUser()));
    }

    private void updateUserCalendars(User user) {
        Single.fromCallable(() -> calendarDao.getCalendars())
                .subscribeOn(Schedulers.io())
                .subscribe(userCalendars.get(user)::onNext);
    }
}
