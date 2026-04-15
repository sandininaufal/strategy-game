package com.game.entity;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Unit extends StackPane {

    private int x, y;
    private int maxHp;
    private int hp;
    private int attack;

    private UnitType type;

    private Circle body;
    private Rectangle hpBarBg;
    private Rectangle hpBar;

    public Unit(int x, int y, UnitType type, Color color) {
        this.x = x;
        this.y = y;
        this.type = type;

        // SET STAT BERDASARKAN CLASS
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

        // BODY
        body = new Circle(20);
        body.setFill(color);

        // HP BAR
        hpBarBg = new Rectangle(40, 5);
        hpBarBg.setFill(Color.GRAY);
        hpBarBg.setTranslateY(-30);

        hpBar = new Rectangle(40, 5);
        hpBar.setFill(Color.LIMEGREEN);
        hpBar.setTranslateY(-30);

        getChildren().addAll(hpBarBg, hpBar, body);
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;

        setTranslateX(newX * 80);
        setTranslateY(newY * 80);
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;

        updateHpBar();
        System.out.println(type + " HP: " + hp);
    }

    private void updateHpBar() {
        double ratio = (double) hp / maxHp;
        hpBar.setWidth(40 * ratio);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int getAttack() {
        return attack;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}