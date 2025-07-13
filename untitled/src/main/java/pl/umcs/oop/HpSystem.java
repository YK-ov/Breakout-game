package pl.umcs.oop;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class HpSystem extends GraphicsItem {
    int hp;
    private final int maxHp;

    public HpSystem(int hp) {
        this.hp = hp;
        this.maxHp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void decreaseHp() {
        hp = Math.max(0, hp - 1);
    }

    @Override
    public void draw(GraphicsContext gc) {
        Image heart = new Image("health.jpg");
        for (int i = 0; i < hp; i++) {
            gc.drawImage(heart, 20 + i * 40, 20, 32, 32);
        }

        gc.setFill(Color.WHITE);
        gc.fillText("HP: " + hp, 20, 70);
    }
}