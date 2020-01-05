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
    private Set<Event> events = new HashSet<>();

    public Calendar() {
    }

    public Calendar(String name, User user) {
        this.name = name;
        this.user = user;
        user.addCalendar(this);
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addEvent(Event event){
        events.add(event);
        event.setCalendar(this);
    }

    public void removeEvent(Event event){
        events.remove(event);
        event.setCalendar(null);
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
    public String toString(){
        return "Calendar " + name;
    }
}
