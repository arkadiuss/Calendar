package app.presenter.week;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import logic.model.Event;

import java.time.LocalDate;
import java.util.List;

public class WeekViewPresenter {

    @FXML
    private HBox weekPane;
    private LocalDate selectedDate;

    private List<Event> events;

    private void setupView() {
        LocalDate startDayOfWeek = selectedDate.minusDays(selectedDate.getDayOfWeek().getValue()-1);
        for (int i = 0; i < 7; i++) {
            ViewUtils.LoadedView loadedView = ViewUtils.loadView("week/WeekViewDay.fxml");
            Node n = loadedView.view;
            n.prefWidth(weekPane.getWidth() / 7.0);
            n.prefHeight(weekPane.getHeight());
            ((WeekViewDayPresenter) loadedView.controller).setEvents(events);
            ((WeekViewDayPresenter) loadedView.controller).setDate(startDayOfWeek.plusDays(i));
            weekPane.getChildren().add(n);
        }
    }

    public void setCurrentDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        setupView();
        Node node = weekPane.getChildren().get(selectedDate.getDayOfWeek().getValue() - 1);
        node.setStyle("-fx-background-color: #FFCFFF;");
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
