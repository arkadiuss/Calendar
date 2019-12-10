package app.presenter.week;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import logic.model.Event;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class WeekViewDayPresenter {
    public AnchorPane dayPane;
    private static final double DAY_PX_HEIGHT = 52.0;
    private static final double DAY_PX_WIDTH = 75.0;
    private List<Event> events;
    private LocalDate selectedDate;

    @FXML
    private VBox hoursPane;

    private Label dayOfWeek;

    private String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @FXML
    public void initialize() {
        for (int i = 0; i < 25; i++) {
            if (i == 0) {
                ViewUtils.LoadedView<Object> n = ViewUtils.loadView("week/WeekViewHour.fxml");
                dayOfWeek = (Label) n.view.lookup("#hourView");
                hoursPane.getChildren().add(n.view);
                continue;
            }
            ViewUtils.LoadedView<Object> n = ViewUtils.loadView("week/WeekViewHour.fxml");
            Label label = (Label) n.view.lookup("#hourView");
            label.setText((i - 1) + ":00");
            hoursPane.getChildren().add(n.view);
        }
    }


    public void setDayNumber(int day) {
        dayOfWeek.setText(days[day]);
    }

    private void applyEvents() {
        List<Event> interestingEvents = events.stream()
                .filter(e -> (selectedDate.isBefore(e.getEndDateTime().toLocalDate()) &&
                        selectedDate.isAfter(e.getStartDateTime().toLocalDate())) ||
                        selectedDate.isEqual(e.getStartDateTime().toLocalDate()) ||
                        selectedDate.isEqual(e.getEndDateTime().toLocalDate()))
                .collect(Collectors.toList());
        System.out.println(Arrays.deepToString(interestingEvents.toArray()));
        interestingEvents.forEach(e -> {
            Label label = new Label(e.getTitle());

            label.setPrefWidth(DAY_PX_WIDTH);
            label.setLayoutX(60);
            label.setPrefHeight(countHeight(e));
            label.setStyle("-fx-background-color: #0000FF;");
            label.setLayoutY(countOffset(e));
            dayPane.getChildren().add(label);
        });
    }

    private double countHeight(Event e) {
        long minutes = e.getStartDateTime().until(e.getEndDateTime(), ChronoUnit.MINUTES);
        double hours = minutes / 60.0;
        return hours * DAY_PX_HEIGHT;
    }

    private double countOffset(Event e) {
        long minutes = e.getStartDateTime().toLocalDate().atStartOfDay().until(e.getStartDateTime(), ChronoUnit.MINUTES);
        double hours = minutes / 60.0;
        return hours * DAY_PX_HEIGHT;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        applyEvents();
    }
}
