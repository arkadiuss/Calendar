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
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractDayView {

    public void applyEvents(Pane pane, LocalDate date, List<Event> events) {
        pane.getChildren().clear();
        List<Event> interestingEvents = events.stream()
                .filter(e -> (date.isBefore(e.getEndDateTime().toLocalDate()) &&
                        date.isAfter(e.getStartDateTime().toLocalDate())) ||
                        date.isEqual(e.getStartDateTime().toLocalDate()) ||
                        date.isEqual(e.getEndDateTime().toLocalDate()))
                .collect(Collectors.toList());
        interestingEvents.forEach(e -> {
            Label label = new Label(e.getTitle());

            label.setPrefWidth(getHourWidth());
            label.setLayoutX(getEventOffset());
            label.setPrefHeight(countHeight(e));
            label.setStyle("-fx-background-color: #0099FF; -fx-text-fill: #000000");
            label.setLayoutY(countOffset(e));
            label.setOnMouseClicked(event -> {
                ViewUtils.LoadedView<Node, EventDetailsViewPresenter> view = ViewUtils.loadView("EventDetailsView.fxml");
                view.controller.setEvent(e);
                Stage stage = new Stage();
                stage.setTitle("Event details");
                stage.setScene(new Scene((Parent) view.view, 600, 450));
                stage.show();
            });
            pane.getChildren().add(label);
        });
    }

    protected abstract double getHourHeight();
    protected abstract double getEventOffset();
    protected abstract double getHourWidth();

    private double countHeight(Event e) {
        long minutes = e.getStartDateTime().until(e.getEndDateTime(), ChronoUnit.MINUTES);
        double hours = minutes / 60.0;
        return hours * getHourHeight();
    }

    private double countOffset(Event e) {
        long minutes = e.getStartDateTime().toLocalDate().atStartOfDay().until(e.getStartDateTime(), ChronoUnit.MINUTES);
        double hours = minutes / 60.0;
        return hours * getHourHeight();
    }
}
