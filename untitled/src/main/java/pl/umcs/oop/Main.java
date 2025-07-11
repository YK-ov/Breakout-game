package pl.umcs.oop;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Iterator;

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

        canvas.loadLevel();

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
        checkBrickCollisions(); // Nowa metoda!

        // Reset piłki gdy spadnie na dół
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

            // Ustaw pozycję piłki nad paletką
            ball.setY(paddle.getY() - ball.getHeight());

            // Oblicz pozycję uderzenia względem środka paletki
            double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
            double ballCenter = ball.getX() + ball.getWidth() / 2;
            double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);

            // Używamy nowej metody bounceFromPaddle
            ball.bounceFromPaddle(hitPosition);
        }
    }

    // Nowa metoda sprawdzająca kolizje z cegłami
    private void checkBrickCollisions() {
        Iterator<Brick> brickIterator = canvas.getBricks().iterator();

        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();

            // Pobieramy skrajne punkty piłki
            Point2D topPoint = ball.getTopPoint();
            Point2D bottomPoint = ball.getBottomPoint();
            Point2D leftPoint = ball.getLeftPoint();
            Point2D rightPoint = ball.getRightPoint();

            // Sprawdzamy kolizję
            Brick.CrushType crushType = brick.checkCollision(topPoint, bottomPoint, leftPoint, rightPoint);

            if (crushType != Brick.CrushType.NoCrush) {
                // Usuwamy cegłę
                brickIterator.remove();

                // Odbijamy piłkę w odpowiednim kierunku
                switch (crushType) {
                    case HorizontalCrush:
                        ball.bounceHorizontal();
                        break;
                    case VerticalCrush:
                        ball.bounceVertical();
                        break;
                }

                // Przerwij sprawdzanie po pierwszej kolizji
                break;
            }
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