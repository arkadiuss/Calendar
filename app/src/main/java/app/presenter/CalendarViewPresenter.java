package app.presenter;

import app.view.month.MonthView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;

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

    private LocalDate selectedDate;

    public void setSelectedDate(LocalDate selectedDate){
        this.selectedDate = selectedDate;
        datePicker.setValue(selectedDate);
        setMonthViewContent();
    }

    public void handleDatePickerChange(ActionEvent actionEvent){
        selectedDate = datePicker.getValue();
        setMonthViewContent();
    }

    public void handleSetCurrentDateButton(ActionEvent actionEvent){
        selectedDate = LocalDate.now();
        datePicker.setValue(selectedDate);
    }

    public void setMonthViewContent(){
        YearMonth yearMonth = YearMonth.of(selectedDate.getYear(), selectedDate.getMonth());
        MonthView monthViewContent = new MonthView(this, yearMonth);
        VBox monthTabContent = monthViewContent.getMonthViewVBox();
        monthViewTab.setContent(monthTabContent);
    }

}
