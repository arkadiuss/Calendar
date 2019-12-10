package app.presenter.week;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class WeekViewDayPresenter {
    @FXML
    private VBox hoursPane;

    @FXML
    public void initialize(){
        for (int i=0; i < 24; i++){
            ViewUtils.LoadedView<Object> n = ViewUtils.loadView("week/WeekViewHour.fxml");
            Label label = (Label)n.view.lookup("#hourView");
            label.setText(i+":00");
            hoursPane.getChildren().add(n.view);
        }
    }
}
