package app.presenter.day;

import app.AppContext;
import app.di.DIProvider;
import app.presenter.AbstractDayView;
import app.util.ViewUtils;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import logic.model.Calendar;
import logic.model.Event;
import logic.service.CalendarService;
import org.apache.commons.math3.util.Pair;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DayViewPresenter extends AbstractDayView {
    private static final double DAY_PX_HEIGHT = 52.0;
    private static final double DAY_PX_WIDTH = 490.0;
    private final AppContext appContext;
    private final CalendarService calendarService;

    @FXML
    private AnchorPane eventsPane;

    @FXML
    private VBox hoursPane;

    public DayViewPresenter() {
        this.appContext = DIProvider.getAppContext();
        this.calendarService = DIProvider.getCalendarService();
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
                appContext.observeUser().flatMap(calendarService::getCalendars),
                appContext.observeSelectedCalendars(),
                (localDate, calendars, selectedCalendars) -> new Pair<>(
                        localDate,
                        calendars.stream()
                                .filter(c -> selectedCalendars.contains(c.getId()))
                                .flatMap(c -> c.getEvents().stream())
                                .collect(Collectors.toList())))
                .observeOn(JavaFxScheduler.platform())
                .subscribe((pair) -> {
                    applyEvents(appContext, eventsPane, pair.getFirst(), pair.getSecond());
                });
    }

    @Override
    protected double getHourHeight() {
        return DAY_PX_HEIGHT;
    }

    @Override
    protected double getEventOffset() {
        return 60;
    }

    @Override
    protected double getHourWidth() {
        return DAY_PX_WIDTH;
    }
}
