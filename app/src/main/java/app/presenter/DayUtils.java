package app.presenter;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import logic.model.Event;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DayUtils {

    public static void applyEvents(Pane pane, LocalDate date, List<Event> events, double width, double height, double offsetX) {
        List<Event> interestingEvents = events.stream()
                .filter(e -> (date.isBefore(e.getEndDateTime().toLocalDate()) &&
                        date.isAfter(e.getStartDateTime().toLocalDate())) ||
                        date.isEqual(e.getStartDateTime().toLocalDate()) ||
                        date.isEqual(e.getEndDateTime().toLocalDate()))
                .collect(Collectors.toList());
        System.out.println(Arrays.deepToString(interestingEvents.toArray()));
        interestingEvents.forEach(e -> {
            Label label = new Label(e.getTitle());

            label.setPrefWidth(width);
            label.setLayoutX(offsetX);
            label.setPrefHeight(countHeight(e,height));
            label.setStyle("-fx-background-color: #0099FF; -fx-text-fill: #000000");
            label.setLayoutY(countOffset(e,height));
            pane.getChildren().add(label);
        });
    }

    private static double countHeight(Event e, double height) {
        long minutes = e.getStartDateTime().until(e.getEndDateTime(), ChronoUnit.MINUTES);
        double hours = minutes / 60.0;
        return hours * height;
    }

    private static double countOffset(Event e, double height) {
        long minutes = e.getStartDateTime().toLocalDate().atStartOfDay().until(e.getStartDateTime(), ChronoUnit.MINUTES);
        double hours = minutes / 60.0;
        return hours * height;
    }
}
