package logic.model;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    @Embedded
    private Place place;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "calendarId")
    private Calendar calendar;

    @OneToMany
    @JoinColumn(name = "reminderId")
    private Set<Reminder> reminders = new HashSet<>();

    private String description;

    private boolean isAllDay;

    public Event() {
    }

    public Event(String title, Place place, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isAllDay) {
        this.title = title;
        this.place = place;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isAllDay = isAllDay;
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
        reminder.setEventId(id);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Place getPlace() {
        return place;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setAllDay(boolean allDay) {
        this.isAllDay = allDay;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Title: %s \nPlace: %s \n %s - %s", title, place, startDateTime.format(formatter), endDateTime.format(formatter));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equal(title, event.title) &&
                Objects.equal(place, event.place) &&
                Objects.equal(startDateTime, event.startDateTime) &&
                Objects.equal(endDateTime, event.endDateTime) &&
                Objects.equal(calendar, event.calendar) &&
                Objects.equal(description, event.description) &&
                Objects.equal(isAllDay, event.isAllDay);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, place, startDateTime, endDateTime, calendar, description, isAllDay);
    }

    public boolean contains(String phrase) {
        phrase = phrase.toLowerCase();
        return compare(phrase, title) || compare(place.getName(), phrase)
                || compare(place.getAddress(), phrase) || compare(description, phrase);
    }

    private boolean compare(String first, String second) {
        if (first == null || second == null) {
            return false;
        }
        return first.toLowerCase().equals(second.toLowerCase());
    }
}
