package logic.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "calendars")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String description;

    @OneToMany
    @JoinColumn(name = "calendarId")
    private Set<Event> events = new HashSet<>();

    public Calendar() {
    }

    public Calendar(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addEvent(Event event){
        events.add(event);
        event.setCalendarId(id);
    }
}
