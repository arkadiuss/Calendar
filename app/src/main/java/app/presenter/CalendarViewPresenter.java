package app.presenter;

import app.AppContext;
import app.di.DIProvider;
import app.presenter.day.DayViewPresenter;
import app.presenter.week.WeekViewPresenter;
import app.util.AlertPopup;
import app.util.ViewUtils;
import app.view.month.MonthView;
import com.google.common.base.Strings;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.Place;
import logic.model.User;
import logic.service.CalendarService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CalendarViewPresenter {

    @FXML
    private HBox root;
    @FXML
    private Tab dayViewTab;
    @FXML
    private Tab weekViewTab;
    @FXML
    private Tab monthViewTab;
    @FXML
    private DatePicker datePicker;
    @FXML
    private LeftMenuCalendarPresenter addCalendarViewController;
    @FXML
    private Label welcomeLabel;

    private AppContext appContext;

    @FXML
    private RightAddEventMenuPresenter addEventViewController;

    private List<Calendar> selectedCalendars = new ArrayList<>();

    private CalendarService calendarService = new CalendarService();


    public CalendarViewPresenter() {
        appContext = DIProvider.getAppContaxt();
        appContext.setSelectedDate(LocalDate.now());
    }


    public void handleDatePickerChange(ActionEvent actionEvent) {
        appContext.setSelectedDate(datePicker.getValue());
    }

    public void handleSetCurrentDateButton() {
    }

    @FXML
    public void initialize() {
        addCalendarViewController.setOnNewCalendarAdded((calendar -> {
            addEventViewController.getCalendarsCombobox().getItems().add(calendar);
        }));

        addCalendarViewController.setOnRemoveCalendar(calendar -> {
            selectedCalendars.remove(calendar);
            addEventViewController.getCalendarsCombobox().getItems().remove(calendar);
            updateView();
        });
        datePicker.setValue(LocalDate.now());
        updateView();
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

    private void updateView() {
        setDayViewContent();
        setWeekViewContent();
        setMonthViewContent();
    }

    private List<Event> getEventsFromSelectedCalendars() {
        return selectedCalendars.stream().flatMap(c -> c.getEvents().stream()).collect(Collectors.toList());
    }

    public void setCurrentUser(User currentUser) {
        this.appContext.setCurrentUser(currentUser);
        welcomeLabel.setText(String.format("Welcome, %s", currentUser.getUsername()));
        addEventViewController.getCalendarsCombobox().getItems().addAll(currentUser.getCalendars());
    }

}
