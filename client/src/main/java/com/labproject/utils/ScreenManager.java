package com.labproject.utils;

import com.labproject.client.Client;
import com.labproject.controllers.StudyGroupCardController;
import com.labproject.models.StudyGroup;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScreenManager {
    private static Stage mainWindow;

    public static Stage getMainWindow() {
        return mainWindow;
    }

    public static void setMainWindow(Stage stage) {
        mainWindow = stage;
//        mainWindow.setResizable(false);
        mainWindow.setOnCloseRequest(event -> {
            event.consume();
            Client.close();
            mainWindow.close();
        });
    }

    public static void showAuthScreen() {
        Scene scene = Localization.prepareScene(Screen.AUTH);
        mainWindow.setTitle(Localization.localize("authentication"));
        mainWindow.setScene(scene);
        mainWindow.sizeToScene();
        mainWindow.show();
    }

    public static void showMainScreen() {
        Scene scene = Localization.prepareScene(Screen.MAIN);
        mainWindow.setTitle(Localization.localize("studyGroups"));
        mainWindow.setScene(scene);
        mainWindow.sizeToScene();
        mainWindow.show();
    }

    public static void showStudyGroupCardScreen(StudyGroup studyGroup) {
        Scene scene = Localization.prepareScene(Screen.STUDY_GROUP_CARD, new StudyGroupCardController(studyGroup));
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainWindow);
        stage.setResizable(false);
        stage.setTitle(Localization.localize("studyGroupCard"));
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public enum Screen {
        MAIN("/views/main.fxml"),
        AUTH("/views/auth.fxml"),
        STUDY_GROUP_CARD("/views/study_group_card.fxml");

        public final String resourceName;
        Screen(String resourceName) {
            this.resourceName = resourceName;
        }
    }
}

