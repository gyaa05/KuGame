import objects.ColorEntity;
import java.awt.*;
import java.util.Scanner;

public class Console {
    public int level;
    private static final int DEFAULT_COL_COUNT = 6;
    private static final int DEFAULT_ROW_COUNT = 6;
    private GameParams params = new GameParams(DEFAULT_ROW_COUNT, DEFAULT_COL_COUNT);
    private Game game = new Game();

    public Console() {
        int level;
        System.out.println("Вас приветствует Kugame!");
        System.out.println("Краткое руководство:");
        System.out.println("-- - стена");
        System.out.println("** - общий паттерн элемента");
        System.out.println("На первом месте цвет по первой букве названия");
        System.out.println("На втором b - шар, B - база");
        System.out.println("Введите номер уровня:");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                level = scanner.nextInt();
                if (level >= 1 && level <= 3) {
                    break;
                }
                System.out.println("Доступны только три уровня");
            }
            System.out.println("Введите цифру");
        }
        this.level = level;
        allGame();
    }

    private void allGame() {
        newGame();
        boolean isWin = false;
        while (!isWin) {
            String[] move;
            ColorEntity ball;
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Чтобы сделать ход напишите(через пробел) цвет шара(например Red),");
                System.out.println("направление(D - вниз, U - вверх, L - влево, R - вправо):");
                move = scanner.nextLine().split(" ");
                if ((move[0].equals("Red") || move[0].equals("Blue") || move[0].equals("Green") || move[0].equals("Orange")) && (move[1].equals("D") || move[1].equals("R") || move[1].equals("U") || move[1].equals("L"))) {
                    ball = getBall(move[0]);
                    if (ball != null) {
                        break;
                    }
                }
            }
            isWin = game.consoleMove(ball.getPoint(), move[1]);
            updateView();
        }
        System.out.println("Поздравляем, вы победили!!!");
    }

    private void newGame() {
        String chooseLevel = "levels/lvl_" + Integer.toString(level) + ".txt";
        game.newGame(chooseLevel);
        updateView();
    }

    private void updateView() {
        ColorEntity[][] field = game.getField();
        for (ColorEntity[] entities : field) {
            for (ColorEntity entity : entities) {
                if (entity == null) {
                    System.out.print("  ");
                } else {
                    if (entity.getName().equals("Ball")) {
                        if (entity.getColor() == Color.BLUE) {
                            System.out.print("Bb");
                        } else if (entity.getColor() == Color.RED) {
                            System.out.print("Rb");
                        } else if (entity.getColor() == Color.ORANGE) {
                            System.out.print("Ob");
                        } else if (entity.getColor() == Color.GREEN) {
                            System.out.print("Gb");
                        }
                    } else if (entity.getName().equals("Base")) {
                        if (entity.getColor() == Color.BLUE) {
                            System.out.print("BB");
                        } else if (entity.getColor() == Color.RED) {
                            System.out.print("RB");
                        } else if (entity.getColor() == Color.ORANGE) {
                            System.out.print("OB");
                        } else if (entity.getColor() == Color.GREEN) {
                            System.out.print("GB");
                        }
                    } else {
                        System.out.print("--");
                    }
                }
            }
            System.out.println();
        }
    }

    private ColorEntity getBall(String color) {
        Color needColor = null;
        switch (color) {
            case "Red" -> needColor = Color.RED;
            case "Blue" -> needColor = Color.BLUE;
            case "Orange" -> needColor = Color.ORANGE;
            case "Green" -> needColor = Color.GREEN;
        }
        ColorEntity[][] field = game.getField();
        for (ColorEntity[] entities : field) {
            for (ColorEntity entity : entities) {
                if (entity != null && entity.getName().equals("Ball") && entity.getColor() == needColor) {
                    return entity;
                }
            }
        }
        return null;
    }
}
