package com.game;

import com.game.board.GameBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        GameBoard board = new GameBoard();

        Scene scene = new Scene(board, 640, 640);

        stage.setTitle("Strategy Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}