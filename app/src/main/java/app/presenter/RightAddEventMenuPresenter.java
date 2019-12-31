package app.presenter;

import app.AppContext;
import app.di.DIProvider;
import app.util.AlertPopup;
import io.reactivex.rxjavafx.observers.JavaFxObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.Place;
import logic.service.CalendarService;

import java.util.List;


public class RightAddEventMenuPresenter extends AbstractEventViewPresenter{
    @FXML
    private Button addEventButton;
    @FXML
    private ComboBox<Calendar> calendarsCombobox;

    private AppContext appContext;
    private CalendarService calendarService;

    public RightAddEventMenuPresenter() {
        appContext = DIProvider.getAppContext();
        calendarService = DIProvider.getCalendarService();
    }

    @FXML
    public void initialize() {
        appContext.observeUser()
                .flatMap(calendarService::getCalendars)
                .observeOn(JavaFxScheduler.platform())
                .subscribe(calendars -> {
                    calendarsCombobox.getItems().clear();
                    calendarsCombobox.getItems().addAll(calendars);
                }, err -> {

                });
    }

    public void handleAddEvent(ActionEvent event) {
        if (super.areFieldsEmpty() || calendarsCombobox.getValue() == null) {
            AlertPopup.showAlert("Event properties cannot be empty");
        } else if(!startDateField.getValue().isEqual(endDateField.getValue())) {
            AlertPopup.showAlert("Currently only one-day events are supported :(");
        }else {
            Calendar calendar = (Calendar) calendarsCombobox.getValue();

            if (super.getStartDateTime().compareTo(super.getEndDateTime()) >= 0) {
                AlertPopup.showAlert("End time and date must be later than start time and date");
            } else {

                addEventButton.setText("Adding event...");
                addEventButton.setDisable(true);
                Event newEvent = new Event(eventNameField.getText(),
                        new Place(
                                placeNameField.getText(),
                                addressNameField.getText()),
                        super.getStartDateTime(), super.getEndDateTime());
                calendar.addEvent(newEvent);

                //todo UPDATE VIEW
                calendarService.updateCalendar(calendar).observeOn(JavaFxScheduler.platform())
                        .subscribe(() -> {
                            addEventButton.setText("Add event");
                            addEventButton.setDisable(false);
                            //updateView();
                        }, error -> {
                            addEventButton.setText("Add event");
                            addEventButton.setDisable(false);
                        });
            }
        }
    }


}
