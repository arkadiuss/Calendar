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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CalendarViewPresenter {
    @FXML
    public Spinner spinnerStartHour;
    @FXML
    public Spinner spinnerStartMinute;
    @FXML
    public Spinner spinnerEndHour;
    @FXML
    public Spinner spinnerEndMinute;
    @FXML
    private DatePicker eventStartDatePicker;
    @FXML
    private DatePicker eventEndDatePicker;
    @FXML
    private Button addEventButton;
    @FXML
    private ComboBox calendarsCombobox;
    @FXML
    private TextField eventNameField;
    @FXML
    private TextField placeNameField;
    @FXML
    private TextField addressNameField;
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
    private Button setCurrentDateButton;
    @FXML
    private VBox addCalendarView;
    @FXML
    private LeftMenuCalendarPresenter addCalendarViewController;
    @FXML
    private Label welcomeLabel;

    private AppContext appContext;

    private User currentUser;

    private LocalDate selectedDate;

    private List<Calendar> selectedCalendars = new ArrayList<>();

    private CalendarService calendarService = new CalendarService();

    public CalendarViewPresenter() {
        appContext = DIProvider.getAppContaxt();
        appContext.setSelectedDate(LocalDate.now());
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        datePicker.setValue(selectedDate);
        updateView();
    }

    public void handleDatePickerChange(ActionEvent actionEvent) {
        selectedDate = datePicker.getValue();
        updateView();
    }

    public void handleSetCurrentDateButton() {
    }

    @FXML
    public void initialize() {
        addCalendarViewController.setOnNewCalendarAdded((calendar -> {
            calendarsCombobox.getItems().add(calendar);
        }));
        addCalendarViewController.setOnSelectCalendar((calendar, isSelected) -> {
            if (isSelected) {
                selectedCalendars.add(calendar);
            } else {
                selectedCalendars.remove(calendar);
            }
            updateView();
        });

        addCalendarViewController.setOnRemoveCalendar(calendar -> {
            selectedCalendars.remove(calendar);
            calendarsCombobox.getItems().remove(calendar);
            updateView();
        });
    }

    public void setMonthViewContent() {
        YearMonth yearMonth = YearMonth.of(selectedDate.getYear(), selectedDate.getMonth());
        MonthView monthViewContent = new MonthView(this, yearMonth);
        VBox monthTabContent = monthViewContent.getMonthViewVBox();
        monthViewTab.setContent(monthTabContent);
    }

    private void setDayViewContent() {
        ViewUtils.LoadedView<Node, DayViewPresenter> lw = ViewUtils.loadView("day/DayView.fxml");
        lw.controller.setEvents(getEventsFromSelectedCalendars());
        lw.controller.setSelectedDate(selectedDate);
        dayViewTab.setContent(lw.view);
    }


    public void setWeekViewContent() {

        ViewUtils.LoadedView<Node, WeekViewPresenter> loadedView = ViewUtils.loadView("week/WeekView.fxml");
        loadedView.controller.setEvents(getEventsFromSelectedCalendars());
        loadedView.controller.setCurrentDate(selectedDate);
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
        calendarsCombobox.getItems().addAll(currentUser.getCalendars());
    }

    public void handleAddEvent(ActionEvent event) {
        if (Strings.isNullOrEmpty(eventNameField.getText()) || Strings.isNullOrEmpty(addressNameField.getText())
                || Strings.isNullOrEmpty(placeNameField.getText()) || calendarsCombobox.getValue() == null
                || eventStartDatePicker.getValue() == null || eventEndDatePicker.getValue() == null ||
                spinnerStartHour.getValue() == null || spinnerEndMinute.getValue() == null) {
            AlertPopup.showAlert("Event properties cannot be empty");
        } else if (!eventStartDatePicker.getValue().isEqual(eventEndDatePicker.getValue())) {
            AlertPopup.showAlert("Currently only one-day events are supported :(");
        } else {
            Calendar calendar = (Calendar) calendarsCombobox.getValue();

            LocalDate startDate = eventStartDatePicker.getValue();
            LocalDate endDate = eventEndDatePicker.getValue();

            LocalTime startTime = LocalTime.of((Integer) spinnerStartHour.getValue(),
                    (Integer) spinnerStartMinute.getValue());
            LocalTime endTime = LocalTime.of((Integer) spinnerEndHour.getValue(),
                    (Integer) spinnerEndMinute.getValue());

            LocalDateTime startDateTime = LocalDateTime.of(startDate.getYear(), startDate.getMonth(),
                    startDate.getDayOfMonth(), startTime.getHour(), startTime.getMinute());
            LocalDateTime endDateTime = LocalDateTime.of(endDate.getYear(), endDate.getMonth(),
                    endDate.getDayOfMonth(), endTime.getHour(), endTime.getMinute());

            if (startDateTime.compareTo(endDateTime) >= 0) {
                AlertPopup.showAlert("End time and date must be later than start time and date");
            } else {

                addEventButton.setText("Adding event...");
                addEventButton.setDisable(true);
                Event newEvent = new Event(eventNameField.getText(),
                        new Place(
                                placeNameField.getText(),
                                addressNameField.getText()),
                        startDateTime, endDateTime);
                calendar.addEvent(newEvent);

                calendarService.updateCalendar(calendar).observeOn(JavaFxScheduler.platform())
                        .subscribe(() -> {
                            addEventButton.setText("Add event");
                            addEventButton.setDisable(false);
                            updateView();
                        }, error -> {
                            addEventButton.setText("Add event");
                            addEventButton.setDisable(false);
                        });
            }
        }
    }
}
