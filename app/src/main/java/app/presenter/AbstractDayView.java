package app.presenter;

import app.AppContext;
import app.util.DateUtils;
import app.util.ViewUtils;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


public abstract class AbstractDayView {
    protected String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public void applyEvents(AppContext context, Pane pane, LocalDate date, List<Event> events) {
        pane.getChildren().clear();
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atStartOfDay().plusDays(1);
        List<Event> interestingEvents = events.stream()
                .filter(e -> DateUtils.IsCoincident(e.getStartDateTime(), e.getEndDateTime(), dayStart, dayEnd))
                .collect(Collectors.toList());
        interestingEvents.forEach(e -> {
            LocalDateTime startDate = DateUtils.maximum(e.getStartDateTime(), date.atStartOfDay());
            LocalDateTime endDate = DateUtils.minimum(e.getEndDateTime(), date.atStartOfDay().plusDays(1));

            Label label = new Label(e.getTitle());

            if(e.isAllDay()){
                label.setPrefWidth(getHourWidth());
                label.setLayoutX(getEventOffset());
                label.setPrefHeight(getDayEventHeight());
                label.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: #000000", context.getColor(e)));
                label.setLayoutY(getDayEventOffset());

            }else{
                label.setPrefWidth(getHourWidth());
                label.setLayoutX(getEventOffset());
                label.setPrefHeight(countHeight(startDate, endDate));
                label.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: #000000", context.getColor(e)));
                label.setLayoutY(countOffset(startDate));
            }

            label.setOnMouseClicked(event -> {
                ViewUtils.LoadedView<Node, EventDetailsViewPresenter> view = ViewUtils.loadView("EventDetailsView.fxml");
                view.controller.setEvent(e);
                //not best solution
                view.controller.setEventSaved(aBoolean -> label.setText(e.getTitle()));
                Stage stage = new Stage();
                stage.setTitle("Event details");
                stage.setScene(new Scene((Parent) view.view, 600, 450));
                view.controller.setDialogStage(stage);
                stage.show();
            });
            pane.getChildren().add(label);
        });
    }

    protected abstract double getHourHeight();
    protected abstract double getEventOffset();
    protected abstract double getHourWidth();
    protected abstract double getHeaderOffset();
    protected abstract double getDayEventHeight();
    protected abstract double getDayEventOffset();


    private double countHeight(LocalDateTime startDate, LocalDateTime endDate) {
        double hours = DateUtils.differenceInHours(startDate, endDate);
        return hours* getHourHeight();
    }

    private double countOffset(LocalDateTime startTime) {
        long minutes = startTime.toLocalDate().atStartOfDay().until(startTime, ChronoUnit.MINUTES);
        double hours = minutes / 60.0;
        return hours * getHourHeight() + getHeaderOffset();
    }
}
