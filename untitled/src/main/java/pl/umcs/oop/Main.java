package pl.umcs.oop;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
//s
public class Main extends Application {
    private Ball ball;
    private Paddle paddle;
    private GameCanvas canvas;
    private AnimationTimer gameLoop;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Breakout Game");
        canvas = new GameCanvas(1920, 1080);

        GraphicsItem.setCanvasWidth(canvas.getWidth());
        GraphicsItem.setCanvasHeight(canvas.getHeight());

        paddle = new Paddle();
        canvas.setPaddle(paddle);

        ball = new Ball();
        ball.setPosition(new Point2D(960, 540));

        System.out.println("Ball dimensions: " + ball.getWidth() + "x" + ball.getHeight());
        System.out.println("Ball position: " + ball.getX() + ", " + ball.getY());


        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 1920, 1080);

        setupKeyboardControls(scene);

        startGameLoop();

        stage.setScene(scene);
        stage.show();

        canvas.setFocusTraversable(true);
        canvas.requestFocus();
    }

    private void setupKeyboardControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            switch (key) {
                case LEFT:
                case A:
                    paddle.moveLeft();
                    break;
                case RIGHT:
                case D:
                    paddle.moveRight();
                    break;
                case SPACE:
                    ball.setPosition(new Point2D(960, 540));
                    double angle = Math.toRadians(45);
                    ball.setMoveVector(new Point2D(Math.cos(angle), -Math.sin(angle))); // w górę
                    break;
                case ESCAPE:
                    if (gameLoop != null) {
                        gameLoop.stop();
                        gameLoop = null;
                    } else {
                        startGameLoop();
                    }
                    break;
            }
        });
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
        gameLoop.start();
    }

    private void update() {
        ball.updatePosition();

        checkWallCollisions();

        checkPaddleCollision();

        if (ball.getY() > canvas.getHeight()) {
            ball.setPosition(new Point2D(960, 540));
            double angle = Math.toRadians(45);
            ball.setMoveVector(new Point2D(Math.cos(angle), -Math.sin(angle)));
        }
    }

    private void checkWallCollisions() {
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= canvas.getWidth()) {
            ball.bounceHorizontal();

            if (ball.getX() <= 0) {
                ball.setX(0);
            } else {
                ball.setX(canvas.getWidth() - ball.getWidth());
            }
        }

        if (ball.getY() <= 0) {
            ball.bounceVertical();
            ball.setY(0);
        }
    }

    private void checkPaddleCollision() {
        if (ball.getX() < paddle.getX() + paddle.getWidth() &&
                ball.getX() + ball.getWidth() > paddle.getX() &&
                ball.getY() < paddle.getY() + paddle.getHeight() &&
                ball.getY() + ball.getHeight() > paddle.getY()) {

            ball.bounceVertical();

            ball.setY(paddle.getY() - ball.getHeight());

            double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
            double ballCenter = ball.getX() + ball.getWidth() / 2;
            double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);

            hitPosition = Math.max(-1, Math.min(1, hitPosition));

            double angle = hitPosition * Math.PI / 4;
            Point2D newVector = new Point2D(Math.sin(angle), -Math.abs(Math.cos(angle)));
            ball.setMoveVector(newVector);
        }
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.draw();

        paddle.draw(gc);
        ball.draw(gc);
    }

    @Override
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}