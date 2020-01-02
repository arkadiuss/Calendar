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

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private Set<Event> events;

    public Calendar() {
    }

    public Calendar(String name, User user) {
        this.name = name;
        this.user = user;
        this.events = new HashSet<>();
        user.addCalendar(this);
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addEvent(Event event) {
        events.add(event);
        event.addCalendar(this);
    }

    public Set<Event> getEvents() {
        return events;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Calendar " + name;
    }
}
