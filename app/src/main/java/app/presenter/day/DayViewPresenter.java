package app.presenter.day;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import logic.model.Event;

import javax.swing.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DayViewPresenter {
    private static final double DAY_PX_HEIGHT = 52.0;
    private static final double DAY_PX_WIDTH = 490.0;

    @FXML
    private AnchorPane dayPane;

    @FXML
    private VBox hoursPane;

    private List<Event> events;

    private LocalDate selectedDate;

    @FXML
    public void initialize(){
        for (int i=0; i < 24; i++){
            ViewUtils.LoadedView<Object> n = ViewUtils.loadView("day/DayViewHour.fxml");
            Label label = (Label)n.view.lookup("#hourView");
            label.setText(i+":00");
            hoursPane.getChildren().add(n.view);
        }
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
        double hours = minutes/60.0;
        return hours * DAY_PX_HEIGHT;
    }

    private double countOffset(Event e) {
        long minutes = e.getStartDateTime().toLocalDate().atStartOfDay().until(e.getStartDateTime(), ChronoUnit.MINUTES);
        double hours = minutes/60.0;
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
