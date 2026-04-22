package com.game.entity;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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

    private Circle body;
    private Rectangle hpBar;

    private int burnTurns = 0;
    private int debuffTurns = 0;
    private int shieldTurns = 0;

    private boolean hasMoved = false;

    private Random random = new Random();

    public Unit(int x, int y, UnitType type, ElementType element, Color color) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.element = element;

        switch (type) {
            case KING: maxHp = 150; attack = 30; break;
            case QUEEN: maxHp = 120; attack = 40; break;
            case ROOK: maxHp = 180; attack = 20; break;
            case BISHOP: maxHp = 100; attack = 35; break;
            case KNIGHT: maxHp = 110; attack = 45; break;
            case PAWN: maxHp = 80; attack = 15; break;
        }

        hp = maxHp;

        body = new Circle(20);
        body.setFill(color);

        Rectangle hpBg = new Rectangle(40, 5);
        hpBg.setFill(Color.GRAY);
        hpBg.setTranslateY(-30);

        hpBar = new Rectangle(40, 5);
        hpBar.setFill(Color.LIMEGREEN);
        hpBar.setTranslateY(-30);

        getChildren().addAll(hpBg, hpBar, body);
    }

    // =========================
    // MOVEMENT SYSTEM (FIXED)
    // =========================
    public List<int[]> getValidMoves(List<Unit> allUnits, int size) {

        List<int[]> moves = new ArrayList<>();

        switch (type) {

            case KING:
                addMove(moves, allUnits, x+1,y,size);
                addMove(moves, allUnits, x-1,y,size);
                addMove(moves, allUnits, x,y+1,size);
                addMove(moves, allUnits, x,y-1,size);
                addMove(moves, allUnits, x+1,y+1,size);
                addMove(moves, allUnits, x-1,y-1,size);
                addMove(moves, allUnits, x+1,y-1,size);
                addMove(moves, allUnits, x-1,y+1,size);
                break;

            case ROOK:
                addLineMoves(moves, allUnits, 1,0,size);
                addLineMoves(moves, allUnits,-1,0,size);
                addLineMoves(moves, allUnits,0,1,size);
                addLineMoves(moves, allUnits,0,-1,size);
                break;

            case BISHOP:
                addLineMoves(moves, allUnits,1,1,size);
                addLineMoves(moves, allUnits,-1,-1,size);
                addLineMoves(moves, allUnits,1,-1,size);
                addLineMoves(moves, allUnits,-1,1,size);
                break;

            case QUEEN:
                addLineMoves(moves, allUnits,1,0,size);
                addLineMoves(moves, allUnits,-1,0,size);
                addLineMoves(moves, allUnits,0,1,size);
                addLineMoves(moves, allUnits,0,-1,size);
                addLineMoves(moves, allUnits,1,1,size);
                addLineMoves(moves, allUnits,-1,-1,size);
                addLineMoves(moves, allUnits,1,-1,size);
                addLineMoves(moves, allUnits,-1,1,size);
                break;

            case KNIGHT:
                addMove(moves, allUnits,x+2,y+1,size);
                addMove(moves, allUnits,x+2,y-1,size);
                addMove(moves, allUnits,x-2,y+1,size);
                addMove(moves, allUnits,x-2,y-1,size);
                addMove(moves, allUnits,x+1,y+2,size);
                addMove(moves, allUnits,x+1,y-2,size);
                addMove(moves, allUnits,x-1,y+2,size);
                addMove(moves, allUnits,x-1,y-2,size);
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
        checkPawnCapture(moves, units, x+1, y+dir, size);
        checkPawnCapture(moves, units, x-1, y+dir, size);
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

    private boolean isBlue() {
        return body.getFill().equals(Color.BLUE);
    }

    public boolean isEnemy(Unit other) {
        return !this.getFillColor().equals(other.getFillColor());
    }

    public void move(int newX, int newY) {
        x = newX;
        y = newY;
        hasMoved = true;

        setTranslateX(newX * 80);
        setTranslateY(newY * 80);
    }

    public void moveSimulated(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void applyTurnEffect() {

        // 🔥 Burn
        if (burnTurns > 0) {
            takeDamage(5);
            burnTurns--;
            body.setStroke(Color.ORANGE);
            body.setStrokeWidth(3);
        }

        // 💧 Debuff
        if (debuffTurns > 0) {
            debuffTurns--;
            body.setStroke(Color.BLUE);
            body.setStrokeWidth(3);
        }

        // 🌍 Shield
        if (shieldTurns > 0) {
            shieldTurns--;
            body.setStroke(Color.GREEN);
            body.setStrokeWidth(3);
        }

        // reset visual
        if (burnTurns == 0 && debuffTurns == 0 && shieldTurns == 0) {
            body.setStroke(null);
        }
    }

    public void useSkill(Unit target) {

        int damage = attack;

        switch (element) {

            case FIRE:
                target.takeDamage(damage + 10);
                break;

            case WATER:
                target.takeDamage(damage + 5);
                // (opsional: slow / debuff nanti bisa ditambah)
                break;

            case EARTH:
                target.takeDamage(damage);
                this.hp = Math.min(this.hp + 10, this.maxHp); // heal
                break;

            case LIGHTNING:
                target.takeDamage(damage + 15);
                break;

            default:
                target.takeDamage(damage);
        }
    }

    public void takeDamage(int dmg) {
        hp -= dmg;

        if (hp < 0) hp = 0;

        // update HP bar
        double ratio = (double) hp / maxHp;
        hpBar.setWidth(40 * ratio);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public UnitType getType() { return type; }
    public ElementType getElement() { return element; }

    public Color getFillColor() {
        return (Color) body.getFill();
    }

    public boolean isDead() {
        return hp <= 0;
    }
}