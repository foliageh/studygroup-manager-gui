package com.labproject.utils;

import com.labproject.app.App;
import com.labproject.commands.CollectCommand;
import com.labproject.commands.Command;
import com.labproject.commands.InfoCommand;
import com.labproject.models.StudyGroup;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CommandExecutor {
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });
    private static Future<?> commandFuture;

    public static void execute(Command<?> command) {
        if (commandIsRunning()) AlertManager.showErrorAlert("msg.pleaseWait");
        else commandFuture = executorService.submit(command);
    }

    public static boolean commandIsRunning() {
        return commandFuture != null && !commandFuture.isDone();
    }

    public static void startUpdateLoop(StringProperty collectionInfo) {
        new UpdateCollectionService(collectionInfo).start();
    }

    private static class UpdateCollectionService extends ScheduledService<String> {
        public UpdateCollectionService(StringProperty collectionInfo) {
            setPeriod(Duration.seconds(3));
            collectionInfo.bind(lastValueProperty());
        }

        @Override
        protected Task<String> createTask() {
            return new Task<>() {
                private Collection<StudyGroup> collection;
                @Override
                protected String call() {
                    collection = new CollectCommand().call();
                    return new InfoCommand().call();
                }
                @Override
                protected void succeeded() {
                    collection.forEach(studyGroup -> App.getTableManager().update(studyGroup));
                }
            };
        }
    }
}
