package app.presenter;

import logic.exceptions.UserAlreadyExistException;
import app.util.AlertPopup;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.model.User;
import logic.service.UserService;

public class RegisterViewPresenter {
    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button signUpButton;

    @FXML
    public TextField emailField;

    private Stage dialogStage;
    private UserService userService = new UserService();

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    @FXML
    private void handleSignUp(ActionEvent actionEvent) {
        if (validateInput()) {
            signUpButton.setText("Checking...");
            signUpButton.setDisable(true);
            User user = new User(usernameField.getText(), passwordField.getText(), emailField.getText());
            userService.addUser(user)
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(() -> {
                        signUpButton.setText("Done!");
                        dialogStage.close();
                    }, error -> {
                        AlertPopup.showAlert("User already exists");
                        signUpButton.setDisable(false);
                    });
        }
    }

    private boolean validateInput() {
        if (!isEmailValid(emailField.getText())) {
            AlertPopup.showAlert("Email should be in xxx@domain.com");
            return false;
        }
        if (usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty()) {
            AlertPopup.showAlert("Username and password cannot be empty");
            return false;
        }
        return true;
    }


    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
