package app.presenter;

import app.controller.StartController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class WelcomeViewPresenter {
    @FXML
    public AnchorPane welcomeAnchor;
    @FXML
    public Button signInButton;
    @FXML
    public Button signUpButton;
    private StartController startController;

    @FXML
    public void handleSignUp(ActionEvent actionEvent) {

    }

    @FXML
    public void handleSignIn(ActionEvent actionEvent) {
        startController.showLoginWindow();
    }

    public void setAppController(StartController startController) {
        this.startController = startController;
    }
}
