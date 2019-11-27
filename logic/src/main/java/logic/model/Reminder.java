package logic.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int eventId;
    private String description;
    private Date date;

    public Reminder() {
    }

    public Reminder(String description, Date date) {
        this.description = description;
        this.date = date;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
