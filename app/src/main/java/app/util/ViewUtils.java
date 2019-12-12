package app.util;

import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ViewUtils {

    public static class LoadedView<V, T> {
        public final V view;
        public final T controller;

        public LoadedView(V view, T controller) {
            this.view = view;
            this.controller = controller;
        }
    }

    public static <V, T> LoadedView<V, T> loadView(String location) {
        FXMLLoader loader = new FXMLLoader();
        URL url;
        try {
            url = new File("src/main/java/app/view/" + location).toURI().toURL();
            loader.setLocation(url);
            return new LoadedView<V, T>((V) loader.load(), (T) loader.getController());
        } catch (IOException e) {
            throw new RuntimeException("Cannot load view ", e);
        }

    }
}
