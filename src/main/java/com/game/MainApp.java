package com.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static boolean tutorialCompleted = false;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showMainMenu();
    }

    public static void showMainMenu() {
        MainMenu menu = new MainMenu();
        Scene scene = new Scene(menu, 640, 640);

        primaryStage.setTitle("Strategy Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showTutorial() {
        TutorialScene tutorial = new TutorialScene();
        Scene scene = new Scene(tutorial, 640, 640);

        primaryStage.setScene(scene);
    }

    public static void startGameWithSetup(
            com.game.entity.ElementType p1,
            com.game.entity.ElementType p2
    ) {
        com.game.board.GameBoard board =
                new com.game.board.GameBoard(p1, p2);

        Scene scene = new Scene(board, 640, 640);
        primaryStage.setScene(scene);
    }

    public static boolean isTutorialCompleted() {
        return tutorialCompleted;
    }

    public static void setTutorialCompleted(boolean value) {
        tutorialCompleted = value;
    }

    public static void showSetup() {
        SetupScene setup = new SetupScene();
        Scene scene = new Scene(setup, 640, 640);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}