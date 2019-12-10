package app.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.model.Calendar;
import logic.model.Event;

import java.time.LocalDateTime;

public class EventDetailsViewPresenter {

    @FXML
    public TextField eventNameField;
    @FXML
    public TextField placeNameField;
    @FXML
    public TextField addressField;
    @FXML
    public DatePicker startDateField;
    @FXML
    public DatePicker endDateField;
    @FXML
    public Spinner<Integer> spinnerStartHour;
    @FXML
    public Spinner<Integer> spinnerStartMinute;
    @FXML
    public Spinner<Integer> spinnerEndHour;
    @FXML
    public Spinner<Integer> spinnerEndMinute;
    private Stage dialogStage;
    private Calendar calendar;
    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        fillWithEventData();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void fillWithEventData() {
//        calendarNameLabel.setText(calendar.getName());
        eventNameField.setText(event.getTitle());
        placeNameField.setText(event.getPlace().getName());
        addressField.setText(event.getPlace().getAddress());

        LocalDateTime startDateTime = event.getStartDateTime();
        LocalDateTime endDateTime = event.getEndDateTime();

        startDateField.setValue(startDateTime.toLocalDate());
        endDateField.setValue(endDateTime.toLocalDate());

        spinnerStartHour.getValueFactory().setValue(startDateTime.getHour());
        spinnerStartMinute.getValueFactory().setValue(startDateTime.getMinute());
        spinnerEndHour.getValueFactory().setValue(endDateTime.getHour());
        spinnerEndMinute.getValueFactory().setValue(endDateTime.getMinute());
    }

}
