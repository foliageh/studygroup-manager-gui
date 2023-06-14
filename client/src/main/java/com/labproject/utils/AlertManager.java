package com.labproject.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertManager {
    public static void showErrorAlert(String message, boolean autoLocalize) {
        Alert alert = new Alert(Alert.AlertType.ERROR, autoLocalize ? Localization.localize(message) : message, ButtonType.CLOSE);
//        alert.getButtonTypes().get(0).getButtonData()
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    public static void showErrorAlert(String message) {
        showErrorAlert(message, true);
    }

//    public static void showInfoAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION, Localization.localize(message), ButtonType.OK);
//        alert.setHeaderText(null);
//        alert.showAndWait();
//    }
}
