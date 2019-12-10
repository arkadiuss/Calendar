package app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ViewUtils {

    public static class LoadedView<T> {
        public final Node view;
        public final T controller;

        public LoadedView(Node view, T controller) {
            this.view = view;
            this.controller = controller;
        }
    }

    public static LoadedView loadView(String location){
        FXMLLoader loader = new FXMLLoader();
        URL url = null;
        try {
            url = new File("src/main/java/app/view/" + location).toURI().toURL();
            loader.setLocation(url);
            return new LoadedView(loader.load(), loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
