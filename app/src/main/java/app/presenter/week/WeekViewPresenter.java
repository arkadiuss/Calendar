package app.presenter.week;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class WeekViewPresenter {

    @FXML
    private HBox weekPane;
    private LocalDate selectedDate;


    @FXML
    public void initialize() {
        for (int i = 0; i < 7; i++) {
            Node n = ViewUtils.loadView("week/WeekViewDay.fxml").view;
            n.prefWidth(weekPane.getWidth() / 7.0);
            n.prefHeight(weekPane.getHeight());
            weekPane.getChildren().add(n);
        }
    }

    public void setCurrentDate(LocalDate selectedDate) {
        Node node = weekPane.getChildren().get(selectedDate.getDayOfWeek().getValue() - 1);
        node.setStyle("-fx-background-color: #FFCFFF;");
    }
}
