package app.presenter;

import app.util.ViewUtils;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
        interestingEvents.forEach(e -> {
            Label label = new Label(e.getTitle());

            label.setPrefWidth(width);
            label.setLayoutX(offsetX);
            label.setPrefHeight(countHeight(e,height));
            label.setStyle("-fx-background-color: #0099FF; -fx-text-fill: #000000");
            label.setLayoutY(countOffset(e,height));
            label.setOnMouseClicked(event -> {
                ViewUtils.LoadedView view = ViewUtils.loadView("EventDetailsView.fxml");
                ((EventDetailsViewPresenter)view.controller).setEvent(e);
                Stage stage = new Stage();
                stage.setTitle("Event details");
                stage.setScene(new Scene((Parent) view.view, 600, 450));
                stage.show();
            });
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
