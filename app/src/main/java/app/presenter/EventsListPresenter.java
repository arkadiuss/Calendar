package app.presenter;

import app.util.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import logic.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsListPresenter {

    @FXML
    private ListView<Event> eventsList;

    private List<Event> events = new ArrayList<>();

    public void addEvents(List<Event> events){
        events.addAll(events);
        events.forEach(e -> {
            eventsList.getItems().add(e);
        });
    }

    @FXML
    public void initialize(){
        eventsList.setCellFactory(lv -> {
                ListCell<Event> cell = new ListCell<Event>() {
                    @Override
                    protected void updateItem(Event item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(setCell(item.getTitle()) + setCell(item.getPlace().getName())
                                    + setCell(item.getStartDateTime().toString()));
                        }
                    }
                };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty()) {
                    ViewUtils.LoadedView<Node, EventDetailsViewPresenter> view = ViewUtils.loadView("EventDetailsView.fxml");
                    view.controller.setEvent(cell.getItem());
                    Stage stage = new Stage();
                    stage.setTitle("Event details");
                    stage.setScene(new Scene((Parent) view.view, 600, 450));
                    stage.show();
                    e.consume();
                }
            });
            return cell;
        });
    }

    private String setCell(String s){
        return String.format("|%-30s|", s);
    }
}
