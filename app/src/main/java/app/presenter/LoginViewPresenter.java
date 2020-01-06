package app.presenter;

import app.controller.StartController;
import app.util.AlertPopup;
import com.google.common.base.Strings;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.service.UserService;

public class LoginViewPresenter {

    @FXML
    public TextField userNameField;
    @FXML
    public PasswordField passwordField;
    public Button signInButton;
    public Button cancelButton;
    private Stage dialogStage;
    private UserService userService = new UserService();
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
            signInButton.setText("Checking...");
            signInButton.setDisable(true);
            userService.getUser(userNameField.getText(), passwordField.getText())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe((user) -> {
                        signInButton.setText("Done!");
                        dialogStage.close();
                        startController.showCalendar(user);
                    }, error -> {
                        AlertPopup.showAlert(String.format("User %s does not exist or wrong password", userNameField.getText()));
                        signInButton.setDisable(false);
                    });
        }

    }

    public void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
