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
    private Date date;

    @ManyToOne
    @JoinColumn(name = "calendarId")
    private Calendar calendar;

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

    public void addReminder(Reminder reminder){
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

}
