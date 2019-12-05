package logic.service;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import logic.dao.CalendarDao;
import logic.model.Calendar;

import java.util.List;

public class CalendarService {
    private CalendarDao calendarDao;

    public CalendarService() {
        this.calendarDao = new CalendarDao();
    }

    public Single<List<Calendar>> getCalendars() {
        return Single.fromCallable(() -> calendarDao.getCalendars())
                .subscribeOn(Schedulers.io());
    }

    public Completable addCalendar(Calendar calendar) {
        return Completable.fromAction(() -> calendarDao.addCalendar(calendar))
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteCalendar(Calendar calendar) {
        return Completable.fromAction(() -> calendarDao.deleteCalendar(calendar))
                .subscribeOn(Schedulers.io());
    }


    public Completable updateCalendar(Calendar calendar){
        return Completable.fromAction(() -> calendarDao.updateCalendar(calendar))
                .subscribeOn(Schedulers.io());
    }
}
