package app.presenter;

import app.util.AlertPopup;
import app.view.month.MonthView;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import logic.model.Calendar;
import logic.model.User;
import logic.service.CalendarService;

import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarViewPresenter {
    @FXML
    public HBox root;
    @FXML
    public Tab dayViewTab;
    @FXML
    public Tab weekViewTab;
    @FXML
    public Tab monthViewTab;
    @FXML
    public DatePicker datePicker;
    @FXML
    public Button setCurrentDateButton;
    @FXML
    public ListView calendarsList;
    @FXML
    public TextField newCalendarName;
    @FXML
    public Button addCalendarButton;
    public Label welcomeLabel;

    private User currentUser;

    private LocalDate selectedDate;

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

    public void handleAddNewCalendar(ActionEvent actionEvent) {
        if (Strings.isNullOrEmpty(newCalendarName.getText())) {
            AlertPopup.showAlert("Calendar name cannot be empty");
            return;
        }
        Calendar calendar = new Calendar(newCalendarName.getText(), currentUser);
        CalendarService calendarService = new CalendarService();
        calendarService.addCalendar(calendar);
        calendarsList.getItems().add(calendar);

    }

    public void setMonthViewContent() {
        YearMonth yearMonth = YearMonth.of(selectedDate.getYear(), selectedDate.getMonth());
        MonthView monthViewContent = new MonthView(this, yearMonth);
        VBox monthTabContent = monthViewContent.getMonthViewVBox();
        monthViewTab.setContent(monthTabContent);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        welcomeLabel.setText(String.format("Welcome, %s", currentUser.getUsername()));
    }
}
