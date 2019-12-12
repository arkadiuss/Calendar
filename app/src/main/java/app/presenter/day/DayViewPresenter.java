package app.presenter.day;

import app.AppContext;
import app.di.DIProvider;
import app.presenter.AbstractDayView;
import app.util.ViewUtils;
import io.reactivex.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.commons.math3.util.Pair;

public class DayViewPresenter extends AbstractDayView {
    private static final double DAY_PX_HEIGHT = 52.0;
    private static final double DAY_PX_WIDTH = 490.0;
    private final AppContext appContext;

    @FXML
    private AnchorPane dayPane;

    @FXML
    private VBox hoursPane;

    public DayViewPresenter() {
        this.appContext = DIProvider.getAppContaxt();
    }

    @FXML
    public void initialize(){
        for (int i=0; i < 24; i++){
            ViewUtils.LoadedView<Node, DayViewPresenter> n = ViewUtils.loadView("day/DayViewHour.fxml");
            Label label = (Label)n.view.lookup("#hourView");
            label.setText(i+":00");
            hoursPane.getChildren().add(n.view);
        }

        Observable.combineLatest(
                appContext.observeSelectedDate(),
                appContext.observeEvents(),
                Pair::new
        ).subscribe((pair) -> {
            applyEvents(dayPane, pair.getFirst(), pair.getSecond(), DAY_PX_WIDTH, DAY_PX_HEIGHT, 60);
        });
    }

}
