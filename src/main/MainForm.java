import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import objects.ColorEntity;
import util.JTableUtils;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JTable tableGameField;
    private JLabel labelStatus;

    private static final int DEFAULT_COL_COUNT = 6;
    private static final int DEFAULT_ROW_COUNT = 6;
    private static final int DEFAULT_GAP = 8;
    private static final int DEFAULT_CELL_SIZE = 40;

    private GameParams params = new GameParams(DEFAULT_ROW_COUNT, DEFAULT_COL_COUNT);
    private Game game = new Game();

    public int level;

    public MainForm(int level) {
        this.setTitle("Kugame");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.level = level;
        this.pack();

        setJMenuBar(createMenuBar());
        this.pack();

        SwingUtils.setShowMessageDefaultErrorHandler();

        tableGameField.setRowHeight(DEFAULT_CELL_SIZE);
        JTableUtils.initJTableForArray(tableGameField, DEFAULT_CELL_SIZE, false, false, false, false);
        tableGameField.setIntercellSpacing(new Dimension(0, 0));
        tableGameField.setEnabled(false);

        tableGameField.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            final class DrawComponent extends Component {
                private int row = 0, column = 0;

                @Override
                public void paint(Graphics gr) {
                    Graphics2D g2d = (Graphics2D) gr;
                    int width = getWidth() - 2;
                    int height = getHeight() - 2;
                    paintCell(row, column, g2d, width, height);
                }
            }

            DrawComponent comp = new DrawComponent();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                comp.row = row;
                comp.column = column;
                return comp;
            }
        });

        String chooseLevel = "levels/lvl_" + Integer.toString(level) + ".txt";
        newGame(chooseLevel);

        updateWindowSize();
        updateView();

        tableGameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = tableGameField.rowAtPoint(e.getPoint());
                int col = tableGameField.columnAtPoint(e.getPoint());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    boolean isWin = game.leftMouseClick(row, col);
                    updateView();
                    if (isWin) {
                        if (level + 1 < 4) {
                            EventQueue.invokeLater(() -> new MainForm(level + 1).setVisible(true));
                        } else {
                            SwingUtils.showInfoMessageBox("???? ????????... ???? ???????? ????????????????");
                            dispose();
                        }
                    }
                }
            }
        });
    }

    private JMenuItem createMenuItem(String text, String shortcut, Character mnemonic, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        if (shortcut != null) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut.replace('+', ' ')));
        }
        if (mnemonic != null) {
            menuItem.setMnemonic(mnemonic);
        }
        return menuItem;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBarMain = new JMenuBar();

        JMenu menuGame = new JMenu("????????");
        menuBarMain.add(menuGame);
        menuGame.add(createMenuItem("??????????", "ctrl+N", null, e -> {
            String chooseLevel = "levels/lvl_" + Integer.toString(level) + ".txt";
            newGame(chooseLevel);
        }));
        menuGame.addSeparator();
        menuGame.add(createMenuItem("??????????", "ctrl+X", null, e -> {
            System.exit(0);
        }));

        JMenu menuView = new JMenu("??????");
        menuBarMain.add(menuView);
        menuView.add(createMenuItem("?????????????????? ???????????? ????????", null, null, e -> {
            updateWindowSize();
        }));
        menuView.addSeparator();
        SwingUtils.initLookAndFeelMenu(menuView);

        JMenu menuHelp = new JMenu("??????????????");
        menuBarMain.add(menuHelp);
        menuHelp.add(createMenuItem("??????????????", "ctrl+R", null, e -> {
            SwingUtils.showInfoMessageBox("?????????? ???????????? ???????? ?????????????? ???????????????? ???????????? ...", "??????????????");
        }));
        menuHelp.add(createMenuItem("?? ??????????????????", "ctrl+A", null, e -> {
            SwingUtils.showInfoMessageBox(
                    "???????????? ?????? ???????????????? ????????" +
                            "\n\n??????????: ?????????????????? ??.??." +
                            "\nE-mail: solomatin.cs.vsu.ru@gmail.com",
                    "?? ??????????????????"
            );
        }));

        return menuBarMain;
    }

    private void updateWindowSize() {
        int menuSize = this.getJMenuBar() != null ? this.getJMenuBar().getHeight() : 0;
        SwingUtils.setFixedSize(
                this,
                tableGameField.getWidth() + 2 * DEFAULT_GAP + 60,
                tableGameField.getHeight() + panelMain.getY() + labelStatus.getHeight() +
                        menuSize + 1 * DEFAULT_GAP + 2 * DEFAULT_GAP + 60
        );
        this.setMaximumSize(null);
        this.setMinimumSize(null);
    }

    private void updateView() {
        tableGameField.repaint();
    }


    private Font font = null;

    private Font getFont(int size) {
        if (font == null || font.getSize() != size) {
            font = new Font("Comic Sans MS", Font.BOLD, size);
        }
        return font;
    }

    private void paintCell(int row, int column, Graphics2D g2d, int cellWidth, int cellHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ColorEntity object = game.getCell(row, column);

        int size = Math.min(cellWidth, cellHeight);
        int bound = (int) Math.round(size * 0.1);

        if (object != null) {
            switch (object.getName()) {
                case "Ball": {
                    g2d.setColor(object.getColor());
                    g2d.fillOval(bound, bound, size - 2 * bound, size - 2 * bound);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawOval(bound, bound, size - 2 * bound, size - 2 * bound);
                    break;
                }
                case "Wall":
                case "Base": {
                    g2d.setColor(object.getColor());
                    g2d.fillRoundRect(bound, bound, size - 2 * bound, size - 2 * bound, bound * 3, bound * 3);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawRoundRect(bound, bound, size - 2 * bound, size - 2 * bound, bound * 3, bound * 3);
                    break;
                }
            }
        }
    }

    private void newGame(String level) {
        game.newGame(level);
        JTableUtils.resizeJTable(tableGameField,
                game.getRowCount(), game.getColCount(),
                tableGameField.getRowHeight(), tableGameField.getRowHeight()
        );
        updateView();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, 10));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableGameField = new JTable();
        scrollPane1.setViewportView(tableGameField);
        labelStatus = new JLabel();
        labelStatus.setText("Label");
        panelMain.add(labelStatus, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
