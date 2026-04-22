package com.game;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

public class MainMenu extends VBox {

    public MainMenu() {

        setSpacing(20);
        setAlignment(Pos.CENTER);

        Button startBtn = new Button("START GAME");
        Button tutorialBtn = new Button("TUTORIAL");
        Button exitBtn = new Button("EXIT");

        // LOCK START GAME
        if (!MainApp.isTutorialCompleted()) {
            startBtn.setDisable(true);
        }

        startBtn.setOnAction(e -> MainApp.showSetup());

        tutorialBtn.setOnAction(e -> MainApp.showTutorial());

        exitBtn.setOnAction(e -> System.exit(0));

        getChildren().addAll(startBtn, tutorialBtn, exitBtn);
    }
}