package logic.service;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import logic.dao.EventDao;
import logic.model.Event;

public class EventService {
    private EventDao eventDao;

    public EventService() {
        this.eventDao = new EventDao();
    }

    public Completable updateEvent(Event event) {
        return Completable.fromAction(() -> eventDao.updateEvent(event))
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteEvent(Event event) {
        return Completable.fromAction(() -> eventDao.deleteEvent(event))
                .subscribeOn(Schedulers.io());
    }
}
