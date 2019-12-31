package app.di;

import app.AppContext;
import logic.service.CalendarService;

public class DIProvider {
    private static AppContext appContext = null;

    private static CalendarService calendarService = null;

    public static AppContext getAppContext() {
        if (appContext == null) appContext = new AppContext();
        return appContext;
    }

    public static CalendarService getCalendarService() {
        if(calendarService == null) calendarService = new CalendarService();
        return calendarService;
    }
}
