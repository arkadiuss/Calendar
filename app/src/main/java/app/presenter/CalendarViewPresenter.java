package app.presenter;

import app.AppContext;
import app.di.DIProvider;
import app.presenter.day.DayViewPresenter;
import app.presenter.week.WeekViewPresenter;
import app.util.ViewUtils;
import app.view.month.MonthView;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.observers.JavaFxObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.model.Calendar;
import logic.model.User;
import logic.service.CalendarService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarViewPresenter {

    @FXML
    private Tab dayViewTab;
    @FXML
    private Tab weekViewTab;
    @FXML
    private Tab monthViewTab;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label welcomeLabel;

    private AppContext appContext;

    public CalendarViewPresenter() {
        appContext = DIProvider.getAppContext();
    }

    @FXML
    public void initialize() {
        JavaFxObservable.valuesOf(datePicker.valueProperty())
                .subscribe(d -> appContext.setSelectedDate(d));

        appContext.observeSelectedDate()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(d -> datePicker.setValue(d));
        appContext.setSelectedDate(LocalDate.now());

        appContext.observeUser()
                .subscribe(user -> {
                    welcomeLabel.setText(String.format("Welcome, %s", user.getUsername()));
                });

        setDayViewContent();
        setWeekViewContent();
        setMonthViewContent();
    }

    public void handleSetCurrentDateButton() {
        datePicker.setValue(LocalDate.now());
    }

    public void setMonthViewContent() {
        MonthView monthViewContent = new MonthView();
        VBox monthTabContent = monthViewContent.getMonthViewVBox();
        monthViewTab.setContent(monthTabContent);
    }

    private void setDayViewContent() {
        ViewUtils.LoadedView<Node, DayViewPresenter> lw = ViewUtils.loadView("day/DayView.fxml");
        dayViewTab.setContent(lw.view);
    }


    public void setWeekViewContent() {
        ViewUtils.LoadedView<Node, WeekViewPresenter> loadedView = ViewUtils.loadView("week/WeekView.fxml");
        weekViewTab.setContent(loadedView.view);
    }

    public void setCurrentUser(User currentUser) {
        this.appContext.setCurrentUser(currentUser);
    }
}
