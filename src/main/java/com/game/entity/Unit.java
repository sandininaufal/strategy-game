package com.game.entity;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Unit extends StackPane {

    private int x, y;
    private int maxHp;
    private int hp;
    private int attack;

    private UnitType type;
    private ElementType element;

    private ImageView body;
    private Rectangle hpBar;

    private int burnTurns = 0;
    private int debuffTurns = 0;
    private int shieldTurns = 0;

    private boolean hasMoved = false;
    private boolean isPlayerOne;

    private Random random = new Random();

    public Unit(int x, int y, UnitType type, ElementType element, Color color) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.element = element;
        this.color = color;

        switch (type) {
            case KING:
                maxHp = 150;
                attack = 30;
                break;
            case QUEEN:
                maxHp = 120;
                attack = 40;
                break;
            case ROOK:
                maxHp = 180;
                attack = 20;
                break;
            case BISHOP:
                maxHp = 100;
                attack = 35;
                break;
            case KNIGHT:
                maxHp = 110;
                attack = 45;
                break;
            case PAWN:
                maxHp = 80;
                attack = 15;
                break;
        }

        hp = maxHp;

        body = getPieceImage(type, color);

        Rectangle hpBg = new Rectangle(40, 5);
        hpBg.setFill(Color.GRAY);
        hpBg.setTranslateY(-30);

        hpBar = new Rectangle(40, 5);
        hpBar.setFill(Color.LIMEGREEN);
        hpBar.setTranslateY(-30);

        getChildren().addAll(hpBg, hpBar, body);
    }

    private ImageView getPieceImage(UnitType type, Color color) {

        String prefix = color.equals(Color.BLUE) ? "white_" : "black_";

        String name = "";

        switch (type) {
            case KING:
                name = "king";
                break;
            case QUEEN:
                name = "queen";
                break;
            case ROOK:
                name = "rook";
                break;
            case BISHOP:
                name = "bishop";
                break;
            case KNIGHT:
                name = "knight";
                break;
            case PAWN:
                name = "pawn";
                break;
        }

        String path = "/images/" + prefix + name + ".png";

        Image img = new Image(getClass().getResourceAsStream(path));

        ImageView iv = new ImageView(img);
        iv.setFitWidth(50);
        iv.setFitHeight(50);

        return iv;
    }

    // =========================
    // MOVEMENT SYSTEM (FIXED)
    // =========================
    public List<int[]> getValidMoves(List<Unit> allUnits, int size) {

        List<int[]> moves = new ArrayList<>();

        switch (type) {

            case KING:
                addMove(moves, allUnits, x + 1, y, size);
                addMove(moves, allUnits, x - 1, y, size);
                addMove(moves, allUnits, x, y + 1, size);
                addMove(moves, allUnits, x, y - 1, size);
                addMove(moves, allUnits, x + 1, y + 1, size);
                addMove(moves, allUnits, x - 1, y - 1, size);
                addMove(moves, allUnits, x + 1, y - 1, size);
                addMove(moves, allUnits, x - 1, y + 1, size);
                break;

            case ROOK:
                addLineMoves(moves, allUnits, 1, 0, size);
                addLineMoves(moves, allUnits, -1, 0, size);
                addLineMoves(moves, allUnits, 0, 1, size);
                addLineMoves(moves, allUnits, 0, -1, size);
                break;

            case BISHOP:
                addLineMoves(moves, allUnits, 1, 1, size);
                addLineMoves(moves, allUnits, -1, -1, size);
                addLineMoves(moves, allUnits, 1, -1, size);
                addLineMoves(moves, allUnits, -1, 1, size);
                break;

            case QUEEN:
                addLineMoves(moves, allUnits, 1, 0, size);
                addLineMoves(moves, allUnits, -1, 0, size);
                addLineMoves(moves, allUnits, 0, 1, size);
                addLineMoves(moves, allUnits, 0, -1, size);
                addLineMoves(moves, allUnits, 1, 1, size);
                addLineMoves(moves, allUnits, -1, -1, size);
                addLineMoves(moves, allUnits, 1, -1, size);
                addLineMoves(moves, allUnits, -1, 1, size);
                break;

            case KNIGHT:
                addMove(moves, allUnits, x + 2, y + 1, size);
                addMove(moves, allUnits, x + 2, y - 1, size);
                addMove(moves, allUnits, x - 2, y + 1, size);
                addMove(moves, allUnits, x - 2, y - 1, size);
                addMove(moves, allUnits, x + 1, y + 2, size);
                addMove(moves, allUnits, x + 1, y - 2, size);
                addMove(moves, allUnits, x - 1, y + 2, size);
                addMove(moves, allUnits, x - 1, y - 2, size);
                break;

            case PAWN:
                handlePawnMoves(moves, allUnits, size);
                break;
        }

        return moves;
    }

    // =========================
    // PAWN FIX
    // =========================
    private void handlePawnMoves(List<int[]> moves, List<Unit> units, int size) {

        int dir = isBlue() ? -1 : 1;

        // maju 1
        if (getUnitAt(units, x, y + dir) == null) {
            addMoveRaw(moves, x, y + dir, size);

            // maju 2
            if (!hasMoved && getUnitAt(units, x, y + 2 * dir) == null) {
                addMoveRaw(moves, x, y + 2 * dir, size);
            }
        }

        // makan diagonal
        checkPawnCapture(moves, units, x + 1, y + dir, size);
        checkPawnCapture(moves, units, x - 1, y + dir, size);
    }

    private void checkPawnCapture(List<int[]> moves, List<Unit> units, int tx, int ty, int size) {
        Unit target = getUnitAt(units, tx, ty);
        if (target != null && isEnemy(target)) {
            addMoveRaw(moves, tx, ty, size);
        }
    }

    // =========================
    // LINE MOVES
    // =========================
    private void addLineMoves(List<int[]> moves, List<Unit> units, int dx, int dy, int size) {

        int cx = x + dx;
        int cy = y + dy;

        while (cx >= 0 && cx < size && cy >= 0 && cy < size) {

            Unit target = getUnitAt(units, cx, cy);

            if (target == null) {
                moves.add(new int[]{cx, cy});
            } else {
                if (isEnemy(target)) {
                    moves.add(new int[]{cx, cy});
                }
                break;
            }

            cx += dx;
            cy += dy;
        }
    }

    private void addMove(List<int[]> moves, List<Unit> units, int nx, int ny, int size) {

        if (nx < 0 || ny < 0 || nx >= size || ny >= size) return;

        Unit target = getUnitAt(units, nx, ny);

        if (target == null || isEnemy(target)) {
            moves.add(new int[]{nx, ny});
        }
    }

    private void addMoveRaw(List<int[]> moves, int nx, int ny, int size) {
        if (nx >= 0 && ny >= 0 && nx < size && ny < size) {
            moves.add(new int[]{nx, ny});
        }
    }

    private Unit getUnitAt(List<Unit> units, int x, int y) {
        for (Unit u : units) {
            if (u.x == x && u.y == y) return u;
        }
        return null;
    }

    public boolean isBlue() {
        return color.equals(Color.BLUE);
    }

    public boolean isEnemy(Unit other) {
        return !this.getFillColor().equals(other.getFillColor());
    }

    public void move(int newX, int newY) {
        x = newX;
        y = newY;
        hasMoved = true;

        setLayoutX(newX * 80);
        setLayoutY(newY * 80);
    }

    public void moveSimulated(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void applyTurnEffect() {

        // 🔥 Burn
        if (burnTurns > 0) {
            takeDamage(5, ElementType.FIRE);
            burnTurns--;
            setStyle("-fx-effect: dropshadow(gaussian, orange, 15, 0.7, 0, 0);");
        }

        // 💧 Debuff
        else if (debuffTurns > 0) {
            debuffTurns--;
            setStyle("-fx-effect: dropshadow(gaussian, blue, 15, 0.7, 0, 0);");
        }

        // 🌍 Shield
        else if (shieldTurns > 0) {
            shieldTurns--;
            setStyle("-fx-effect: dropshadow(gaussian, green, 15, 0.7, 0, 0);");
        }

        // reset
        else {
            setStyle(null);
        }
    }

    public void useSkill(Unit target) {

        int damage = attack;

        switch (element) {

            // 🔥 FIRE (DPS + Burn)
            case FIRE:
                target.takeDamage(damage + 10, ElementType.FIRE);

                // burn effect (optional logic)
                target.burnTurns = 2;
                break;

            // 💧 WATER (Control / Debuff)
            case WATER:
                target.takeDamage(damage + 5, ElementType.WATER);

                // debuff effect
                target.debuffTurns = 1;
                break;

            // 🌱 EARTH (Sustain / Heal)
            case EARTH:
                target.takeDamage(damage, ElementType.EARTH);

                // heal diri sendiri
                this.hp = Math.min(this.hp + 10, this.maxHp);

                // tampilkan efek heal di diri sendiri
                this.playElementEffect(ElementType.EARTH);
                break;

            // ⚡ LIGHTNING (Burst)
            case LIGHTNING:
                target.takeDamage(damage + 15, ElementType.LIGHTNING);

                // OPTIONAL: chance double hit
                if (random.nextDouble() < 0.25) {
                    target.takeDamage(damage, ElementType.LIGHTNING);
                }
                break;

            default:
                target.takeDamage(damage, element);
        }
    }

    // =========================
// 🔥 KING ALERT SYSTEM
// =========================
    public void updateKingAlert(boolean inCheck) {

        if (type != UnitType.KING) return;

        double hpRatio = (double) hp / maxHp;

        // reset dulu
        this.setEffect(null);
        this.setOpacity(1);

        // =========================
        // 🔴 CRITICAL HP (< 25%)
        // =========================
        if (hpRatio < 0.25) {

            javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
            glow.setColor(javafx.scene.paint.Color.RED);
            glow.setRadius(40);

            this.setEffect(glow);

            // blink cepat
            javafx.animation.FadeTransition blink =
                    new javafx.animation.FadeTransition(javafx.util.Duration.millis(150), this);

            blink.setFromValue(1);
            blink.setToValue(0.3);
            blink.setCycleCount(6);
            blink.setAutoReverse(true);
            blink.play();

            return;
        }

        // =========================
        // 🟠 LOW HP (< 50%)
        // =========================
        if (hpRatio < 0.5) {

            javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
            glow.setColor(javafx.scene.paint.Color.ORANGE);
            glow.setRadius(30);

            this.setEffect(glow);
        }

        // =========================
        // 🔥 CHECK STATE
        // =========================
        if (inCheck) {

            javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
            glow.setColor(javafx.scene.paint.Color.RED);
            glow.setRadius(35);

            this.setEffect(glow);

            // pulse (membesar-kecil)
            javafx.animation.ScaleTransition pulse =
                    new javafx.animation.ScaleTransition(javafx.util.Duration.millis(300), this);

            pulse.setToX(1.15);
            pulse.setToY(1.15);
            pulse.setCycleCount(4);
            pulse.setAutoReverse(true);
            pulse.play();
        }
    }

    private void playElementEffect(ElementType element) {

        if (element == null) return; // 🔥 TAMBAH INI

        javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
        glow.setRadius(30);

        switch (element) {
            case FIRE:
                glow.setColor(javafx.scene.paint.Color.ORANGE);
                break;
            case WATER:
                glow.setColor(javafx.scene.paint.Color.DEEPSKYBLUE);
                break;
            case EARTH:
                glow.setColor(javafx.scene.paint.Color.LIMEGREEN);
                break;
            case LIGHTNING:
                glow.setColor(javafx.scene.paint.Color.YELLOW);
                break;
        }

        this.setEffect(glow);

        javafx.animation.PauseTransition reset =
                new javafx.animation.PauseTransition(javafx.util.Duration.millis(250));

        reset.setOnFinished(e -> this.setEffect(null));
        reset.play();
    }

    private void showDamageNumber(int dmg) {

        javafx.scene.text.Text dmgText =
                new javafx.scene.text.Text("-" + dmg);

        dmgText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        dmgText.setFill(javafx.scene.paint.Color.RED);

        // posisi awal (di atas unit)
        dmgText.setTranslateY(-40);

        getChildren().add(dmgText);

        // =========================
        // 🔥 ANIMASI NAIK
        // =========================
        javafx.animation.TranslateTransition moveUp =
                new javafx.animation.TranslateTransition(javafx.util.Duration.millis(600), dmgText);

        moveUp.setByY(-30);

        // =========================
        // 🔥 FADE OUT
        // =========================
        javafx.animation.FadeTransition fade =
                new javafx.animation.FadeTransition(javafx.util.Duration.millis(600), dmgText);

        fade.setFromValue(1);
        fade.setToValue(0);

        // =========================
        // 🔥 HAPUS SETELAH SELESAI
        // =========================
        fade.setOnFinished(e -> getChildren().remove(dmgText));

        moveUp.play();
        fade.play();
    }

    public void takeDamage(int dmg, ElementType element) {

        // 🔥 NONAKTIFKAN CLICK SEMENTARA (TARUH PALING ATAS)
        this.setMouseTransparent(true);

        javafx.animation.PauseTransition pt =
                new javafx.animation.PauseTransition(javafx.util.Duration.millis(200));

        pt.setOnFinished(e -> this.setMouseTransparent(false));
        pt.play();

        // =========================
        // 🔥 DAMAGE LOGIC
        // =========================
        hp -= dmg;
        if (hp < 0) hp = 0;

        double ratio = (double) hp / maxHp;
        hpBar.setWidth(40 * ratio);

        // 🔥 DAMAGE NUMBER
        showDamageNumber(dmg);

        // =========================
        // 🔥 ELEMENT EFFECT
        // =========================
        playElementEffect(element);

        // =========================
        // 🔥 SHAKE
        // =========================
        javafx.animation.TranslateTransition shake =
                new javafx.animation.TranslateTransition(javafx.util.Duration.millis(80), this);

        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();

        // =========================
        // 🔥 SCALE
        // =========================
        javafx.animation.ScaleTransition scale =
                new javafx.animation.ScaleTransition(javafx.util.Duration.millis(120), this);

        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public UnitType getType() {
        return type;
    }

    public ElementType getElement() {
        return element;
    }

    private Color color;

    public Color getFillColor() {
        return color;
    }

    public boolean isDead() {
        return hp <= 0;
    }
}