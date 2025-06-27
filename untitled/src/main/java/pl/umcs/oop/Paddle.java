package pl.umcs.oop;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Paddle extends GraphicsItem {
    public Paddle() {
        this.width = 200.0;
        this.height = 20.0;

        this.x = (canvasWidth - this.width) / 2;
        this.y = canvasHeight - 100;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Paint.valueOf("blue"));
        gc.fillRect(this.x, this.y, this.width, this.height);
    }

    public void setPositionByMouseX(double mouseX) {
        double newX = mouseX - (this.width / 2);

        if (newX < 0) {
            newX = 0;
        } else if (newX + this.width > canvasWidth) {
            newX = canvasWidth - this.width;
        }

        this.x = newX;
    }

    public void setPosition(javafx.geometry.Point2D pos) {
        this.x = pos.getX() - this.width / 2;
        this.y = pos.getY() - this.height / 2;
    }

    public void moveLeft() {
        this.x -= 15;

        if (this.x < 0) {
            this.x = 0;
        }
    }

    public void moveRight() {
        this.x += 15;

        if (this.x + this.width > canvasWidth) {
            this.x = canvasWidth - this.width;
        }
    }
}