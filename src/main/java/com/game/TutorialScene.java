package com.game;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public class TutorialScene extends VBox {

    public TutorialScene() {

        setSpacing(20);
        setAlignment(Pos.CENTER);

        Text tutorialText = new Text(
                "=== TUTORIAL GAME ===\n\n" +

                        "🎮 CARA MAIN:\n" +
                        "- Klik unit untuk memilih\n" +
                        "- Klik tile hijau untuk bergerak\n" +
                        "- Klik musuh untuk menyerang\n" +
                        "- Giliran bergantian tiap pemain\n\n" +

                        "♟️ ATURAN CATUR:\n" +
                        "- KING: 1 langkah semua arah\n" +
                        "- QUEEN: bebas semua arah\n" +
                        "- ROOK: lurus\n" +
                        "- BISHOP: diagonal\n" +
                        "- KNIGHT: gerakan L (lompat)\n" +
                        "- PAWN:\n" +
                        "  • maju 1 langkah\n" +
                        "  • bisa 2 langkah di awal\n" +
                        "  • makan diagonal\n" +
                        "  • jadi QUEEN jika sampai ujung\n\n" +

                        "⚠️ RULE PENTING:\n" +
                        "- Tidak bisa gerak kalau KING dalam bahaya\n" +
                        "- Jika KING diserang → CHECK\n" +
                        "- Jika tidak bisa selamat → CHECKMATE\n\n" +

                        "🔥 ELEMENT SYSTEM:\n" +
                        "- FIRE 🔥  : Damage +10 + Burn (damage tiap turn)\n" +
                        "- WATER 💧 : Damage +5 + Weakening (attack musuh turun)\n" +
                        "- EARTH 🌍 : Heal + Shield (tahan damage)\n" +
                        "- LIGHTNING ⚡ : Damage tinggi + chance double attack\n\n" +

                        "💡 TIPS:\n" +
                        "- Lindungi KING kamu!\n" +
                        "- Gunakan element dengan strategi\n" +
                        "- Jangan asal menyerang\n"
        );

        tutorialText.setWrappingWidth(500);

        Button finishBtn = new Button("SELESAI");

        finishBtn.setOnAction(e -> {
            MainApp.setTutorialCompleted(true);
            MainApp.showMainMenu();
        });

        getChildren().addAll(tutorialText, finishBtn);
    }
}