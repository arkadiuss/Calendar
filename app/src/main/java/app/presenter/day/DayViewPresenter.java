package app.presenter.day;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import logic.model.Event;

import java.util.List;

public class DayViewPresenter {
    @FXML
    private VBox hoursPane;

    private List<Event> events;

    @FXML
    public void initialize(){
        for (int i=0; i < 24; i++){
            ViewUtils.LoadedView<Object> n = ViewUtils.loadView("day/DayViewHour.fxml");
//            n.prefWidth(hoursBox.getWidth());
            Label label = (Label)n.view.lookup("#hourView");
            label.setText(i+":00");
            hoursPane.getChildren().add(n.view);

//            hoursBox.getChildren().add(ViewUtils.loadView("day/DayViewHour.fxml"));
//            ViewUtils.loadView("day/DayViewHour.fxml");
        }
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
