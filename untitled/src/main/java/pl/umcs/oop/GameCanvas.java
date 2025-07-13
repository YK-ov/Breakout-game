package pl.umcs.oop;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameCanvas extends Canvas {
    private javafx.scene.canvas.GraphicsContext canvas;
    private Paddle paddle;
    private List<Brick> bricks = new ArrayList<>();
    private HpSystem hpSystem;

    public GameCanvas(double width, double height) {
        super(width, height);
        this.canvas = this.getGraphicsContext2D();

        this.setOnMouseMoved(event -> {
            if (paddle != null) {
                paddle.setPositionByMouseX(event.getX());
                redraw();
            }
        });
    }

    public void draw() {
        canvas.setFill(Paint.valueOf("black"));
        canvas.fillRect(0, 0, getWidth(), getHeight());

        for (Brick brick : bricks) {
            brick.draw(canvas);
        }

        if (hpSystem != null) {
            hpSystem.draw(canvas);
        }
    }

    public void setHPSystem(HpSystem hpSystem) {
        this.hpSystem = hpSystem;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public void redraw() {
        draw();
        if (paddle != null) {
            paddle.draw(canvas);
        }
    }

    public void loadLevel() {
        bricks.clear();

        int rows = 20;
        int cols = 10;

        int numberOfColors = 6;
        Random random = new Random();

        List<Color> rowColors = new ArrayList<>();
        for (int i = 0; i < numberOfColors; i++) {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            rowColors.add(Color.rgb(r, g, b));
        }

        for (int row = 2; row <= 7; row++) {
            Color color = rowColors.get(row - 2);
            for (int col = 0; col < cols; col++) {
                Brick brick = new Brick(col, row, color, rows, cols);
                bricks.add(brick);
            }
        }
    }

    public List<Brick> getBricks() {
        return bricks;
    }
}