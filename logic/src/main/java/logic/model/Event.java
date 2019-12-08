package logic.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
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
    private int calendarId;

    @OneToMany
    @JoinColumn(name = "reminderId")
    private Set<Reminder> reminders = new HashSet<>();

    public Event() {
    }

    public Event(String title, Place place, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.place = place;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public void addReminder(Reminder reminder){
        reminders.add(reminder);
        reminder.setEventId(id);
    }
}
