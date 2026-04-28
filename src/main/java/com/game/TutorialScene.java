package com.game;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public class TutorialScene extends VBox {

    private int page = 0;

    private Text title;
    private Text content;

    private Button prevBtn;
    private Button nextBtn;
    private Button finishBtn;

    public TutorialScene() {

        setSpacing(20);
        setAlignment(Pos.CENTER);

        title = new Text();
        content = new Text();
        content.setWrappingWidth(500);

        prevBtn = new Button("PREV");
        nextBtn = new Button("NEXT");
        finishBtn = new Button("FINISH");

        prevBtn.setOnAction(e -> {
            page--;
            updatePage();
        });

        nextBtn.setOnAction(e -> {
            page++;
            updatePage();
        });

        finishBtn.setOnAction(e -> {
            MainApp.setTutorialCompleted(true);
            MainApp.showMainMenu();
        });

        getChildren().addAll(title, content, prevBtn, nextBtn, finishBtn);

        updatePage();
    }

    // =========================
    // UPDATE PAGE
    // =========================
    private void updatePage() {

        // reset visibility
        prevBtn.setVisible(true);
        nextBtn.setVisible(true);
        finishBtn.setVisible(false);

        // =========================
        // PAGE 0 - CARA MAIN
        // =========================
        if (page == 0) {
            title.setText("TUTORIAL - CARA MAIN");

            content.setText(
                    "- Klik unit untuk memilih\n" +
                            "- Klik tile hijau untuk bergerak\n" +
                            "- Klik musuh untuk menyerang\n" +
                            "- Giliran bergantian tiap pemain"
            );

            prevBtn.setVisible(false);
        }

        // =========================
        // PAGE 1 - CATUR
        // =========================
        else if (page == 1) {
            title.setText("TUTORIAL - ATURAN CATUR");

            content.setText(
                    "KING: 1 langkah semua arah\n" +
                            "QUEEN: bebas semua arah\n" +
                            "ROOK: lurus\n" +
                            "BISHOP: diagonal\n" +
                            "KNIGHT: gerakan L\n\n" +
                            "PAWN:\n" +
                            "- maju 1 langkah\n" +
                            "- bisa 2 langkah di awal\n" +
                            "- makan diagonal\n" +
                            "- jadi QUEEN jika sampai ujung"
            );
        }

        // =========================
        // PAGE 2 - ELEMENT
        // =========================
        else if (page == 2) {
            title.setText("TUTORIAL - ELEMENT SYSTEM");

            content.setText(
                    "Setiap player memilih 2 element.\n" +
                            "Setiap unit memiliki element berbeda.\n\n" +

                            "FIRE:\nDamage +10 + Burn\n\n" +

                            "WATER:\nDamage +5 + Weakening\n\n" +

                            "EARTH:\nHeal +10 + Shield\n\n" +

                            "LIGHTNING:\nDamage +15 (Burst)"
            );
        }

        // =========================
        // PAGE 3 - RULE & TIPS
        // =========================
        else if (page == 3) {
            title.setText("TUTORIAL - RULE & TIPS");

            content.setText(
                    "RULE:\n" +
                            "- Tidak bisa bergerak jika KING dalam bahaya\n" +
                            "- CHECK jika KING diserang\n" +
                            "- CHECKMATE jika tidak bisa selamat\n\n" +

                            "TIPS:\n" +
                            "- Lindungi KING kamu\n" +
                            "- Kombinasikan element dengan bijak\n" +
                            "- Jangan asal menyerang"
            );

            nextBtn.setVisible(false);
            finishBtn.setVisible(true);
        }
    }
}