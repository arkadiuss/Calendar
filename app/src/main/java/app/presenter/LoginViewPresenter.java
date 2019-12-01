package app.presenter;

import app.controller.StartController;
import app.util.AlertPopup;
import com.google.common.base.Strings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.model.User;
import logic.service.UserService;

import java.util.Optional;

public class LoginViewPresenter {

    @FXML
    public TextField userNameField;
    @FXML
    public PasswordField passwordField;
    public Button signInButton;
    public Button cancelButton;
    private Stage dialogStage;
    private UserService userService;
    private StartController startController;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private boolean isInputValid() {
        if (Strings.isNullOrEmpty(userNameField.getText())
                || Strings.isNullOrEmpty(passwordField.getText())) {
            AlertPopup.showAlert("Username and password cannot be empty");
            return false;
        }
        return true;
    }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }

    public void handleSignIn(ActionEvent actionEvent) {
        if (isInputValid()) {
            Optional<User> optionalUser = userService.getUser(userNameField.getText(), passwordField.getText());
            if (optionalUser.isPresent()) {
                dialogStage.close();
                startController.showCalendar(optionalUser.get());
            } else {
                AlertPopup.showAlert(String.format("User %s does not exist or wrong password", userNameField.getText()));
            }
        }

    }

    public void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
