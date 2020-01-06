package app.presenter;

import com.google.common.base.Strings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    @FXML
    public CheckBox allDayCheckbox;
    @FXML
    public Label startHourLabel;
    @FXML
    public Label endHourLabel;


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


    public void handleAllDayCheckboxChange(ActionEvent event) {
        if(this.allDayCheckbox.isSelected()){
            this.spinnerStartHour.getValueFactory().setValue(0);
            this.spinnerStartMinute.getValueFactory().setValue(1);
            this.spinnerEndHour.getValueFactory().setValue(23);
            this.spinnerEndMinute.getValueFactory().setValue(59);

            this.spinnerStartHour.setVisible(false);
            this.spinnerStartMinute.setVisible(false);
            this.spinnerEndHour.setVisible(false);
            this.spinnerEndMinute.setVisible(false);
            this.startHourLabel.setVisible(false);
            this.endHourLabel.setVisible(false);
        }else{
            this.spinnerStartHour.setVisible(true);
            this.spinnerStartMinute.setVisible(true);
            this.spinnerEndHour.setVisible(true);
            this.spinnerEndMinute.setVisible(true);
            this.startHourLabel.setVisible(true);
            this.endHourLabel.setVisible(true);
        }
    }
}
