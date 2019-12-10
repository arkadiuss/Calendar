package app.presenter.week;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class WeekViewDayPresenter {
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
}
