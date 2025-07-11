package pl.umcs.oop;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends GraphicsItem {
    private static int gridRows;
    private static int gridCols;
    private Color color;

    // Krok 7: Typ wyliczeniowy CrushType
    public enum CrushType {
        NoCrush,
        HorizontalCrush,
        VerticalCrush
    }

    public static void setGridRows(int gridRows) {
        Brick.gridRows = gridRows;
    }

    public static void setGridCols(int gridCols) {
        Brick.gridCols = gridCols;
    }

    public Brick(int gridX, int gridY, Color color, int gridRows, int gridCols) {
        this.color = color;
        this.width = canvasWidth / gridCols;
        this.height = canvasHeight / gridRows;

        this.x = gridX * width;
        this.y = gridY * height;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Główny kolor cegły
        gc.setFill(color);
        gc.fillRect(x, y, width, height);

        // Efekt 3D - jaśniejsze krawędzie góra i lewa
        gc.setFill(color.brighter());
        gc.fillRect(x, y, width, height * 0.1); // górna krawędź
        gc.fillRect(x, y, width * 0.05, height); // lewa krawędź

        // Efekt 3D - ciemniejsze krawędzie dół i prawa
        gc.setFill(color.darker());
        gc.fillRect(x + width - width * 0.05, y, width * 0.05, height); // prawa krawędź
        gc.fillRect(x, y + height - height * 0.1, width, height * 0.1); // dolna krawędź
    }

    // Krok 7: Metoda sprawdzająca kolizję z piłką
    public CrushType checkCollision(Point2D topPoint, Point2D bottomPoint, Point2D leftPoint, Point2D rightPoint) {
        // Sprawdzamy czy któryś z punktów piłki jest wewnątrz cegły
        boolean collision = false;

        // Sprawdzamy kolizję każdego punktu z prostokątem cegły
        if (isPointInBrick(topPoint) || isPointInBrick(bottomPoint) ||
                isPointInBrick(leftPoint) || isPointInBrick(rightPoint)) {
            collision = true;
        }

        if (!collision) {
            return CrushType.NoCrush;
        }

        // Określamy typ kolizji na podstawie pozycji punktów
        // Sprawdzamy które strony cegły są najbliżej centrum piłki
        double ballCenterX = (leftPoint.getX() + rightPoint.getX()) / 2;
        double ballCenterY = (topPoint.getY() + bottomPoint.getY()) / 2;

        double brickCenterX = this.x + this.width / 2;
        double brickCenterY = this.y + this.height / 2;

        // Obliczamy odległości do krawędzi
        double distanceToLeft = Math.abs(ballCenterX - this.x);
        double distanceToRight = Math.abs(ballCenterX - (this.x + this.width));
        double distanceToTop = Math.abs(ballCenterY - this.y);
        double distanceToBottom = Math.abs(ballCenterY - (this.y + this.height));

        // Znajdź najmniejszą odległość
        double minHorizontal = Math.min(distanceToLeft, distanceToRight);
        double minVertical = Math.min(distanceToTop, distanceToBottom);

        // Jeśli piłka jest bliżej górnej/dolnej krawędzi, to odbicie pionowe
        if (minVertical < minHorizontal) {
            return CrushType.VerticalCrush;
        } else {
            return CrushType.HorizontalCrush;
        }
    }

    // Pomocnicza metoda sprawdzająca czy punkt jest wewnątrz cegły
    private boolean isPointInBrick(Point2D point) {
        return point.getX() >= this.x && point.getX() <= this.x + this.width &&
                point.getY() >= this.y && point.getY() <= this.y + this.height;
    }
}