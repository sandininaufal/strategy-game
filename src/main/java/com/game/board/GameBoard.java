package com.game.board;

import com.game.entity.Unit;
import com.game.entity.UnitType;
import com.game.entity.ElementType;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

import javafx.animation.FadeTransition;
import javafx.util.Duration;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle; // 🔥 TAMBAHAN

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends Pane {

    private Tile[][] tiles = new Tile[8][8];
    private Unit selectedUnit = null;
    private boolean playerOneTurn = true;

    private boolean isAnimating = false;

    private List<Unit> units = new ArrayList<>();
    private List<int[]> validMoves = new ArrayList<>();

    private Text statusText;
    private Rectangle statusBg; // 🔥 TAMBAHAN

    private ElementType p1a, p1b;
    private ElementType p2a, p2b;

    // =========================
// 🔥 WIN OVERLAY
// =========================
    private Rectangle winOverlay;
    private Text winText;
    private boolean gameOver = false;

    public GameBoard(ElementType p1a, ElementType p1b,
                     ElementType p2a, ElementType p2b) {

        this.p1a = p1a;
        this.p1b = p1b;
        this.p2a = p2a;
        this.p2b = p2b;

        initBoard();
        initUI();
        spawnUnits();
    }

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

    // =========================
    // 🔥 UI STATUS FIX
    // =========================
    private void initUI() {

        // =========================
        // 🔥 STATUS BACKGROUND
        // =========================
        statusBg = new Rectangle();
        statusBg.setHeight(80);
        statusBg.setFill(Color.rgb(20, 20, 20, 0.6));
        statusBg.setArcWidth(30);
        statusBg.setArcHeight(30);

        // =========================
        // 🔥 STATUS TEXT
        // =========================
        statusText = new Text();
        statusText.setFont(new Font("Arial Black", 42));
        statusText.setFill(Color.web("#FFD700"));

        // =========================
        // 🔥 AUTO WIDTH BACKGROUND
        // =========================
        statusText.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            statusBg.setWidth(newVal.getWidth() + 80);
        });

        // =========================
        // 🔥 CENTER POSITION
        // =========================
        statusBg.xProperty().bind(
                widthProperty().subtract(statusBg.widthProperty()).divide(2)
        );

        statusText.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {

            double textWidth = newVal.getWidth();

            statusText.setLayoutX((getWidth() - textWidth) / 2);
            statusBg.setWidth(textWidth + 80);
            statusBg.setLayoutX((getWidth() - statusBg.getWidth()) / 2);
        });

        // posisi Y
        statusBg.setTranslateY(280);
        statusText.setTranslateY(330);

        // awal hidden
        statusBg.setVisible(false);
        statusText.setVisible(false);

        // =========================
        // TAMBAHKAN KE SCENE
        // =========================
        getChildren().addAll(statusBg, statusText);

        // ====================================================
        // 🔥 🔥 WIN OVERLAY (TAMBAHKAN DI SINI 🔥 🔥)
        // ====================================================

        winOverlay = new Rectangle();
        winOverlay.setFill(Color.rgb(0, 0, 0, 0.75));
        winOverlay.widthProperty().bind(widthProperty());
        winOverlay.heightProperty().bind(heightProperty());
        winOverlay.setVisible(false);

        winText = new Text();
        winText.setFont(new Font("Arial Black", 60));
        winText.setFill(Color.GOLD);
        winText.setStroke(Color.BLACK);
        winText.setStrokeWidth(2);
        winText.setVisible(false);

        // center text
        winText.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            winText.setLayoutX((getWidth() - newVal.getWidth()) / 2);
            winText.setLayoutY(getHeight() / 2);
        });

        // 🔥 penting: add AFTER status → supaya di paling atas
        getChildren().addAll(winOverlay, winText);
    }

    private void spawnUnits() {
        spawnBackRow(7, Color.BLUE, true);
        spawnPawns(6, Color.BLUE, true);

        spawnBackRow(0, Color.RED, false);
        spawnPawns(1, Color.RED, false);
    }

    private ElementType getElement(UnitType type, boolean isP1) {

        ElementType e1 = isP1 ? p1a : p2a;
        ElementType e2 = isP1 ? p1b : p2b;

        switch (type) {
            case PAWN:
            case KNIGHT:
                return e1;
            case ROOK:
            case BISHOP:
                return e2;
            case QUEEN:
            case KING:
                return Math.random() < 0.5 ? e1 : e2;
            default:
                return e1;
        }
    }

    private void spawnBackRow(int y, Color color, boolean isPlayerOne) {

        UnitType[] order = {
                UnitType.ROOK, UnitType.KNIGHT, UnitType.BISHOP,
                UnitType.QUEEN, UnitType.KING,
                UnitType.BISHOP, UnitType.KNIGHT, UnitType.ROOK
        };

        for (int x = 0; x < 8; x++) {

            Unit u = new Unit(
                    x, y,
                    order[x],
                    getElement(order[x], isPlayerOne),
                    color
            );

            u.setLayoutX(x * 80);
            u.setLayoutY(y * 80);

            setupClick(u, isPlayerOne);

            units.add(u);
            getChildren().add(u);
        }
    }

    private void spawnPawns(int y, Color color, boolean isPlayerOne) {
        for (int x = 0; x < 8; x++) {

            Unit p = new Unit(
                    x, y,
                    UnitType.PAWN,
                    getElement(UnitType.PAWN, isPlayerOne),
                    color
            );

            p.setLayoutX(x * 80);
            p.setLayoutY(y * 80);

            setupClick(p, isPlayerOne);

            units.add(p);
            getChildren().add(p);
        }
    }

    private void setupClick(Unit unit, boolean isPlayerOne) {
        unit.setOnMouseClicked(e -> {
            if (playerOneTurn == isPlayerOne) {
                // Klik unit sendiri → select unit
                selectUnit(unit);
                e.consume();
            } else {
                // Klik unit musuh → proses sebagai tile click (attack/move target)
                handleTileClick(unit.getX(), unit.getY());
                e.consume();
            }
        });
    }

    private void selectUnit(Unit unit) {

        selectedUnit = unit;
        clearHighlights();
        validMoves.clear();

        List<int[]> moves = unit.getValidMoves(units, 8);

        for (int[] m : moves) {

            // 🔥 SEMUA MOVE BOLEH
            tiles[m[0]][m[1]].highlight();
            validMoves.add(m);
        }
    }

    private void handleTileClick(int x, int y) {

        if (gameOver) return;

        Unit clickedUnit = getUnitAt(x, y);

        // =========================
        // 🔥 SELECT UNIT
        // =========================
        if (selectedUnit == null) {

            if (clickedUnit != null) {

                // 🔵 PLAYER 1 (BLUE)
                if (playerOneTurn && clickedUnit.getFillColor().equals(Color.BLUE)) {
                    selectUnit(clickedUnit);
                }

                // 🔴 PLAYER 2 (RED)
                else if (!playerOneTurn && clickedUnit.getFillColor().equals(Color.RED)) {
                    selectUnit(clickedUnit);
                }
            }

            return;
        }

        // =========================
        // VALIDASI MOVE
        // =========================
        if (!isValidMove(x, y)) return;

        Unit target = getUnitAt(x, y);

        // =========================
// 🔥 ATTACK
// =========================
        if (target != null && selectedUnit.isEnemy(target)) {

            selectedUnit.useSkill(target);
            selectedUnit.move(selectedUnit.getX(), selectedUnit.getY());

            // 🔥 CEK KING MATI SEBELUM DIHAPUS
            boolean targetIsKing = (target.getType() == UnitType.KING);
            Color targetColor = target.getFillColor();

            if (target.isDead()) {
                getChildren().remove(target);
                units.remove(target);

                // 🔥 LANGSUNG CEK WIN KONDISI
                if (targetIsKing) {
                    if (targetColor.equals(Color.RED)) {
                        showWinAnimation("⚔ WHITE MENANG! ⚔");
                    } else {
                        showWinAnimation("⚔ BLACK MENANG! ⚔");
                    }
                    selectedUnit = null;
                    clearHighlights();
                    validMoves.clear();
                    return;  // stop, jangan lanjut
                }
            }

            applyAllTurnEffects();
            playerOneTurn = !playerOneTurn;
            applyCheckDamage();
            updateGameState();

            selectedUnit = null;
            clearHighlights();
            validMoves.clear();

            return;
        }

        // =========================
        // 🔁 GANTI PILIH UNIT (TEMAN)
        // =========================
        if (target != null) {

            if (playerOneTurn && target.getFillColor().equals(Color.BLUE)) {
                selectUnit(target);
                return;
            }

            if (!playerOneTurn && target.getFillColor().equals(Color.RED)) {
                selectUnit(target);
                return;
            }
        }

        // =========================
        // 🚶 MOVE
        // =========================
        animateMove(selectedUnit, x, y);

        // ⚠️ IMPORTANT:
        // JANGAN tambahkan logic lain di sini
        // karena animateMove sudah handle:
        // - applyAllTurnEffects
        // - updateGameState
        // - turn switch
    }

    private void applyAllTurnEffects() {
        for (Unit u : units) {
            u.applyTurnEffect();
        }

        applyCheckDamage();
    }

    private Unit getKing(Color color) {
        for (Unit u : units) {
            if (u.getType() == UnitType.KING && u.getFillColor().equals(color)) {
                return u;
            }
        }
        return null;
    }

    private void applyCheckDamage() {

        // 🔵 WHITE (BLUE)
        if (isKingInCheck(Color.BLUE)) {
            Unit king = getKing(Color.BLUE);
            if (king != null) {
                king.takeDamage(5, null);
            }
        }

        // 🔴 BLACK (RED)
        if (isKingInCheck(Color.RED)) {
            Unit king = getKing(Color.RED);
            if (king != null) {
                king.takeDamage(5, null);
            }
        }
    }

    private void animateMove(Unit unit, int newX, int newY) {

        if (isAnimating) return;
        isAnimating = true;

        double targetX = newX * 80;
        double targetY = newY * 80;

        TranslateTransition tt = new TranslateTransition();
        tt.setNode(unit);
        tt.setDuration(javafx.util.Duration.seconds(0.25));

        tt.setToX(targetX - unit.getLayoutX());
        tt.setToY(targetY - unit.getLayoutY());

        tt.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        tt.setOnFinished(e -> {

            // =========================
            // 🔥 UPDATE POSISI
            // =========================
            unit.move(newX, newY);

            unit.setLayoutX(targetX);
            unit.setLayoutY(targetY);

            unit.setTranslateX(0);
            unit.setTranslateY(0);

            // =========================
            // 🔥 PROMOTION (FIX UTAMA)
            // =========================
            checkPromotion(unit);

            // =========================
            // 🔥 GAME UPDATE
            // =========================
            applyAllTurnEffects();
            updateGameState();
            playerOneTurn = !playerOneTurn;

            // =========================
            // 🔥 RESET
            // =========================
            selectedUnit = null;
            clearHighlights();

            isAnimating = false;
        });

        tt.play();
    }

    private void checkPromotion(Unit unit) {

        if (unit.getType() != UnitType.PAWN) return;

        // 🔵 BLUE (bawah → atas)
        if (unit.isBlue() && unit.getY() == 0) {
            promote(unit);
        }

        // 🔴 RED (atas → bawah)
        if (!unit.isBlue() && unit.getY() == 7) {
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

        newUnit.setLayoutX(pawn.getX() * 80);
        newUnit.setLayoutY(pawn.getY() * 80);

        getChildren().remove(pawn);
        units.remove(pawn);

        setupClick(newUnit, pawn.getFillColor().equals(Color.BLUE));

        units.add(newUnit);
        getChildren().add(newUnit);
    }

    // =========================
    // 🔥 STATUS UPDATE FIX
    // =========================
    private void updateGameState() {

        String message = "";

        // 🔥 1. PRIORITAS PALING ATAS: KING MATI
        if (isKingDead(Color.BLUE)) {
            showWinAnimation("⚔ BLACK MENANG! ⚔");  // ← TAMBAH INI
            return;                                    // ← TAMBAH INI (stop di sini)
        }
        else if (isKingDead(Color.RED)) {
            showWinAnimation("⚔ WHITE MENANG! ⚔");  // ← TAMBAH INI
            return;                                   // ← TAMBAH INI
        }

        // 🔥 2. CHECK / CHECKMATE
        else if (isKingInCheck(Color.BLUE)) {

            message = "WHITE CHECK!";

        }
        else if (isKingInCheck(Color.RED)) {

            message = "BLACK CHECK!";
        }

        // =========================
        // 🔥 KING ALERT (TARO DI SINI)
        // =========================
        Unit blueKing = getKing(Color.BLUE);
        Unit redKing = getKing(Color.RED);

        if (blueKing != null) {
            blueKing.updateKingAlert(isKingInCheck(Color.BLUE));
        }

        if (redKing != null) {
            redKing.updateKingAlert(isKingInCheck(Color.RED));
        }

        // 🔥 3. JIKA TIDAK ADA STATUS
        if (message.isEmpty()) {
            statusBg.setVisible(false);
            statusText.setVisible(false);
            return;
        }

        // 🔥 4. TAMPILKAN TEXT
        statusText.setText(message);

        // 🔥 WARNA
        if (message.contains("MENANG")) {
            statusText.setFill(Color.RED);
        } else if (message.contains("CHECKMATE")) {
            statusText.setFill(Color.ORANGE);
        } else {
            statusText.setFill(Color.web("#FFD700"));
        }

        statusText.setVisible(true);
        statusBg.setVisible(true);

        // 🔥 ANIMASI
        FadeTransition fadeBg = new FadeTransition(Duration.seconds(0.4), statusBg);
        fadeBg.setFromValue(0);
        fadeBg.setToValue(1);
        fadeBg.play();

        FadeTransition fadeText = new FadeTransition(Duration.seconds(0.4), statusText);
        fadeText.setFromValue(0);
        fadeText.setToValue(1);
        fadeText.play();
    }

    private void showWinAnimation(String text) {

        gameOver = true;

        winText.setText(text);
        winOverlay.setVisible(true);
        winText.setVisible(true);

        // =========================
        // 🔥 FADE OVERLAY
        // =========================
        FadeTransition fadeBg = new FadeTransition(Duration.seconds(0.5), winOverlay);
        fadeBg.setFromValue(0);
        fadeBg.setToValue(1);

        // =========================
        // 🔥 TEXT SCALE
        // =========================
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), winText);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);

        // =========================
        // 🔥 FADE TEXT
        // =========================
        FadeTransition fadeText = new FadeTransition(Duration.seconds(0.5), winText);
        fadeText.setFromValue(0);
        fadeText.setToValue(1);

        fadeBg.play();
        scale.play();
        fadeText.play();

        // =========================
        // 🔥 TOMBOL MAIN MENU (muncul setelah animasi selesai)
        // =========================
        fadeText.setOnFinished(e -> showMainMenuButton());
    }

    private void showMainMenuButton() {

        javafx.scene.control.Button btnMenu = new javafx.scene.control.Button("⬅ Kembali ke Main Menu");

        btnMenu.setStyle(
                "-fx-background-color: #FFD700;" +
                        "-fx-text-fill: #1a1a1a;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Arial Black';" +
                        "-fx-padding: 14 30 14 30;" +
                        "-fx-background-radius: 30;" +
                        "-fx-cursor: hand;"
        );

        // hover effect
        btnMenu.setOnMouseEntered(e ->
                btnMenu.setStyle(
                        "-fx-background-color: #FFA500;" +
                                "-fx-text-fill: #1a1a1a;" +
                                "-fx-font-size: 20px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-family: 'Arial Black';" +
                                "-fx-padding: 14 30 14 30;" +
                                "-fx-background-radius: 30;" +
                                "-fx-cursor: hand;"
                )
        );

        btnMenu.setOnMouseExited(e ->
                btnMenu.setStyle(
                        "-fx-background-color: #FFD700;" +
                                "-fx-text-fill: #1a1a1a;" +
                                "-fx-font-size: 20px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-family: 'Arial Black';" +
                                "-fx-padding: 14 30 14 30;" +
                                "-fx-background-radius: 30;" +
                                "-fx-cursor: hand;"
                )
        );

        // center horizontal
        btnMenu.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            btnMenu.setLayoutX((getWidth() - newVal.getWidth()) / 2);
        });

        // posisi Y (di bawah winText)
        btnMenu.setLayoutY(getHeight() / 2 + 60);

        // =========================
        // 🔥 ACTION: kembali ke main menu
        // =========================
        btnMenu.setOnAction(e -> goToMainMenu());

        // fade in tombol
        btnMenu.setOpacity(0);
        getChildren().add(btnMenu);

        FadeTransition fadeBtn = new FadeTransition(Duration.seconds(0.5), btnMenu);
        fadeBtn.setFromValue(0);
        fadeBtn.setToValue(1);
        fadeBtn.play();
    }

    private void goToMainMenu() {
        // Ambil Stage dari scene saat ini
        javafx.stage.Stage stage =
                (javafx.stage.Stage) getScene().getWindow();

        // =========================
        // 🔥 GANTI SESUAI CLASS MAIN MENU KAMU
        // =========================
        // Contoh jika main menu kamu adalah class MainMenuScene / MainMenu / dll:
        //
        // javafx.scene.Scene menuScene = new javafx.scene.Scene(new MainMenu(), 640, 640);
        // stage.setScene(menuScene);
        //
        // Jika pakai SceneManager / SceneController:
        // SceneManager.getInstance().showMainMenu(stage);

        // ⚠️ SESUAIKAN BARIS INI DENGAN STRUKTUR PROJECT KAMU:
        javafx.scene.Scene menuScene = new javafx.scene.Scene(
                new com.game.MainMenu(), 640, 640  // ← ganti com.game.ui.MainMenu dengan class main menu kamu
        );
        stage.setScene(menuScene);
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

    private boolean isKingDead(Color color) {
        for (Unit u : units) {
            if (u.getType() == UnitType.KING && u.getFillColor().equals(color)) {
                return u.isDead();
            }
        }
        return false;
    }

    private boolean isSafeMove(Unit unit, int x, int y) {
        return true;
    }

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