package com.game.board;

import com.game.entity.Unit;
import com.game.entity.UnitType;
import com.game.entity.ElementType;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends Pane {

    private Tile[][] tiles = new Tile[8][8];
    private Unit selectedUnit = null;
    private boolean playerOneTurn = true;

    private List<Unit> units = new ArrayList<>();
    private List<int[]> validMoves = new ArrayList<>();

    private Text statusText;

    private ElementType p1Element;
    private ElementType p2Element;

    // =========================
    // CONSTRUCTOR
    // =========================
    public GameBoard(ElementType p1, ElementType p2) {
        this.p1Element = p1;
        this.p2Element = p2;

        initBoard();
        initUI();
        spawnUnits();
    }

    // =========================
    // INIT BOARD
    // =========================
    private void initBoard() {
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
    }

    private void initUI() {
        statusText = new Text();
        statusText.setFont(new Font(30));
        statusText.setFill(Color.RED);
        statusText.setTranslateX(150);
        statusText.setTranslateY(30);
        getChildren().add(statusText);
    }

    // =========================
    // SPAWN
    // =========================
    private void spawnUnits() {
        spawnBackRow(7, Color.BLUE, true, p1Element);
        spawnPawns(6, Color.BLUE, true, p1Element);

        spawnBackRow(0, Color.RED, false, p2Element);
        spawnPawns(1, Color.RED, false, p2Element);
    }

    private void spawnBackRow(int y, Color color, boolean isPlayerOne, ElementType element) {

        UnitType[] order = {
                UnitType.ROOK, UnitType.KNIGHT, UnitType.BISHOP,
                UnitType.QUEEN, UnitType.KING,
                UnitType.BISHOP, UnitType.KNIGHT, UnitType.ROOK
        };

        for (int x = 0; x < 8; x++) {
            Unit u = new Unit(x, y, order[x], element, color);
            u.setTranslateX(x * 80);
            u.setTranslateY(y * 80);

            setupClick(u, isPlayerOne);

            units.add(u);
            getChildren().add(u);
        }
    }

    private void spawnPawns(int y, Color color, boolean isPlayerOne, ElementType element) {
        for (int x = 0; x < 8; x++) {
            Unit p = new Unit(x, y, UnitType.PAWN, element, color);
            p.setTranslateX(x * 80);
            p.setTranslateY(y * 80);

            setupClick(p, isPlayerOne);

            units.add(p);
            getChildren().add(p);
        }
    }

    private void setupClick(Unit unit, boolean isPlayerOne) {
        unit.setOnMouseClicked(e -> {
            if (playerOneTurn != isPlayerOne) return;
            selectUnit(unit);
            e.consume();
        });
    }

    // =========================
    // SELECT UNIT
    // =========================
    private void selectUnit(Unit unit) {

        selectedUnit = unit;
        clearHighlights();
        validMoves.clear();

        List<int[]> moves = unit.getValidMoves(units, 8);

        for (int[] m : moves) {
            if (isSafeMove(unit, m[0], m[1])) {
                tiles[m[0]][m[1]].highlight();
                validMoves.add(m);
            }
        }
    }

    // =========================
    // HANDLE CLICK
    // =========================
    private void handleTileClick(int x, int y) {

        if (selectedUnit == null) return;

        // VALIDATION
        if (!isValidMove(x, y)) return;
        if (!isSafeMove(selectedUnit, x, y)) return;

        Unit target = getUnitAt(x, y);

        // ATTACK
        if (target != null && selectedUnit.isEnemy(target)) {

            selectedUnit.useSkill(target);

            if (target.isDead()) {
                getChildren().remove(target);
                units.remove(target);
            }

        }
        // CLICK TEAM (DO NOTHING)
        else if (target != null) {
            return;
        }
        // MOVE
        else {
            selectedUnit.move(x, y);
            checkPromotion(selectedUnit);
        }

        selectedUnit = null;
        clearHighlights();

        applyAllTurnEffects();
        updateGameState();

        playerOneTurn = !playerOneTurn;
    }

    // =========================
    // TURN EFFECT
    // =========================
    private void applyAllTurnEffects() {
        for (Unit u : units) {
            u.applyTurnEffect();
        }
    }

    // =========================
    // PROMOTION
    // =========================
    private void checkPromotion(Unit unit) {
        if (unit.getType() != UnitType.PAWN) return;

        if (unit.getY() == 0 || unit.getY() == 7) {
            promote(unit);
        }
    }

    private void promote(Unit pawn) {

        Unit newUnit = new Unit(
                pawn.getX(),
                pawn.getY(),
                UnitType.QUEEN,
                pawn.getElement(),
                pawn.getFillColor()
        );

        newUnit.setTranslateX(pawn.getX() * 80);
        newUnit.setTranslateY(pawn.getY() * 80);

        getChildren().remove(pawn);
        units.remove(pawn);

        setupClick(newUnit, pawn.getFillColor().equals(Color.BLUE));

        units.add(newUnit);
        getChildren().add(newUnit);
    }

    // =========================
    // CHECK SYSTEM
    // =========================
    private void updateGameState() {

        if (isKingInCheck(Color.BLUE)) {
            statusText.setText(isCheckmate(Color.BLUE) ? "CHECKMATE! RED MENANG" : "BLUE CHECK!");
        }
        else if (isKingInCheck(Color.RED)) {
            statusText.setText(isCheckmate(Color.RED) ? "CHECKMATE! BLUE MENANG" : "RED CHECK!");
        }
        else {
            statusText.setText("");
        }
    }

    private boolean isKingInCheck(Color color) {

        Unit king = null;

        for (Unit u : units) {
            if (u.getType() == UnitType.KING && u.getFillColor().equals(color)) {
                king = u;
                break;
            }
        }

        if (king == null) return false;

        for (Unit u : units) {
            if (!u.getFillColor().equals(color)) {

                List<int[]> moves = u.getValidMoves(units, 8);

                for (int[] m : moves) {
                    if (m[0] == king.getX() && m[1] == king.getY()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isCheckmate(Color color) {

        for (Unit u : units) {
            if (u.getFillColor().equals(color)) {

                List<int[]> moves = u.getValidMoves(units, 8);

                for (int[] m : moves) {
                    if (isSafeMove(u, m[0], m[1])) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // =========================
    // SAFE MOVE
    // =========================
    private boolean isSafeMove(Unit unit, int newX, int newY) {

        int oldX = unit.getX();
        int oldY = unit.getY();

        Unit captured = getUnitAt(newX, newY);

        unit.moveSimulated(newX, newY);
        if (captured != null) units.remove(captured);

        boolean safe = !isKingInCheck(unit.getFillColor());

        unit.moveSimulated(oldX, oldY);
        if (captured != null) units.add(captured);

        return safe;
    }

    // =========================
    // UTIL
    // =========================
    private Unit getUnitAt(int x, int y) {
        for (Unit u : units) {
            if (u.getX() == x && u.getY() == y) return u;
        }
        return null;
    }

    private void clearHighlights() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                tiles[x][y].resetColor();
            }
        }
    }

    private boolean isValidMove(int x, int y) {
        for (int[] m : validMoves) {
            if (m[0] == x && m[1] == y) return true;
        }
        return false;
    }
}