package app.di;

import app.AppContext;

public class DIProvider {
    private static AppContext appContext = null;

    public static AppContext getAppContaxt() {
        if (appContext == null) appContext = new AppContext();
        return appContext;
    }
}
