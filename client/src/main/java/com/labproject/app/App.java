package com.labproject.app;

import com.labproject.client.Client;
import com.labproject.utils.Localization;
import com.labproject.utils.ScreenManager;
import com.labproject.utils.TableManager;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class App extends Application {
    private static TableManager tableManager;
    public static TableManager getTableManager() {
        return tableManager;
    }
    public static void setTableManager(TableManager tableManager) {
        App.tableManager = tableManager;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Localization.initialize();
        ScreenManager.setMainWindow(stage);
        ScreenManager.showAuthScreen();
        while (!Client.connectToServer()) {
            Alert alert = new Alert(AlertType.ERROR, Localization.localize("msg.connectionFailed"), ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            if (alert.showAndWait().get() != ButtonType.YES) {
                stage.close();
                break;
            }
        }
    }
}