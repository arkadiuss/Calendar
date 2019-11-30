package app.presenter;

import app.util.AlertPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.model.User;
import logic.service.UserService;

public class RegisterViewPresenter {
    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;

    @FXML
    public TextField emailField;

    private Stage dialogStage;
    private UserService userService;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }


    @FXML
    private void handleSignUp(ActionEvent actionEvent) {
        if (validateInput()) {
            User user = new User(usernameField.getText(), passwordField.getText(), emailField.getText());
            userService.addUser(user);
            dialogStage.close();
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
