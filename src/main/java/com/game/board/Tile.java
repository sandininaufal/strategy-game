package com.game.board;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {

    private int x, y;
    private Rectangle border;
    private boolean selected = false;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;

        border = new Rectangle(80, 80);

        if ((x + y) % 2 == 0) {
            border.setFill(Color.BEIGE);
        } else {
            border.setFill(Color.BROWN);
        }

        getChildren().add(border);

        setOnMouseClicked(e -> handleClick());
    }

    private void handleClick() {
        selected = !selected;

        if (selected) {
            border.setFill(Color.YELLOW);
        } else {
            resetColor();
        }
    }

    public void highlight() {
        border.setFill(Color.LIGHTGREEN);
    }

    public void resetColor() {
        if ((x + y) % 2 == 0) {
            border.setFill(Color.BEIGE);
        } else {
            border.setFill(Color.BROWN);
        }
    }
}