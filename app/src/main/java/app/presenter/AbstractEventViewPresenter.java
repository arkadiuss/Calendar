package app.presenter;

import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class AbstractEventViewPresenter {
    @FXML
    public TextField eventNameField;
    @FXML
    public TextField placeNameField;
    @FXML
    public TextField addressNameField;
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


    protected boolean areFieldsEmpty() {
        return (Strings.isNullOrEmpty(eventNameField.getText()) || Strings.isNullOrEmpty(addressNameField.getText())
                || Strings.isNullOrEmpty(placeNameField.getText())
                || startDateField.getValue() == null || endDateField.getValue() == null);
    }

    protected LocalDateTime getStartDateTime() {
        LocalDate startDate = startDateField.getValue();
        LocalTime startTime = LocalTime.of(spinnerStartHour.getValue(),
                spinnerStartMinute.getValue());
        return LocalDateTime.of(startDate.getYear(), startDate.getMonth(),
                startDate.getDayOfMonth(), startTime.getHour(), startTime.getMinute());
    }

    protected LocalDateTime getEndDateTime() {
        LocalDate endDate = endDateField.getValue();
        LocalTime endTime = LocalTime.of(spinnerEndHour.getValue(),
                spinnerEndMinute.getValue());
        return LocalDateTime.of(endDate.getYear(), endDate.getMonth(),
                endDate.getDayOfMonth(), endTime.getHour(), endTime.getMinute());
    }
}
