package app.presenter;

import app.AppContext;
import app.di.DIProvider;
import app.util.AlertPopup;
import app.util.ViewUtils;
import com.google.common.base.Strings;
import io.reactivex.Observable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.model.Event;
import logic.service.CalendarService;

import java.util.List;
import java.util.stream.Collectors;

public class SearchEnginePresenter {

    @FXML
    private TextField inputField;
    @FXML
    private Button searchButton;

    private AppContext appContext;
    private CalendarService calendarService;

    public SearchEnginePresenter() {
        this.appContext = DIProvider.getAppContext();
        this.calendarService = DIProvider.getCalendarService();
    }

    public void handleSearchEvent(){
        if (Strings.isNullOrEmpty(inputField.getText())) {
            AlertPopup.showAlert("Empty input");
            return;
        }

        ViewUtils.LoadedView<Node, EventsListPresenter> view = ViewUtils.loadView("events/EventsListView.fxml");

        Observable<List<Event>> events = appContext.observeUser().flatMap(calendarService::getCalendars)
                .map(calendars -> calendars.stream()
                        .flatMap(c -> c.getEvents().stream().filter(event -> event.contains(inputField.getText())))
                        .collect(Collectors.toList()));

        events.subscribe(view.controller::addEvents);

        Stage stage = new Stage();
        stage.setTitle("Events list");
        stage.setScene(new Scene((Parent) view.view, 600, 450));
        stage.show();
    }

}
