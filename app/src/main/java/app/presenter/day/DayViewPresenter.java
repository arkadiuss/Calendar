package app.presenter.day;

import app.presenter.DayUtils;
import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import logic.model.Event;

import java.time.LocalDate;
import java.util.List;

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
            ViewUtils.LoadedView<Node, DayViewPresenter> n = ViewUtils.loadView("day/DayViewHour.fxml");
            Label label = (Label)n.view.lookup("#hourView");
            label.setText(i+":00");
            hoursPane.getChildren().add(n.view);
        }
    }

    private void applyEvents() {
        DayUtils.applyEvents(dayPane, selectedDate, events, DAY_PX_WIDTH, DAY_PX_HEIGHT, 60);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        applyEvents();
    }
}
