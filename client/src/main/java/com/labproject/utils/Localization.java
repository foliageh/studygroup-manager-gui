package com.labproject.utils;

import com.labproject.utils.ScreenManager.Screen;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

public class Localization {
    private static final String[] LANGUAGES = {"en-GB", "ru-RU", "de-DE", "sq-AL"};
    private static int currentLanguage;
    private static ResourceBundle currentBundle;
    private static final ResourceBundle localeTagsBundle = getBundle("");
    private static final Map<String, String> nodesLocaleTags = new HashMap<>();  // nodeId : nodeLocaleTag
    private static final List<Labeled> nodesToLocalize = new ArrayList<>();

    public static void initialize() {
        try {
            FXMLLoader loader = getLoader(Screen.MAIN.resourceName);
            loader.setResources(localeTagsBundle);
            setLocaleTags(loader.load());
            currentBundle = getBundle(LANGUAGES[currentLanguage]);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void toggleLanguage() {
        currentLanguage = ++currentLanguage % LANGUAGES.length;
        currentBundle = getBundle(LANGUAGES[currentLanguage]);
        nodesToLocalize.forEach(node -> node.setText(currentBundle.getString(nodesLocaleTags.get(node.getId()))));
        ScreenManager.getMainWindow().sizeToScene();
    }

    public static String localize(String key) {
        return currentBundle.getString(key);
    }

    public static String localize(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentBundle.getLocale()).format(date);
    }

    private static FXMLLoader getLoader(String resourceName) {
        return new FXMLLoader(Screen.class.getResource(resourceName));
    }

    private static ResourceBundle getBundle(String languageTag) {
        return ResourceBundle.getBundle("locales/gui", Locale.forLanguageTag(languageTag));
    }

    public static Scene prepareScene(Screen screen, Object controller) {
        try {
            FXMLLoader loader = getLoader(screen.resourceName);
            loader.setResources(currentBundle);
            if (controller != null) loader.setController(controller);
            Parent root = loader.load();
            if (screen.equals(Screen.MAIN)) {
                nodesToLocalize.clear();
                setNodesToLocalize(root);
            }
            return new Scene(root);
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    public static Scene prepareScene(Screen screen) { return prepareScene(screen, null); }

    private static void setLocaleTags(Parent root) throws LoadException {
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof Labeled l) {
                if (nodesLocaleTags.containsKey(l.getId()))
                    throw new LoadException("Every localizable node must have unique id! Id \"" + l.getId() + "\" is not unique.");
                if (!localeTagsBundle.containsKey(l.getText())) continue;
                if (l.getId() == null) throw new LoadException("Every localizable node must have id!");
                nodesLocaleTags.put(l.getId(), l.getText());
            } else if (node instanceof TabPane tp) {
                Pane p = new Pane();
                tp.getTabs().forEach(tab -> p.getChildren().add(tab.getContent()));
                setLocaleTags(p);
            } else if (node instanceof Parent p) {
                setLocaleTags(p);
            }
        }
    }

    private static void setNodesToLocalize(Parent root) {
        for (Node node : root.getChildrenUnmodifiable()) {
            if (nodesLocaleTags.containsKey(node.getId())) {
                nodesToLocalize.add((Labeled) node);
            } else if (node instanceof TabPane tp) {
                Pane p = new Pane();
                tp.getTabs().forEach(tab -> p.getChildren().add(tab.getContent()));
                setNodesToLocalize(p);
            } else if (node instanceof Parent p) {
                setNodesToLocalize(p);
            }
        }
    }
}
