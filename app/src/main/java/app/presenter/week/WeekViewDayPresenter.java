package app.presenter.week;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class WeekViewDayPresenter {
    @FXML
    private VBox hoursPane;

    private static int dayOfWeek = 1;

    private String getDayNameByValue() {
        switch (dayOfWeek) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "Sunday";
        }
    }

    @FXML
    public void initialize() {
        for (int i = 0; i < 25; i++) {
            if (i == 0) {
                ViewUtils.LoadedView<Object> n = ViewUtils.loadView("week/WeekViewHour.fxml");
                Label label = (Label) n.view.lookup("#hourView");
                label.setText(getDayNameByValue());
                dayOfWeek++;
                hoursPane.getChildren().add(n.view);
                continue;
            }
            ViewUtils.LoadedView<Object> n = ViewUtils.loadView("week/WeekViewHour.fxml");
            Label label = (Label) n.view.lookup("#hourView");
            label.setText((i - 1) + ":00");
            hoursPane.getChildren().add(n.view);
        }
    }
}
