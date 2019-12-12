package app.presenter;

import app.presenter.day.DayViewPresenter;
import app.presenter.week.WeekViewPresenter;
import app.util.AlertPopup;
import app.util.ViewUtils;
import app.view.calendar_list.CalendarListViewCell;
import app.view.month.MonthView;
import com.google.common.base.Strings;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.Arrays;
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
    private Button setCurrentDateButton;
    @FXML
    private ListView calendarsList;
    @FXML
    private TextField newCalendarName;
    @FXML
    private Button addCalendarButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox leftVbox;

    private User currentUser;

    private LocalDate selectedDate;

    @FXML
    private RightAddEventMenuPresenter addEventViewController;

    private List<Calendar> selectedCalendars = new ArrayList<>();

    private CalendarService calendarService = new CalendarService();

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        datePicker.setValue(selectedDate);
        updateView();
    }

    public void handleDatePickerChange(ActionEvent actionEvent) {
        selectedDate = datePicker.getValue();
        updateView();
    }

    public void handleSetCurrentDateButton(ActionEvent actionEvent) {
        selectedDate = LocalDate.now();
        datePicker.setValue(selectedDate);
    }

    @FXML
    public void initialize() {
        this.calendarsList.setCellFactory(listView ->
                new CalendarListViewCell((calendar, deleteButton) -> {
                    deleteButton.setDisable(true);
                    deleteButton.setText("Deleting...");
                    currentUser.getCalendars().remove(calendar);
                    selectedCalendars.remove(calendar);
                    calendarService.deleteCalendar(calendar)
                            .observeOn(JavaFxScheduler.platform())
                            .subscribe(() -> {
                                this.calendarsList.getItems().remove(calendar);
                                this.addEventViewController.getCalendarsCombobox().getItems().remove(calendar);
                                updateView();
                            }, error -> {
                                deleteButton.setDisable(false);
                                deleteButton.setText("Remove");
                                System.out.println(error.toString());
                            });
                }, (calendar, isSelected) -> {
                    if(isSelected)
                        selectedCalendars.add(calendar);
                    else
                        selectedCalendars.remove(calendar);
                    updateView();
                }));
    }

    public void handleAddNewCalendar(ActionEvent actionEvent) {
        if (Strings.isNullOrEmpty(newCalendarName.getText())) {
            AlertPopup.showAlert("Calendar name cannot be empty");
            return;
        }
        Calendar calendar = new Calendar(newCalendarName.getText(), currentUser);
//        calendar.addEvent(new Event("XMAS", new Place("EVERYWHERE", "EVERYWHERE"), new LocalDateTime(2019, 12, 24)));
//        calendar.addEvent(new Event("event1", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 6)));
//        calendar.addEvent(new Event("event2", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 5)));
//        calendar.addEvent(new Event("event3", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 7)));
//        calendar.addEvent(new Event("event4", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 7)));
        addCalendarButton.setText("Adding...");
        addCalendarButton.setDisable(true);
        calendarService.addCalendar(calendar)
                .andThen(calendarService.getCalendars())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(calendars -> {
                    calendarsList.getItems().add(calendar);
                    addEventViewController.getCalendarsCombobox().getItems().add(calendar);
                    addCalendarButton.setText("Add");
                    addCalendarButton.setDisable(false);
                }, error -> {
                    System.out.println(error);
                    addCalendarButton.setText("Add");
                    addCalendarButton.setDisable(false);
//todo: print error about unadded calendar
                });
    }

    public void setMonthViewContent() {
        YearMonth yearMonth = YearMonth.of(selectedDate.getYear(), selectedDate.getMonth());
        MonthView monthViewContent = new MonthView(this, yearMonth);
        VBox monthTabContent = monthViewContent.getMonthViewVBox();
        monthViewTab.setContent(monthTabContent);
    }

    private void setDayViewContent() {
        ViewUtils.LoadedView lw = ViewUtils.loadView("day/DayView.fxml");
        ((DayViewPresenter) lw.controller).setEvents(getEventsFromSelectedCalendars());
        ((DayViewPresenter) lw.controller).setSelectedDate(selectedDate);
        dayViewTab.setContent(lw.view);
    }


    public void setWeekViewContent() {

        ViewUtils.LoadedView loadedView = ViewUtils.loadView("week/WeekView.fxml");
        ((WeekViewPresenter) loadedView.controller).setEvents(getEventsFromSelectedCalendars());
        ((WeekViewPresenter) loadedView.controller).setCurrentDate(selectedDate);
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
        this.currentUser = currentUser;
        currentUser.getCalendars().forEach((calendar -> this.calendarsList.getItems().add(calendar)));
        welcomeLabel.setText(String.format("Welcome, %s", currentUser.getUsername()));
        addEventViewController.getCalendarsCombobox().getItems().addAll(currentUser.getCalendars());
    }

}
