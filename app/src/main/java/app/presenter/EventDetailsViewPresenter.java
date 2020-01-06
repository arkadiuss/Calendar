package app.presenter;

import app.di.DIProvider;
import app.util.AlertPopup;
import com.google.common.base.Strings;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logic.model.Calendar;
import logic.model.Event;
import logic.model.Place;
import logic.service.CalendarService;
import logic.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EventDetailsViewPresenter extends AbstractEventViewPresenter {

    @FXML
    public TextArea descriptionArea;
    @FXML
    public Button saveButton;
    @FXML
    public Button deleteButton;
    private Stage dialogStage;
    private Event event;

    private Consumer<Boolean> eventSaved;

    private EventService eventService = new EventService();

    private CalendarService calendarService;

    public EventDetailsViewPresenter() {
        calendarService = DIProvider.getCalendarService();
    }


    public void setEvent(Event event) {
        this.event = event;
        fillWithEventData();
    }

    public void setEventSaved(Consumer<Boolean> eventSaved) {
        this.eventSaved = eventSaved;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void fillWithEventData() {
//        calendarNameLabel.setText(calendar.getName());
        eventNameField.setText(event.getTitle());
        placeNameField.setText(event.getPlace().getName());
        addressNameField.setText(event.getPlace().getAddress());

        LocalDateTime startDateTime = event.getStartDateTime();
        LocalDateTime endDateTime = event.getEndDateTime();

        startDateField.setValue(startDateTime.toLocalDate());
        endDateField.setValue(endDateTime.toLocalDate());

        spinnerStartHour.getValueFactory().setValue(startDateTime.getHour());
        spinnerStartMinute.getValueFactory().setValue(startDateTime.getMinute());
        spinnerEndHour.getValueFactory().setValue(endDateTime.getHour());
        spinnerEndMinute.getValueFactory().setValue(endDateTime.getMinute());
        descriptionArea.setText(event.getDescription());

        allDayCheckbox.setSelected(event.isAllDay());
        handleAllDayCheckboxChange(new ActionEvent());
    }


    public void setEventData() {
        event.setTitle(eventNameField.getText());
        Place place = new Place(placeNameField.getText(), addressNameField.getText());
        event.setPlace(place);
        event.setStartDateTime(super.getStartDateTime());
        event.setEndDateTime(super.getEndDateTime());
        event.setDescription(descriptionArea.getText());
        event.setAllDay(allDayCheckbox.isSelected());

    }

    public void handleSaveEvent(ActionEvent event) {
        if (super.areFieldsEmpty()) {
            AlertPopup.showAlert("Event properties cannot be empty");
        } else {
            if (super.getStartDateTime().compareTo(super.getEndDateTime()) >= 0) {
                AlertPopup.showAlert("End time and date must be later than start time and date");
            } else {
                setEventData();
                saveButton.setText("Saving event...");
                saveButton.setDisable(true);

                //todo update views
                Calendar calendar = this.event.getCalendar();
                eventService.updateEvent(this.event).andThen(calendarService.updateCalendar(calendar))
                        .observeOn(JavaFxScheduler.platform())
                        .subscribe(() -> {
                            saveButton.setText("Save");
                            saveButton.setDisable(false);
                            eventSaved.accept(true);
                        }, error -> {
                            saveButton.setText("Save");
                            saveButton.setDisable(false);
                        });
            }
        }
    }

    public void handleDeleteEvent(ActionEvent event) {

        deleteButton.setText("Deleting event...");
        deleteButton.setDisable(true);

        Calendar calendar = this.event.getCalendar();
        calendar.removeEvent(this.event);

        eventService.deleteEvent(this.event).andThen(calendarService.updateCalendar(calendar)).observeOn(JavaFxScheduler.platform())
        .subscribe(() -> {
                    deleteButton.setText("Remove");
                    deleteButton.setDisable(false);
                    this.dialogStage.close();

                },
                error -> {
                    deleteButton.setText("Remove");
                    deleteButton.setDisable(false);
                    AlertPopup.showAlert("Error occurred while deleting event");
                    System.out.println(error.getMessage());
                    calendar.addEvent(this.event);
                });

    }

}
