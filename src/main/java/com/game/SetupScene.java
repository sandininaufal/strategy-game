package com.game;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

import com.game.entity.ElementType;

public class SetupScene extends VBox {

    private ElementType p1Element1 = null;
    private ElementType p1Element2 = null;

    private ElementType p2Element1 = null;
    private ElementType p2Element2 = null;

    public SetupScene() {

        setSpacing(15);
        setAlignment(Pos.CENTER);

        Text title = new Text("SELECT 2 ELEMENTS");

        Text p1Text = new Text("Player 1: -");
        Text p2Text = new Text("Player 2: -");

        // 🔥 ERROR TEXT (UI NOTIFICATION)
        Text errorText = new Text();
        errorText.setStyle("-fx-fill: red;");

        // =========================
        // PLAYER 1 BUTTON
        // =========================
        Button p1Fire = new Button("P1 FIRE");
        Button p1Water = new Button("P1 WATER");
        Button p1Earth = new Button("P1 EARTH");
        Button p1Lightning = new Button("P1 LIGHTNING");

        p1Fire.setOnAction(e -> selectP1(ElementType.FIRE, p1Text));
        p1Water.setOnAction(e -> selectP1(ElementType.WATER, p1Text));
        p1Earth.setOnAction(e -> selectP1(ElementType.EARTH, p1Text));
        p1Lightning.setOnAction(e -> selectP1(ElementType.LIGHTNING, p1Text));

        // =========================
        // PLAYER 2 BUTTON
        // =========================
        Button p2Fire = new Button("P2 FIRE");
        Button p2Water = new Button("P2 WATER");
        Button p2Earth = new Button("P2 EARTH");
        Button p2Lightning = new Button("P2 LIGHTNING");

        p2Fire.setOnAction(e -> selectP2(ElementType.FIRE, p2Text));
        p2Water.setOnAction(e -> selectP2(ElementType.WATER, p2Text));
        p2Earth.setOnAction(e -> selectP2(ElementType.EARTH, p2Text));
        p2Lightning.setOnAction(e -> selectP2(ElementType.LIGHTNING, p2Text));

        // =========================
        // START GAME
        // =========================
        Button startBtn = new Button("START GAME");

        startBtn.setOnAction(e -> {

            if (p1Element1 == null || p1Element2 == null ||
                    p2Element1 == null || p2Element2 == null) {

                errorText.setText("⚠ Pilih 2 element untuk masing-masing player!");

                // 🔥 AUTO HIDE ERROR
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(ev -> errorText.setText(""));
                pause.play();

                return;
            }

            errorText.setText("");

            MainApp.startGameWithSetup(
                    p1Element1, p1Element2,
                    p2Element1, p2Element2
            );
        });

        getChildren().addAll(
                title,
                p1Text, p1Fire, p1Water, p1Earth, p1Lightning,
                p2Text, p2Fire, p2Water, p2Earth, p2Lightning,
                errorText, // 🔥 NOTIF DI SINI
                startBtn
        );
    }

    // =========================
    // PLAYER 1 LOGIC
    // =========================
    private void selectP1(ElementType e, Text text) {

        if (p1Element1 == null) {
            p1Element1 = e;
        } else if (p1Element2 == null && e != p1Element1) {
            p1Element2 = e;
        } else {
            p1Element1 = e;
            p1Element2 = null;
        }

        updateTextP1(text);
    }

    private void updateTextP1(Text text) {
        text.setText("Player 1: " +
                (p1Element1 != null ? p1Element1 : "-") +
                " & " +
                (p1Element2 != null ? p1Element2 : "-"));
    }

    // =========================
    // PLAYER 2 LOGIC
    // =========================
    private void selectP2(ElementType e, Text text) {

        if (p2Element1 == null) {
            p2Element1 = e;
        } else if (p2Element2 == null && e != p2Element1) {
            p2Element2 = e;
        } else {
            p2Element1 = e;
            p2Element2 = null;
        }

        updateTextP2(text);
    }

    private void updateTextP2(Text text) {
        text.setText("Player 2: " +
                (p2Element1 != null ? p2Element1 : "-") +
                " & " +
                (p2Element2 != null ? p2Element2 : "-"));
    }
}