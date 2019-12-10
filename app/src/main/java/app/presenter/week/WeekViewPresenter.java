package app.presenter.week;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class WeekViewPresenter {

    @FXML
    private HBox weekPane;

    @FXML
    public void initialize(){
        for(int i=0; i<7; i++) {
            Node n = ViewUtils.loadView("week/WeekViewDay.fxml").view;
            n.prefWidth(weekPane.getWidth()/7.0);
            n.prefHeight(weekPane.getHeight());
            weekPane.getChildren().add(n);
        }
    }
}
