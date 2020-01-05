import com.sendgrid.helpers.mail.objects.Email;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.User;
import logic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    private final static Logger logger = LoggerFactory.getLogger(Sender.class);
    private final static long SECONDS_BEFORE_EVENT = 10 * 60;
    private final static int MILLIS = 20 * 1000;

    public static void main(String[] args) {
        Email from = new Email("calendar-notifier@aniolki_charliego.pl");
        Sender sender = new Sender(System.getenv("API_KEY"), from);
        UserService userService = new UserService();
        while (true) {
            LocalDateTime now = LocalDateTime.now();
            userService.getUsersList()
                    .subscribe(users -> sendNotificationsToUsers(sender, now, users),
                            error -> logger.error(error.getMessage()));
            try {
                Thread.sleep(MILLIS);
            } catch (InterruptedException e) {
                logger.error("Sleep interrupted", e);
            }
        }
    }

    private static void sendNotificationsToUsers(Sender sender, LocalDateTime now, List<User> users) {
        users.forEach(user -> sendNotification(getUserUpcomingEvents(now, user), user, sender));
    }

    private static Set<Event> getUserUpcomingEvents(LocalDateTime now, User user) {
        Set<Event> upcomingEvents = new HashSet<>();
        user.getCalendars().forEach(calendar -> filterEvents(now, upcomingEvents, calendar));
        return upcomingEvents;
    }

    private static void filterEvents(LocalDateTime now, Set<Event> upcomingEvents, Calendar calendar) {
        for (Event event : calendar.getEvents()) {
            LocalDateTime diff = event.getStartDateTime().minusSeconds(SECONDS_BEFORE_EVENT).truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime truncated = now.truncatedTo(ChronoUnit.MINUTES);
            if (diff.isEqual(truncated)) {
                upcomingEvents.add(event);
            }
        }
    }

    private static void sendNotification(Collection<Event> events, User user, Sender sender) {
        StringBuilder stringBuilder = new StringBuilder();
        events.forEach(event -> stringBuilder.append(event).append("\n"));
        String content = String.format("Dear %s,\nYou have following upcoming events:\n%s", user.getUsername(), stringBuilder.toString());
        sender.sendNotification(new Email(user.getEmail()), content);
    }
}
