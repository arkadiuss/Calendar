package app.presenter.week;

import app.AppContext;
import app.di.DIProvider;
import app.presenter.AbstractDayView;
import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;


public class WeekViewDayPresenter extends AbstractDayView {
    public AnchorPane dayPane;
    private AppContext appContext;
    private static final double DAY_PX_HEIGHT = 56.0;
    private static final double DAY_PX_WIDTH = 75.0;

    @FXML
    private VBox hoursPane;

    private Label dayOfWeek;

    private String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public WeekViewDayPresenter() {
        this.appContext = DIProvider.getAppContext();
    }

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
        dayOfWeek.setText(days[date.getDayOfWeek().getValue() - 1]);
        this.appContext.observeEvents().subscribe((events) -> {
            applyEvents(dayPane, date, events);
        });

    }

    @Override
    protected double getHourHeight() {
        return DAY_PX_HEIGHT;
    }

    @Override
    protected double getEventOffset() {
        return 0;
    }

    @Override
    protected double getHourWidth() {
        return DAY_PX_WIDTH;
    }
}
