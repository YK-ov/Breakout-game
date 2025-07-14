package pl.umcs.oop;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class HpSystem extends GraphicsItem {
    int hp;
    private final int maxHp;
    private final Rectangle2D yesButtonBounds = new Rectangle2D(700, 650, 100, 100);
    private final Rectangle2D exitButtonBounds = new Rectangle2D(1200, 650, 100, 100);

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

    public void resetHp() {
        this.hp = maxHp;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Image heart = new Image("health.jpg");
        for (int i = 0; i < hp; i++) {
            gc.drawImage(heart, 20 + i * 40, 20, 32, 32);
        }

        gc.setFill(Color.WHITE);
        Font normalFont = new Font("Arial", 20);
        gc.setFont(normalFont);
        gc.fillText("HP: " + hp, 20, 70);
    }

    public void drawGameOver(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        Font gameOverFont = new Font("Arial", 100);
        gc.setFont(gameOverFont);
        gc.fillText("Game Over", 700, 200);
        gc.fillText("Restart the game?", 565, 500);

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(yesButtonBounds.getMinX(), yesButtonBounds.getMinY(),yesButtonBounds.getWidth(), yesButtonBounds.getHeight());
        Font buttonFont = new Font("Arial", 50);
        gc.setFont(buttonFont);


        gc.fillRect(exitButtonBounds.getMinX(), exitButtonBounds.getMinY(), exitButtonBounds.getWidth(), exitButtonBounds.getHeight());

        gc.setFill(Color.GREEN);
        gc.fillText("Exit", exitButtonBounds.getMinX() + 10, exitButtonBounds.getMinY());
        gc.fillText( "Yes", yesButtonBounds.getMinX(), yesButtonBounds.getMinY());
    }

    public Rectangle2D getYesButtonBounds() {
        return yesButtonBounds;
    }

    public Rectangle2D getExitButtonBounds() {
        return exitButtonBounds;
    }

}