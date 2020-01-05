package app;

import app.util.ColorRandomizer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AppContext {
    private BehaviorSubject<User> currentUser = BehaviorSubject.create();
    private BehaviorSubject<LocalDate> selectedDate = BehaviorSubject.create();
    private Set<Integer> selectedCalendars = new HashSet<>();
    private BehaviorSubject<List<Integer>> selectedCalendarsObservable = BehaviorSubject.create();
    private Map<Integer, String> color = new HashMap<>();

    public Observable<User> observeUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser.onNext(currentUser);
    }

    public void selectCalendar(int calendarId) {
        selectedCalendars.add(calendarId);
        selectedCalendarsObservable.onNext(new ArrayList<>(selectedCalendars));
    }

    public void unselectCalendar(int calendarId) {
        selectedCalendars.remove(calendarId);
        selectedCalendarsObservable.onNext(new ArrayList<>(selectedCalendars));
    }

    public Observable<LocalDate> observeSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate.onNext(selectedDate);
    }

    public Observable<List<Integer>> observeSelectedCalendars() {
        return selectedCalendarsObservable;
    }

    public String getColor(Event e) {
        int id = e.getCalendar().getId();
        if(!color.containsKey(id)) {
            color.put(id, ColorRandomizer.randomColor());
        }
        return color.get(id).replace("0x", "#");
    }
}
