import objects.*;
import objects.Point;
import util.ArrayUtils;

import java.awt.*;

/**
 * Класс, реализующий логику игры
 */
public class Game {
    /**
     * двумерный массив для хранения игрового поля
     * (в данном случае цветов, 0 - пусто; создается / пересоздается при старте игры)
     */
    private ColorEntity[][] field = null;

    public Game() {
    }

    public void newGame(String lvlName) {
        // создаем поле
        int[][] newField = ArrayUtils.readIntArray2FromFile(lvlName);
        this.field = new ColorEntity[newField.length][newField[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                switch (newField[i][j]) {
                    case 00 -> field[i][j] = null;
                    case 11 -> field[i][j] = new Ball(new Point(i, j), Color.RED, "Ball");
                    case 12 -> field[i][j] = new Ball(new Point(i, j), Color.BLUE, "Ball");
                    case 13 -> field[i][j] = new Ball(new Point(i, j), Color.ORANGE, "Ball");
                    case 14 -> field[i][j] = new Ball(new Point(i, j), Color.GREEN, "Ball");
                    case 21 -> field[i][j] = new Base(new Point(i, j), Color.RED, "Base");
                    case 22 -> field[i][j] = new Base(new Point(i, j), Color.BLUE, "Base");
                    case 23 -> field[i][j] = new Base(new Point(i, j), Color.ORANGE, "Base");
                    case 24 -> field[i][j] = new Base(new Point(i, j), Color.GREEN, "Base");
                    case 99 -> field[i][j] = new Wall(new Point(i, j), "Wall");
                }
            }
        }
    }

    ColorEntity object = null;
    public boolean leftMouseClick(int row, int col) {
        if (object == null || !this.object.getName().equals("Ball")) {
            this.object = getCell(row, col);
        } else {
            this.move(object.getPoint(), new Point(row, col));
            this.object = null;
        }
        return isWin();
    }

    public boolean consoleMove(Point point, String side) {
        switch (side) {
            case "U" -> moveUp(point.x, point.y);
            case "D" -> moveDown(point.x, point.y);
            case "L" -> moveLeft(point.x, point.y);
            case "R" -> moveRight(point.x, point.y);
        }
        return isWin();
    }

    public void move(Point start, Point stop) {
        if (start.x != stop.x && start.y == stop.y) {
            if (start.x > stop.x) {
                this.moveUp(start.x, start.y);
            } else {
                this.moveDown(start.x, start.y);
            }
        } else if (start.x == stop.x){
            if (start.y > stop.y) {
                this.moveLeft(start.x, start.y);
            } else {
                this.moveRight(start.x, start.y);
            }
        }
    }
    // x - row - i     y - col - j
    public void moveUp(int startRow, int startCol) {
        if (getCell(startRow, startCol).getName().equals("Base")) return;
        int stopRow = startRow;
        while (getCell(stopRow - 1, startCol) == null) {
            stopRow--;
        }
        if (getCell(stopRow - 1, startCol) instanceof Base && getCell(startRow, startCol).getColor() == getCell(stopRow - 1, startCol).getColor()) {
            this.field[startRow][startCol] = null;
        } else  if (stopRow != startRow){
            this.field[stopRow][startCol] = getCell(startRow, startCol);
            this.field[startRow][startCol] = null;
            getCell(stopRow, startCol).setPoint(new Point(stopRow, startCol));
        }
    }

    public void moveDown(int startRow, int startCol) {
        if (getCell(startRow, startCol).getName().equals("Base")) return;
        int stopRow = startRow;
        while (getCell(stopRow + 1, startCol) == null) {
            stopRow++;
        }
        if (getCell(stopRow + 1, startCol) instanceof Base && getCell(startRow, startCol).getColor() == getCell(stopRow + 1, startCol).getColor()) {
            this.field[startRow][startCol] = null;
        } else if (stopRow != startRow) {
            this.field[stopRow][startCol] = getCell(startRow, startCol);
            this.field[startRow][startCol] = null;
            getCell(stopRow, startCol).setPoint(new Point(stopRow, startCol));
        }
    }

    public void moveLeft(int startRow, int startCol) {
        if (getCell(startRow, startCol).getName().equals("Base")) return;
        int stopCol = startCol;
        while (getCell(startRow, stopCol - 1) == null) {
            stopCol--;
        }
        if (getCell(startRow, stopCol - 1) instanceof Base && getCell(startRow, startCol).getColor() == getCell(startRow, stopCol - 1).getColor()) {
            this.field[startRow][startCol] = null;
        } else if (stopCol != startCol){
            this.field[startRow][stopCol] = getCell(startRow, startCol);
            this.field[startRow][startCol] = null;
            getCell(startRow, stopCol).setPoint(new Point(startRow, stopCol));
        }
    }

    public void moveRight(int startRow, int startCol) {
        if (getCell(startRow, startCol).getName().equals("Base")) return;
        int stopCol = startCol;
        while (getCell(startRow, stopCol + 1) == null) {
            stopCol++;
        }
        if (getCell(startRow, stopCol + 1) instanceof Base && getCell(startRow, startCol).getColor() == getCell(startRow, stopCol + 1).getColor()) {
            this.field[startRow][startCol] = null;
        } else  if (stopCol != startCol){
            this.field[startRow][stopCol] = getCell(startRow, startCol);
            this.field[startRow][startCol] = null;
            getCell(startRow, stopCol).setPoint(new Point(startRow, stopCol));
        }
    }

    public boolean isWin() {
        for (ColorEntity[] entities : field) {
            for (ColorEntity entity : entities) {
                if (entity instanceof Ball) return false;
            }
        }
        return true;
    }
    public int getRowCount() {
        return field == null ? 0 : field.length;
    }

    public int getColCount() {
        return field == null ? 0 : field[0].length;
    }

    public ColorEntity getCell(int row, int col)  {
        return this.field[row][col];
    }

    public ColorEntity[][] getField() {return this.field;}
}
