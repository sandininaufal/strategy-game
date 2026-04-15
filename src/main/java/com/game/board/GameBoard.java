package com.game.board;

import com.game.entity.Unit;
import com.game.entity.UnitType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends Pane {

    private Tile[][] tiles = new Tile[8][8];
    private Unit selectedUnit = null;

    private boolean playerOneTurn = true;
    private List<Unit> units = new ArrayList<>();

    public GameBoard() {


        // BUAT TILE (BOARD)

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {

                Tile tile = new Tile(x, y);
                tile.setTranslateX(x * 80);
                tile.setTranslateY(y * 80);

                final int fx = x;
                final int fy = y;

                tile.setOnMouseClicked(e -> handleTileClick(fx, fy));

                tiles[x][y] = tile;
                getChildren().add(tile);
            }
        }


        // UNIT PLAYER 1 (BIRU)

        Unit unit1 = new Unit(3, 3, UnitType.KING, Color.BLUE);
        unit1.setTranslateX(3 * 80);
        unit1.setTranslateY(3 * 80);

        unit1.setOnMouseClicked(e -> {
            if (!playerOneTurn) return;
            selectUnit(unit1);
            e.consume();
        });


        // UNIT PLAYER 2 (MERAH)

        Unit unit2 = new Unit(4, 3, UnitType.QUEEN, Color.RED);
        unit2.setTranslateX(4 * 80);
        unit2.setTranslateY(3 * 80);

        unit2.setOnMouseClicked(e -> {
            if (playerOneTurn) return;
            selectUnit(unit2);
            e.consume();
        });

        units.add(unit1);
        units.add(unit2);

        getChildren().addAll(unit1, unit2);
    }


    // SELECT UNIT

    private void selectUnit(Unit unit) {
        selectedUnit = unit;
        clearHighlights();

        int x = unit.getX();
        int y = unit.getY();

        highlightIfValid(x + 1, y);
        highlightIfValid(x - 1, y);
        highlightIfValid(x, y + 1);
        highlightIfValid(x, y - 1);
    }

    private void highlightIfValid(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            tiles[x][y].highlight();
        }
    }

    private void clearHighlights() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                tiles[x][y].resetColor();
            }
        }
    }


    // CEK ADA UNIT DI TILE

    private Unit getUnitAt(int x, int y) {
        for (Unit u : units) {
            if (u.getX() == x && u.getY() == y) {
                return u;
            }
        }
        return null;
    }


    // HANDLE CLICK (MOVE + ATTACK)

    private void handleTileClick(int x, int y) {

        if (selectedUnit != null) {

            Unit target = getUnitAt(x, y);

            if (target != null && target != selectedUnit) {

                // ATTACK

                System.out.println("ATTACK TERJADI");

                target.takeDamage(selectedUnit.getAttack());

                if (target.isDead()) {
                    getChildren().remove(target);
                    units.remove(target);
                    System.out.println("Unit mati!");
                }

            } else {

                // MOVE

                selectedUnit.move(x, y);
            }

            selectedUnit = null;
            clearHighlights();


            // GANTI TURN

            playerOneTurn = !playerOneTurn;

            System.out.println("Turn: " + (playerOneTurn ? "Player 1" : "Player 2"));
        }
    }
}