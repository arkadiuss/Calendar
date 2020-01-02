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

    public Event() {
    }

    public Event(String title, Place place, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.place = place;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
        reminder.setEventId(id);
    }

    public void addCalendar(Calendar calendar) {
        this.calendar = calendar;
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


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s \n %s - %s", title, startDateTime.format(formatter), endDateTime.format(formatter));
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
                Objects.equal(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, place, startDateTime, endDateTime, calendar, description);
    }
}
