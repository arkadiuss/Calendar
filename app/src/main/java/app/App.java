/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package app;

import app.controller.StartController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private Stage primaryStage;
    private StartController appController;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Calendar");
        this.appController = new StartController(primaryStage);
        this.appController.initRootLayout();
    }

    public static void main(String[] args) {
        launch(args);
    }

//    public static void main(String[] args)
//    {
//        User user = new User("u1", "aps");
//        System.out.println("Hello " + user.getUsername());
//
//        UserService service = new UserService();
//
//        service.addUser(user);
//
//        Calendar calendar = new Calendar("someName", user);
//        Calendar calendar2 = new Calendar("otherName", user);
//        calendar2.setDescription("desc");
//
//        CalendarService calendarService = new CalendarService();
//        calendarService.addCalendar(calendar);
//        calendarService.addCalendar(calendar2);
//
//        user.getCalendars().forEach(System.out::println);
//    }


}