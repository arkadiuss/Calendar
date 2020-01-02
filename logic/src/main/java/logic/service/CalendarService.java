package logic.service;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import logic.dao.CalendarDao;
import logic.exceptions.EventConflictException;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.User;
import logic.util.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Completable updateCompletable = Completable.fromAction(() -> calendarDao.updateCalendar(calendar))
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> updateUserCalendars(calendar.getUser()));

        return getCalendars(calendar.getUser())
                .flatMapCompletable(calendars -> {
                    if(isConflict(calendar, calendars)) throw new EventConflictException("Unknown");
                    return updateCompletable;
                });
    }

    private boolean isConflict(Calendar calendar, List<Calendar> calendars) {
        List<Event> restEvents = calendars.stream().flatMap(c -> c.getEvents().stream()).collect(Collectors.toList());
        return getNewEvents(calendar).stream()
                .anyMatch(e -> checkConflicts(e, restEvents));
    }

    private List<Event> getNewEvents(Calendar c) {
        return c.getEvents()
                .stream()
                .filter(e -> e.getId() == 0)
                .collect(Collectors.toList());
    }

    private boolean checkConflicts(Event event, List<Event> currentEvents) {
        return currentEvents.stream()
                .anyMatch(e -> e.getId() != event.getId() && DateUtils.IsCoincident(e.getStartDateTime(), e.getEndDateTime(), event.getStartDateTime(), event.getEndDateTime()));
    }

    private void updateUserCalendars(User user) {
        getUserCalendars()
                .subscribe(userCalendars.get(user)::onNext);
    }

    private Single<List<Calendar>> getUserCalendars() {
        return Single.fromCallable(() -> calendarDao.getCalendars())
                .subscribeOn(Schedulers.io());
    }
}
