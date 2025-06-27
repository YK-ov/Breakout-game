package pl.umcs.oop;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class GameCanvas extends Canvas {
    private javafx.scene.canvas.GraphicsContext canvas;
    private Paddle paddle;

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
}