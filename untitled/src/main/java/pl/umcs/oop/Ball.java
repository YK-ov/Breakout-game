package pl.umcs.oop;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends GraphicsItem {
    private Point2D moveVector;
    private double velocity;

    public Ball() {
        this.width = 20.0;
        this.height = 20.0;

        double angle = Math.toRadians(-45); //-45
        this.moveVector = new Point2D(Math.cos(angle), Math.sin(angle));
        this.velocity = 15.0;
    }

    public void setPosition(Point2D pos) {
        this.x = pos.getX() - this.width / 2;
        this.y = pos.getY() - this.height / 2;
    }

    public void updatePosition() {
        this.x += moveVector.getX() * velocity;
        this.y += moveVector.getY() * velocity;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(this.x, this.y, this.width, this.height);
    }

    public Point2D getMoveVector() {
        return moveVector;
    }

    public void setMoveVector(Point2D moveVector) {
        this.moveVector = moveVector.normalize();
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void bounceHorizontal() {
        this.moveVector = new Point2D(-moveVector.getX(), moveVector.getY());
    }

    public void bounceVertical() {
        this.moveVector = new Point2D(moveVector.getX(), -moveVector.getY());
    }

    public Point2D getTopPoint() {
        return new Point2D(this.x + this.width / 2, this.y);
    }

    public Point2D getBottomPoint() {
        return new Point2D(this.x + this.width / 2, this.y + this.height);
    }

    public Point2D getLeftPoint() {
        return new Point2D(this.x, this.y + this.height / 2);
    }

    public Point2D getRightPoint() {
        return new Point2D(this.x + this.width, this.y + this.height / 2);
    }

    public void bounceFromPaddle(double hitPosition) {
        hitPosition = Math.max(-1.0, Math.min(1.0, hitPosition));

        double maxAngle = Math.PI / 3;
        double angle = hitPosition * maxAngle;

        double newX = Math.sin(angle);
        double newY = -Math.abs(Math.cos(angle));

        this.moveVector = new Point2D(newX, newY).normalize();
    }
}