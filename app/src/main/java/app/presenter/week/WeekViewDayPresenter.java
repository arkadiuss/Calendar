package app.presenter.week;

import app.presenter.AbstractDayView;
import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import logic.model.Event;

import java.time.LocalDate;
import java.util.List;


public class WeekViewDayPresenter extends AbstractDayView {
    public AnchorPane dayPane;
    private static final double DAY_PX_HEIGHT = 56.0;
    private static final double DAY_PX_WIDTH = 75.0;
    private List<Event> events;

    @FXML
    private VBox hoursPane;

    private Label dayOfWeek;

    private LocalDate date;

    private String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @FXML
    public void initialize() {
        for (int i = 0; i < 25; i++) {
            if (i == 0) {
                ViewUtils.LoadedView<Node, WeekViewDayPresenter> n = ViewUtils.loadView("week/WeekViewHour.fxml");
                dayOfWeek = (Label) n.view.lookup("#hourView");
                hoursPane.getChildren().add(n.view);
                continue;
            }
            ViewUtils.LoadedView<Node, WeekViewDayPresenter> n = ViewUtils.loadView("week/WeekViewHour.fxml");
            Label label = (Label) n.view.lookup("#hourView");
            label.setText((i - 1) + ":00");
            hoursPane.getChildren().add(n.view);
        }
    }


    public void setDate(LocalDate date) {
        this.date = date;
        dayOfWeek.setText(days[date.getDayOfWeek().getValue() - 1]);
        applyEvents(dayPane, date, events, DAY_PX_WIDTH, DAY_PX_HEIGHT, 0);
    }


    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
