import java.util.Locale;

/**
 * Класс с методом main
 */
public class Program {

    public static class CmdParams {
        public boolean error;
        public boolean help;
        public boolean window;
        public boolean console;
    }
    public static CmdParams parseArgs(String[] args) {
        CmdParams params = new CmdParams();
        if (args.length == 1) {
            if (args[0].equals("--console")) {
                params.console = true;
            } else if (args[0].equals("--window")) {
                params.window = true;
            } else {
                params.error = true;
                params.help = true;
            }
        } else {
            params.error = true;
            params.help = true;
        }
        return params;
    }
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);
        CmdParams params = parseArgs(args);
        if (params.help && params.error) {
            System.out.println("Usage:");
            System.out.println(" <cmd> args");
            System.out.println("--console // console version");
            System.out.println("--window // window version");
        } else if (params.window) {
            java.awt.EventQueue.invokeLater(() -> new Chooser().setVisible(true));
        } else {
            new Console();
        }
    }
}
