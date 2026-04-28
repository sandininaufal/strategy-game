package com.game;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.application.Platform;

public class MainMenu extends VBox {

    public MainMenu() {

        setSpacing(20);
        setAlignment(Pos.CENTER);

        Text title = new Text("STRATEGY GAME");

        Text status = new Text();

        Button startBtn = new Button("START GAME");
        Button tutorialBtn = new Button("TUTORIAL");
        Button exitBtn = new Button("EXIT");

        startBtn.setPrefWidth(200);
        tutorialBtn.setPrefWidth(200);
        exitBtn.setPrefWidth(200);

        // =========================
        // LOCK SYSTEM + STATUS
        // =========================
        if (!MainApp.isTutorialCompleted()) {
            startBtn.setDisable(true);
            startBtn.setText("START GAME (LOCKED)");
            status.setText("🔒 Selesaikan Tutorial untuk membuka Start Game");
        } else {
            startBtn.setDisable(false);
            startBtn.setText("START GAME");
            status.setText("✅ Game Ready!");
        }

        // =========================
        // BUTTON ACTION
        // =========================
        startBtn.setOnAction(e -> MainApp.showSetup());

        tutorialBtn.setOnAction(e -> MainApp.showTutorial());

        exitBtn.setOnAction(e -> Platform.exit());

        // =========================
        // ADD TO LAYOUT
        // =========================
        getChildren().addAll(title, status, startBtn, tutorialBtn, exitBtn);
    }
}