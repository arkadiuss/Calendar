package app.presenter.week;

import app.AppContext;
import app.di.DIProvider;
import app.presenter.AbstractDayView;
import app.util.ViewUtils;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.BehaviorSubject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import logic.service.CalendarService;
import org.apache.commons.math3.util.Pair;

import java.time.LocalDate;
import java.util.stream.Collectors;


public class WeekViewDayPresenter extends AbstractDayView {
    public AnchorPane dayPane;
    private AppContext appContext;
    private static final double DAY_PX_HEIGHT = 48.5;
    private static final double HEADER_PX_HEIGHT = 75.0;
    private static final double OFFSET_PX_HEIGHT = 95.0;
    private static final double DAY_PX_WIDTH = 75.0;
    private final CalendarService calendarService;

    @FXML
    private VBox hoursPane;

    @FXML
    private AnchorPane eventsPane;

    @FXML
    private Label dayOfWeek;

    private BehaviorSubject<LocalDate> date = BehaviorSubject.create();


    public WeekViewDayPresenter() {
        this.appContext = DIProvider.getAppContext();
        this.calendarService = DIProvider.getCalendarService();
    }

    @FXML
    public void initialize() {
        for (int i = 0; i < 25; i++) {
            if (i == 0) {
                ViewUtils.LoadedView<Node, WeekViewDayPresenter> n = ViewUtils.loadView("week/WeekViewHour.fxml");
                dayOfWeek = (Label) n.view.lookup("#hourView");
                dayOfWeek.setPrefHeight(HEADER_PX_HEIGHT);
                hoursPane.getChildren().add(n.view);
                continue;
            }
            ViewUtils.LoadedView<Node, WeekViewDayPresenter> n = ViewUtils.loadView("week/WeekViewHour.fxml");
            Label label = (Label) n.view.lookup("#hourView");
            label.setText((i - 1) + ":00");
            hoursPane.getChildren().add(n.view);
        }

        date.subscribe(d -> {
            dayOfWeek.setText(days[d.getDayOfWeek().getValue() - 1]);
        }, err -> {});

        Observable.combineLatest(
                date,
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

    public void setDate(LocalDate d) {
        this.date.onNext(d);
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
    protected double getHeaderOffset() {
        return OFFSET_PX_HEIGHT;
    }

    @Override
    protected double getHourWidth() {
        return DAY_PX_WIDTH;
    }
}
