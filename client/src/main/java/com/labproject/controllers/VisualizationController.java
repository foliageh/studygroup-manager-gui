package com.labproject.controllers;

import com.labproject.app.App;
import com.labproject.client.Client;
import com.labproject.models.Coordinates;
import com.labproject.utils.ScreenManager;
import com.labproject.models.StudyGroup;
import javafx.animation.*;
import javafx.collections.ListChangeListener;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VisualizationController {
    public Pane pane;
    private final List<StudyGroupShape> studyGroupShapes = new ArrayList<>();

    public VisualizationController(Pane pane) {
        this.pane = pane;
    }

    public void initialize() {
        var studyGroupCollection = App.getTableManager().getCollection();
        addAll(studyGroupCollection);

        ListChangeListener<StudyGroup> listChangeListener = change -> {
            while (change.next()) {
                if (change.wasReplaced()) {
                    updateAll(change.getList().subList(change.getFrom(), change.getTo()));
                }
                else if (change.wasAdded()) {
                    addAll(change.getAddedSubList());
                }
                else if (change.wasRemoved()) {
                    removeAll(change.getRemoved());
                }
            }
        };
        studyGroupCollection.addListener(listChangeListener);
    }

    private void addAll(List<? extends StudyGroup> studyGroups) {
        for (StudyGroup studyGroup : studyGroups) {
            var shape = new StudyGroupShape(studyGroup, pane);
            studyGroupShapes.add(shape);
            shape.appear();
        }
    }

    private void removeAll(List<? extends StudyGroup> studyGroups) {
        for (StudyGroup studyGroup : studyGroups) {
            var shape = findShape(studyGroup);
            if (shape == null) return;
            studyGroupShapes.remove(shape);
            shape.disappear();
        }
    }

    private void updateAll(List<? extends StudyGroup> studyGroups) {
        for (StudyGroup studyGroup : studyGroups) {
            var shape = findShape(studyGroup);
            if (shape == null) return;
            if (!studyGroup.getCoordinates().equals(shape.getCoordinates())) { //shape.getStudyGroup().getCoordinates())) {
                shape.moveTo(studyGroup.getCoordinates().getX(), studyGroup.getCoordinates().getY());
            }
        }
    }

    private StudyGroupShape findShape(StudyGroup studyGroup) {
        return studyGroupShapes.stream().filter(sh -> sh.getStudyGroup().equals(studyGroup)).findAny().orElse(null);
    }


    private static class StudyGroupShape {
        private final StudyGroup studyGroup;
        private final Pane pane;
        private Group group;

        private final Coordinates coordinates;
        public Coordinates getCoordinates() {
            return coordinates;
        }

        public StudyGroupShape(StudyGroup studyGroup, Pane pane) {
            this.pane = pane;
            this.studyGroup = studyGroup;
            coordinates = studyGroup.getCoordinates();
        }

        public StudyGroup getStudyGroup() {
            return studyGroup;
        }

        public void appear() {
            var centerStudent = new StudentShape(studyGroup, 0);
            var leftStudent = new StudentShape(studyGroup, -1);
            var rightStudent = new StudentShape(studyGroup, 1);
            group = new Group(centerStudent.getGroup(), leftStudent.getGroup(), rightStudent.getGroup());
            group.setLayoutX(studyGroup.getCoordinates().getX());
            group.setLayoutY(studyGroup.getCoordinates().getY());
            pane.getChildren().add(group);
            centerStudent.animate();
            leftStudent.animate();
            rightStudent.animate();
        }

        public void disappear() {
            pane.getChildren().remove(group);
        }

        public void moveTo(double x, double y) {
            group.setLayoutX(x);
            group.setLayoutY(y);
        }

        private static class StudentShape {
            private final StudyGroup studyGroup;
            private final Color color;
            private final double startX;
            private final double startY;

            private final double targetX;
            private final double targetY;
            private final double offset;
            private final Group group;

            private final double HEAD_RADIUS = 20;
            private final double BODY_LENGTH = 40;
            private final double ARM_LENGTH = 25;
            private final double LEG_LENGTH = 20;
            private final Duration ANIMATION_DURATION = Duration.seconds(1.5);
            private final double ANIMATION_MOVE_RANGE = 15;

            public StudentShape(StudyGroup studyGroup, double offset) {
                this.offset = offset;
                this.studyGroup = studyGroup;
                this.targetX = 0; //studyGroup.getCoordinates().getX();
                this.targetY = 0; //studyGroup.getCoordinates().getY();
                this.color = studyGroup.getCreator().equals(Client.getUser()) ? Color.BLUE : Color.BLACK;

                Random random = new Random();
                if (random.nextBoolean()) {
                    startX = -HEAD_RADIUS*2;
                    startY = random.nextDouble(500);
                } else {
                    startX = random.nextDouble(800);
                    startY = -HEAD_RADIUS-BODY_LENGTH-LEG_LENGTH;
                }

                group = createGroup();
            }

            public Group getGroup() {
                return group;
            }

            private Group createGroup() {
                Circle head = new Circle(targetX, targetY, HEAD_RADIUS);
                head.setStroke(color);
                head.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2)
                        ScreenManager.showStudyGroupCardScreen(studyGroup);
                });

                double bodyStartY = targetY + HEAD_RADIUS;
                double bodyEndY = bodyStartY + BODY_LENGTH;
                Line body = new Line(targetX, bodyStartY, targetX, bodyEndY);
                body.setStroke(color);

                double armY = bodyStartY + BODY_LENGTH / 4;
                double leftArmX = targetX - ARM_LENGTH;
                double rightArmX = targetX + ARM_LENGTH;
                Line leftArm = new Line(targetX, armY, leftArmX, armY);
                leftArm.setStroke(color);
                Line rightArm = new Line(targetX, armY, rightArmX, armY);
                rightArm.setStroke(color);

                double legStartY = bodyEndY;
                double legEndY = legStartY + LEG_LENGTH;
                double leftLegStartX = targetX - LEG_LENGTH;
                double rightLegStartX = targetX + LEG_LENGTH;
                Line leftLeg = new Line(targetX, legStartY, leftLegStartX, legEndY);
                leftLeg.setStroke(color);
                Line rightLeg = new Line(targetX, legStartY, rightLegStartX, legEndY);
                rightLeg.setStroke(color);

                Text text = new Text(targetX, targetY, String.valueOf(studyGroup.getId()));
                text.setX(targetX - text.prefWidth(-1)/2 - 1);
                text.setY(targetY);
                text.setTextOrigin(VPos.CENTER);

                return new Group(head, body, leftArm, rightArm, leftLeg, rightLeg, text);
            }

            public void animate() {
                TranslateTransition moveTransition = new TranslateTransition(ANIMATION_DURATION, group);
                moveTransition.setFromX(startX);
                moveTransition.setFromY(startY);
                moveTransition.setToX(targetX);
                moveTransition.setToY(targetY);

                TranslateTransition leftRightTransition = new TranslateTransition(ANIMATION_DURATION, group);
                leftRightTransition.setFromX(targetX);
                leftRightTransition.setToX(targetX + ANIMATION_MOVE_RANGE * offset);
                leftRightTransition.setAutoReverse(true);
                leftRightTransition.setCycleCount(Animation.INDEFINITE);

                moveTransition.setOnFinished(event -> leftRightTransition.play());
                moveTransition.play();
            }
        }
    }
}
