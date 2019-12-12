package app;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppContext {
    private BehaviorSubject<User> currentUser = BehaviorSubject.create();
    private BehaviorSubject<LocalDate> selectedDate = BehaviorSubject.create();
    private List<Calendar> selectedCalendars = new ArrayList<>();
    private BehaviorSubject<List<Calendar>> selectedCalendarsObservable = BehaviorSubject.create();

    public Observable<User> observeUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser.onNext(currentUser);
    }

    public void selectCalendar(Calendar calendar) {
        selectedCalendars.add(calendar);
        System.out.println(selectedCalendars.size());
        selectedCalendarsObservable.onNext(selectedCalendars);
    }

    public void unselectCalendar(Calendar calendar) {
        selectedCalendars.remove(calendar);
        System.out.println(selectedCalendars.size());
        selectedCalendarsObservable.onNext(selectedCalendars);
    }

    public Observable<LocalDate> observeSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.onNext(selectedDate);
    }

    public Observable<List<Calendar>> observeCalendars() {
        return selectedCalendarsObservable;
    }

    public Observable<List<Event>> observeEvents() {
        return selectedCalendarsObservable
                .map(calendars -> calendars.stream().flatMap(c -> c.getEvents().stream()).collect(Collectors.toList()));
    }
}
