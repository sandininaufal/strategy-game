package com.game;

import com.game.entity.ElementType;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public class SetupScene extends VBox {

    private ElementType p1Element = ElementType.FIRE;
    private ElementType p2Element = ElementType.WATER;

    public SetupScene() {

        setSpacing(20);
        setAlignment(Pos.CENTER);

        Text title = new Text("SELECT ELEMENT");

        Text p1Text = new Text("Player 1 Element: FIRE");
        Text p2Text = new Text("Player 2 Element: WATER");

        // P1 Buttons
        Button p1Fire = new Button("P1 FIRE");
        Button p1Water = new Button("P1 WATER");
        Button p1Earth = new Button("P1 EARTH");
        Button p1Lightning = new Button("P1 LIGHTNING");

        p1Fire.setOnAction(e -> {
            p1Element = ElementType.FIRE;
            p1Text.setText("Player 1 Element: FIRE");
        });

        p1Water.setOnAction(e -> {
            p1Element = ElementType.WATER;
            p1Text.setText("Player 1 Element: WATER");
        });

        p1Earth.setOnAction(e -> {
            p1Element = ElementType.EARTH;
            p1Text.setText("Player 1 Element: EARTH");
        });

        p1Lightning.setOnAction(e -> {
            p1Element = ElementType.LIGHTNING;
            p1Text.setText("Player 1 Element: LIGHTNING");
        });

        // P2 Buttons
        Button p2Fire = new Button("P2 FIRE");
        Button p2Water = new Button("P2 WATER");
        Button p2Earth = new Button("P2 EARTH");
        Button p2Lightning = new Button("P2 LIGHTNING");

        p2Fire.setOnAction(e -> {
            p2Element = ElementType.FIRE;
            p2Text.setText("Player 2 Element: FIRE");
        });

        p2Water.setOnAction(e -> {
            p2Element = ElementType.WATER;
            p2Text.setText("Player 2 Element: WATER");
        });

        p2Earth.setOnAction(e -> {
            p2Element = ElementType.EARTH;
            p2Text.setText("Player 2 Element: EARTH");
        });

        p2Lightning.setOnAction(e -> {
            p2Element = ElementType.LIGHTNING;
            p2Text.setText("Player 2 Element: LIGHTNING");
        });

        Button startBtn = new Button("START GAME");

        startBtn.setOnAction(e -> {
            MainApp.startGameWithSetup(p1Element, p2Element);
        });

        getChildren().addAll(
                title,
                p1Text, p1Fire, p1Water, p1Earth, p1Lightning,
                p2Text, p2Fire, p2Water, p2Earth, p2Lightning,
                startBtn
        );
    }
}