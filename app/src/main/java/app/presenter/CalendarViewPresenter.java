package app.presenter;

import app.exceptions.UserAlreadyExistException;
import app.util.AlertPopup;
import app.view.calendar_list.CalendarListViewCell;
import app.view.month.MonthView;
import com.google.common.base.Strings;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.Place;
import logic.model.User;
import logic.dao.CalendarDao;
import logic.service.CalendarService;
import logic.service.UserService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;


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

    private CalendarService calendarService = new CalendarService();

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        datePicker.setValue(selectedDate);
        setMonthViewContent();
    }

    public void handleDatePickerChange(ActionEvent actionEvent) {
        selectedDate = datePicker.getValue();
        setMonthViewContent();
    }

    public void handleSetCurrentDateButton(ActionEvent actionEvent) {
        selectedDate = LocalDate.now();
        datePicker.setValue(selectedDate);
    }

    @FXML
    public void initialize() {
        //todo: make it working, bo nie działa xD
        this.calendarsList.setCellFactory(listView ->
                new CalendarListViewCell((calendar, deleteButton) -> {
                    deleteButton.setDisable(true);
                    deleteButton.setText("Deleting...");
                    calendarService.deleteCalendar(calendar)
                            .observeOn(JavaFxScheduler.platform())
                            .subscribe(() -> {
                                this.calendarsList.getItems().remove(calendar);
                            }, error -> {
                                deleteButton.setDisable(false);
                                deleteButton.setText("Remove");
                                System.out.println(error.toString());
                            });
                }));
    }

    public void handleAddNewCalendar(ActionEvent actionEvent) {
        if (Strings.isNullOrEmpty(newCalendarName.getText())) {
            AlertPopup.showAlert("Calendar name cannot be empty");
            return;
        }
        Calendar calendar = new Calendar(newCalendarName.getText(), currentUser);
        calendar.addEvent(new Event("XMAS", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 24)));
        calendar.addEvent(new Event("event1", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 6)));
        calendar.addEvent(new Event("event2", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 5)));
        calendar.addEvent(new Event("event3", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 7)));
        calendar.addEvent(new Event("event4", new Place("EVERYWHERE", "EVERYWHERE"), new Date(2019, 12, 7)));
        addCalendarButton.setText("Adding...");
        addCalendarButton.setDisable(true);
        calendarService.addCalendar(calendar)
                .andThen(calendarService.getCalendars())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(calendars -> {
                    calendarsList.getItems().add(calendar);
                    addCalendarButton.setText("Add");
                    addCalendarButton.setDisable(false);
                }, error -> {
//todo: print error about unadded calendar
                });
    }

    public void setMonthViewContent() {
        YearMonth yearMonth = YearMonth.of(selectedDate.getYear(), selectedDate.getMonth());
        MonthView monthViewContent = new MonthView(this, yearMonth);
        VBox monthTabContent = monthViewContent.getMonthViewVBox();
        monthViewTab.setContent(monthTabContent);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        currentUser.getCalendars().forEach((calendar -> this.calendarsList.getItems().add(calendar)));
        welcomeLabel.setText(String.format("Welcome, %s", currentUser.getUsername()));
    }
}
