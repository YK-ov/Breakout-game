package pl.umcs.oop;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends Canvas {
    private javafx.scene.canvas.GraphicsContext canvas;
    private Paddle paddle;
    private List<Brick> bricks = new ArrayList<>();

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
        // NAJPIERW czarne tło
        canvas.setFill(Paint.valueOf("black"));
        canvas.fillRect(0, 0, getWidth(), getHeight());

        // POTEM cegły na czarnym tle
        for (Brick brick : bricks) {
            brick.draw(canvas);
        }
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

        List<Color> rowColors = List.of(
                Color.RED,
                Color.ORANGE,
                Color.PINK,
                Color.AQUAMARINE,
                Color.AQUA,
                Color.BEIGE
        );

        // Naprawiłem też kolejność parametrów w konstruktorze Brick
        for (int row = 2; row <= 7; row++) {
            Color color = rowColors.get(row - 2);
            for (int col = 0; col < cols; col++) {
                Brick brick = new Brick(col, row, color, rows, cols); // poprawiona kolejność
                bricks.add(brick);
            }
        }
    }

    // Dodaję getter dla cegieł - przyda się do kolizji
    public List<Brick> getBricks() {
        return bricks;
    }
}